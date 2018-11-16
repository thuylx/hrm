package com.company.hrm.listener;

import com.company.hrm.entity.OrgUnit;
import com.company.hrm.entity.OrgUnitHierarchy;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;

@Component("hrm_OrgUnitEntityListener")
public class OrgUnitEntityListener implements BeforeInsertEntityListener<OrgUnit>, BeforeUpdateEntityListener<OrgUnit> {
    @Inject
    private Persistence persistence;
    @Inject
    private Metadata metadata;

    @Override
    public void onBeforeInsert(OrgUnit entity, EntityManager entityManager) {
        createNewHierarchy(entity, entity.getParent(), entityManager);
    }

    @Override
    public void onBeforeUpdate(OrgUnit entity, EntityManager entityManager) {
        if (!persistence.getTools().getDirtyFields(entity).contains("parent"))
            return;

        createNewHierarchy(entity, entity.getParent(), entityManager);

        for (OrgUnitHierarchy oldHierarchy: entity.getDownHierarchyList()) {
            OrgUnit dependentOrgUnit = oldHierarchy.getOrgUnit();
            for (OrgUnitHierarchy depHierarchy : dependentOrgUnit.getUpHierarchyList()) {
                entityManager.remove(depHierarchy);
            }
            entityManager.remove(oldHierarchy);
            createNewHierarchy(dependentOrgUnit, dependentOrgUnit.getParent(), entityManager);
        }
    }

    protected void createNewHierarchy(OrgUnit entity, OrgUnit parent, EntityManager entityManager) {
        if (parent == null) {
            entity.setUpHierarchyList(new ArrayList<>());

            return;
        }

        if (!PersistenceHelper.isManaged(parent) && !PersistenceHelper.isDetached(parent))
            throw new IllegalStateException("Unable to create OrgUnitHierarchy. Commit parent OrgUnit first.");

        if (entity.getUpHierarchyList() == null) {
            entity.setUpHierarchyList(new ArrayList<>());
        } else {
            for (OrgUnitHierarchy hierarchy: entity.getUpHierarchyList()) {
                entityManager.remove(hierarchy);
            }
            entity.getUpHierarchyList().clear();
        }

        if (PersistenceHelper.isDetached(parent))
            parent = entityManager.find(OrgUnit.class, parent.getId()); // refresh parent in case of detached

        if (parent.getUpHierarchyList() == null) {
            parent.setUpHierarchyList(new ArrayList<>());
        }

        int level = 1;
        for (OrgUnitHierarchy hierarchy : parent.getUpHierarchyList()) {
            OrgUnitHierarchy h = metadata.create(OrgUnitHierarchy.class);
            h.setOrgUnit(entity);
            h.setParent(hierarchy.getParent());
            h.setLevel(level++);
            entityManager.persist(h);
            entity.getUpHierarchyList().add(h);
        }
        OrgUnitHierarchy h = metadata.create(OrgUnitHierarchy.class);
        h.setOrgUnit(entity);
        h.setParent(parent);
        h.setLevel(level);
        entityManager.persist(h);
        entity.getUpHierarchyList().add(h);
    }
}