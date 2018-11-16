package com.company.hrm.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.DeletePolicy;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.persistence.Lob;

@Listeners("hrm_OrgUnitEntityListener")
@NamePattern("%s|title")
@Table(name = "HRM_ORG_UNIT")
@Entity(name = "hrm$OrgUnit")
public class OrgUnit extends StandardEntity {
    private static final long serialVersionUID = 1792196161836990838L;

    @CaseConversion
    @NotNull
    @Column(name = "CODE", nullable = false, unique = true, length = 100)
    protected String code;

    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_LEVEL_ID")
    protected OrgLevel orgLevel;

    @NotNull
    @Column(name = "TITLE", nullable = false)
    protected String title;

    @Column(name = "DESCRIPTION")
    protected String description;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open", "clear"})
    @OnDeleteInverse(DeletePolicy.CASCADE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    protected OrgUnit parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUPERVISOR_ID")
    protected Employee supervisor;

    @Lookup(type = LookupType.DROPDOWN, actions = {"lookup", "open", "clear"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MANAGER_ID")
    protected Employee manager;

    @OrderBy("order ASC")
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "parent")
    protected List<OrgUnit> children;


    @OneToMany(mappedBy = "orgUnit")
    @OrderBy("firstName ASC")
    protected List<Employee> employees;




    @OrderBy("level ASC")
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "orgUnit")
    protected List<OrgUnitHierarchy> upHierarchyList;

    @OneToMany(mappedBy = "parent")
    protected List<OrgUnitHierarchy> downHierarchyList;




    @Column(name = "ORDER_")
    protected Integer order;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }


    public void setOrgLevel(OrgLevel orgLevel) {
        this.orgLevel = orgLevel;
    }

    public OrgLevel getOrgLevel() {
        return orgLevel;
    }


    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Employee getManager() {
        return manager;
    }


    public void setDownHierarchyList(List<OrgUnitHierarchy> downHierarchyList) {
        this.downHierarchyList = downHierarchyList;
    }

    public List<OrgUnitHierarchy> getDownHierarchyList() {
        return downHierarchyList;
    }


    public void setUpHierarchyList(List<OrgUnitHierarchy> upHierarchyList) {
        this.upHierarchyList = upHierarchyList;
    }

    public List<OrgUnitHierarchy> getUpHierarchyList() {
        return upHierarchyList;
    }


    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }

    public Employee getSupervisor() {
        return supervisor;
    }



    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }



    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }


    public OrgUnit getParent() {
        return parent;
    }

    public void setParent(OrgUnit parent) {
        this.parent = parent;
    }


    public List<OrgUnit> getChildren() {
        return children;
    }

    public void setChildren(List<OrgUnit> children) {
        this.children = children;
    }






    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}