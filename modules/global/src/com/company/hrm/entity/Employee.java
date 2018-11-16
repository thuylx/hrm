package com.company.hrm.entity;

import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.*;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Listeners("hrm_EmployeeEntityListener")
@NamePattern("%s, %s|firstName,lastName")
@Table(name = "HRM_EMPLOYEE")
@Entity(name = "hrm$Employee")
public class Employee extends StandardEntity {
    private static final long serialVersionUID = -8747005723800626343L;

    @CaseConversion
    @NotNull
    @Column(name = "CODE", nullable = false, unique = true, length = 100)
    protected String code;

    @NotNull
    @Column(name = "FIRST_NAME", nullable = false)
    protected String firstName;

    @OrderBy("order ASC")
    @OneToMany(mappedBy = "manager")
    protected List<OrgUnit> managingOrgUnits;

    @OrderBy("order ASC")
    @OneToMany(mappedBy = "supervisor")
    protected List<OrgUnit> supervisingOrgUnits;

    @Column(name = "LAST_NAME")
    protected String lastName;

    @Lookup(type = LookupType.SCREEN, actions = {"lookup", "open", "clear"})
    @OnDeleteInverse(DeletePolicy.UNLINK)
    @OnDelete(DeletePolicy.UNLINK)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "")
    @JoinColumn(name = "USER_ID")
    protected User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_UNIT_ID")
    protected OrgUnit orgUnit;
    @MetaProperty
    protected String userName;
    @MetaProperty(related = {"firstName", "lastName"})
    protected String fullName;

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    public void setManagingOrgUnits(List<OrgUnit> managingOrgUnits) {
        this.managingOrgUnits = managingOrgUnits;
    }

    public List<OrgUnit> getManagingOrgUnits() {
        return managingOrgUnits;
    }

    public List<OrgUnit> getSupervisingOrgUnits() {
        return supervisingOrgUnits;
    }

    public void setSupervisingOrgUnits(List<OrgUnit> supervisingOrgUnits) {
        this.supervisingOrgUnits = supervisingOrgUnits;
    }


    public void setOrgUnit(OrgUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    public OrgUnit getOrgUnit() {
        return orgUnit;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }


}