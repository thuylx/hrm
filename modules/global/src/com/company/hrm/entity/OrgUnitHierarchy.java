package com.company.hrm.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import com.haulmont.cuba.core.entity.StandardEntity;

@Table(name = "HRM_ORG_UNIT_HIERARCHY")
@Entity(name = "hrm$OrgUnitHierarchy")
public class OrgUnitHierarchy extends StandardEntity {
    private static final long serialVersionUID = 1686801132715784512L;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ORG_UNIT_ID")
    protected OrgUnit orgUnit;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open"})
    @NotNull
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PARENT_ID")
    protected OrgUnit parent;

    @NotNull
    @Column(name = "HIERARCHY_LEVEL", nullable = false)
    protected Integer level;

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }


    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }

    public void setParent(OrgUnit parent) {
        this.parent = parent;
    }

    public OrgUnit getParent() {
        return parent;
    }


}