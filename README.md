# HRM
This project is use to provide organizational structure and employees management. 
There are three main entities: `OrgLevel`, `OrgUnit`, and `Employee`.

The fields of these entities to form the organizational structure is described below. Keep in mind that all of these entities is UUID based ID.

## OrgLevel

OrgLevel is used to specify level of an Organizational Unit. There will be a bean to provide method to detect manager or supervisor of given username at given org level code.  
This will be useful in case of, for example, approval process which we need determine department head, line manager etc of an employee

1. `code`: String, human readable identity for the org level
2. `name`
3. `order`: precedence number to short and display in the list. Usually the smaller mean higher level.
4. `active`: usable or not.

## OrgUnit

OrgUnit is short term of Organizational Unit, which can be any unit such as division, department, work group or hole company.

The OrgUnit is structured in tree, each unit can have a specified manager and a supervisor. There will be a bean to provide function to determine manager and supervisor if any of given OrgUnit code.

1. `code`: String, human readable identity for an Organizational Unit
2. `name`
3. `orgLevel`: specifies level of OrgUnit.
4. `parent`: direct parent organizational unit
5. `manager`: Employee object
6. `supervisor`: Employee object
7. `children`: list of child Organizational Unit (which set parent to this OrgUnit)
8. `employees`: list of employees who is directly belong to this OrgUnit (not include employee of nested OrgUnit).
9. `upHierarchyList`: list of OrgUnitHierarchy object which refer to higher level unit. Se definition of OrgUnitHierarchy below.
10. `downHierarchyList`: list of OrgUnitHierarchy object which refer to subsidiaries units.

## OrgUnitHierarchy

Once OrgUnit is inserted or updated, the OrgUnitEntityListener will update OrgUnitHierarchy to contain all of organizational tree information for any node.  
This design is for quick query Organizational Structure.

OrgUnitHierarchy has below properties:

1. `orgUnit`: the OrgUnit object of interest
2. `parent`: OrgUnit at higher level. The term of parent here does not mean exactly parent of the orgUnit but can be parent of parent, and any higher level OrgUnit in the path to the tree root.  
Once OrgUnit is inserted or updated, the entity listener will create or update OrgUnitHierarchy records to for all of relation to higher level OrgUnit.
3. `level`: level of the `parent` in the tree. level 1 mean root.

## Employee

Employee is class to present business user. A business user may link or not link to a system (security) user.

Main properties of Employee:

1. `code`: human readable identity for employee
2. `firstName`, `lastName`, transient `fullName` which computed from `firstName` and `lastName`
3. `user`: system (security) User object
4. `orgUnit`: OrgUnit object which employee belong to
5. `userName`: transient field which get from `user` object.

# Organizational structure query
With above design, the relationship between employee and any manager, supervisor at any higher level can be determined using OrgUnit tree and relevant manager, supervisor.

This Application component provide bean which exposed to BPMN expression to query the org structure.  
The bean named `hrm` implements Hrm interface, provides functions as following:

```java
public interface Hrm extends BpmExposedBean {
    String NAME = "hrm";

    Employee getCurrentEmployee();

    Employee getSubstitutedEmployee();

    Employee getCurrentOrSubstitutedEmployee();

    Employee getLineManager(String userName);

    String getLineManagerUserName(String userName);

    Employee getTopManager();

    String getTopManagerUserName();

    Employee getManager(String orgLevelCode, String userName);

    String getManagerUserName(String orgLevelCode, String userName);

    Employee getSupervisor(String orgLevelCode, String userName);

    String getSupervisorUserName(String orgLevelCode, String userName);

    Employee getManagerByOrgUnit(String orgUnitCode);

    String getManagerUserNameByOrgUnit(String orgUnitCode);

    Employee getSupervisorByOrgUnit(String orgUnitCode);

    String getSupervisorUserNameByOrgUnit(String orgUnitCode);
}
```

    