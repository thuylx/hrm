package com.company.hrm.listener;

import com.company.hrm.entity.Employee;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.core.listener.BeforeInsertEntityListener;
import com.haulmont.cuba.core.listener.BeforeUpdateEntityListener;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;

@Component("hrm_EmployeeEntityListener")
public class EmployeeEntityListener implements BeforeInsertEntityListener<Employee>, BeforeUpdateEntityListener<Employee> {
    @Inject
    private Persistence persistence;

    @Override
    public void onBeforeInsert(Employee entity, EntityManager entityManager) {
        updateUserInfo(entity, entityManager);
    }


    @Override
    public void onBeforeUpdate(Employee entity, EntityManager entityManager) {
        Set<String> dirtyFields = persistence.getTools().getDirtyFields(entity);
        if (dirtyFields.contains("firstName") || dirtyFields.contains("lastName") || dirtyFields.contains("user")) {
            updateUserInfo(entity, entityManager);
        }
    }

    private void updateUserInfo(Employee entity, EntityManager entityManager) {
        User user = entity.getUser();
        if (user != null) {
            if (PersistenceHelper.isDetached(user)) {
                user = entityManager.find(User.class, user.getId()); // refresh parent in case of detached
                entity.setUser(user);
            }
            user = entityManager.find(User.class, entity.getUser().getId());
            user.setFirstName(entity.getFirstName());
            user.setLastName(entity.getLastName());
            user.setName(String.format("%s %s", entity.getFirstName(), entity.getLastName()));
            entityManager.persist(user);
        }
    }
}