package com.company.hrm.core.bpm.impl;

import com.company.hrm.core.bpm.Hrm;
import com.company.hrm.entity.Employee;
import com.company.hrm.entity.OrgUnit;
import com.company.hrm.entity.OrgUnitHierarchy;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.entity.User;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component(Hrm.NAME)
public class HrmImpl implements Hrm {
    @Inject
    private UserSessionSource userSessionSource;
    @Inject
    private Persistence persistence;
    @Inject
    private Metadata metadata;

    @Override
    public Employee getCurrentEmployee() {
        if (userSessionSource.checkCurrentUserSession()) {
            return getEmployeeByUserName(userSessionSource.getUserSession().getUser().getLogin());
        }

        return createBlankEmployee();
    }

    @Override
    public Employee getSubstitutedEmployee() {
        if (userSessionSource.checkCurrentUserSession()) {
            return getEmployeeByUserName(userSessionSource.getUserSession().getSubstitutedUser().getLogin());
        }
        return createBlankEmployee();
    }

    @Override
    public Employee getCurrentOrSubstitutedEmployee() {
        if (userSessionSource.checkCurrentUserSession()) {
            return getEmployeeByUserName(userSessionSource.getUserSession().getCurrentOrSubstitutedUser().getLogin());
        }
        return createBlankEmployee();
    }

    @Override
    public Employee getLineManager(String userName) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            Employee employee = getEmployeeByUserName(userName, em);

            Employee lineManager;
            if (!employee.getId().equals(employee.getOrgUnit().getManager().getId())) {
                employee.getOrgUnit().getManager().getUser();
                lineManager = employee.getOrgUnit().getManager();
            } else {
                employee.getOrgUnit().getParent().getManager().getUser();
                lineManager = employee.getOrgUnit().getParent().getManager();
            }

            return lineManager;
        } catch (Exception e) {
            return createBlankEmployee();
        }
    }

    @Override
    public Employee getTopManager() {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager entityManager = persistence.getEntityManager();
            TypedQuery<OrgUnit> query = entityManager.createQuery("select o from hrm$OrgUnit o " +
                    "join fetch o.manager " +
                    "join fetch o.manager.user " +
                    "where o.parent is null", OrgUnit.class);

            OrgUnit orgUnit = query.getFirstResult();

            return orgUnit.getManager();
        } catch (Exception e) {
            return createBlankEmployee();
        }
    }

    @Override
    public Employee getManager(String orgLevelCode, String userName) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            Employee employee = getEmployeeByUserName(userName, em);

            if (employee.getOrgUnit() != null && employee.getOrgUnit().getOrgLevel() != null && orgLevelCode.equals(employee.getOrgUnit().getOrgLevel().getCode())) {
                employee.getOrgUnit().getManager().getUser();
                employee.getOrgUnit().getManager().getOrgUnit();

                return employee.getOrgUnit().getManager();
            }

            TypedQuery<OrgUnitHierarchy> query = em.createQuery("select h from hrm$OrgUnitHierarchy h " +
                    "join fetch h.parent.manager " +
                    "join fetch h.parent.manager.orgUnit " +
                    "join fetch h.parent.manager.user " +
                    "where h.orgUnit.id = :orgUnitId and h.parent.orgLevel.code = :orgLevelCode " +
                    "order by h.level desc", OrgUnitHierarchy.class);
            query.setParameter("orgUnitId", employee.getOrgUnit().getId());
            query.setParameter("orgLevelCode", orgLevelCode);

            OrgUnitHierarchy hierarchy = query.getFirstResult();

            if (hierarchy == null || hierarchy.getParent() == null) {
                return null;
            }

            return hierarchy.getParent().getManager();
        } catch (Exception e) {
            return createBlankEmployee();
        }
    }

    @Override
    public Employee getSupervisor(String orgLevelCode, String userName) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            Employee employee = getEmployeeByUserName(userName, em);

            if (employee.getOrgUnit().getOrgLevel().getCode().equals(orgLevelCode)) {
                employee.getOrgUnit().getSupervisor().getUser();
                employee.getOrgUnit().getSupervisor().getOrgUnit();
                return employee.getOrgUnit().getSupervisor();
            }

            TypedQuery<OrgUnitHierarchy> query = em.createQuery("select h from hrm$OrgUnitHierarchy h " +
                    "join fetch h.parent.supervisor " +
                    "join fetch h.parent.supervisor.orgUnit " +
                    "join fetch h.parent.supervisor.user " +
                    "where h.orgUnit.id = :orgUnitId and h.parent.orgLevel.code = :orgLevelCode " +
                    "order by h.level desc", OrgUnitHierarchy.class);
            query.setParameter("orgUnitId", employee.getOrgUnit().getId());
            query.setParameter("orgLevelCode", orgLevelCode);

            OrgUnitHierarchy hierarchy = query.getFirstResult();

            if (hierarchy == null || hierarchy.getParent() == null) {
                return null;
            }

            return hierarchy.getParent().getSupervisor();
        } catch (Exception e) {
            return createBlankEmployee();
        }
    }

    @Override
    public Employee getManagerByOrgUnit(String orgUnitCode) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager entityManager = persistence.getEntityManager();
            OrgUnit orgUnit = getOrgUnitByName(orgUnitCode, entityManager);
            Employee manager = orgUnit.getManager();
            manager.getUser();
            manager.getOrgUnit();

            return manager;
        } catch (Exception e) {
            return createBlankEmployee();
        }
    }

    @Override
    public Employee getSupervisorByOrgUnit(String orgUnitCode) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager entityManager = persistence.getEntityManager();
            OrgUnit orgUnit = getOrgUnitByName(orgUnitCode, entityManager);
            Employee supervisor = orgUnit.getSupervisor();
            supervisor.getUser();
            supervisor.getOrgUnit();

            return supervisor;
        } catch (Exception e) {
            return createBlankEmployee();
        }
    }

    @Override
    public String getLineManagerUserName(String userName) {
        return getLineManager(userName).getUserName();
    }

    @Override
    public String getTopManagerUserName(){
        return getTopManager().getUserName();
    }

    @Override
    public String getManagerUserName(String orgLevelCode, String userName) {
        return getManager(orgLevelCode, userName).getUserName();
    }

    @Override
    public String getSupervisorUserName(String orgLevelCode, String userName) {
        return getSupervisor(orgLevelCode,userName).getUserName();
    }

    @Override
    public String getManagerUserNameByOrgUnit(String orgUnitCode) {
        return getManagerByOrgUnit(orgUnitCode).getUserName();
    }

    @Override
    public String getSupervisorUserNameByOrgUnit(String orgUnitCode) {
        return getSupervisorByOrgUnit(orgUnitCode).getUserName();
    }

    ///////////////////////////////////////////////////////////////////////

    private Employee getEmployeeByUserName(String userName) {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            return getEmployeeByUserName(userName, em);
        }
    }

    private Employee getEmployeeByUserName(String userName, EntityManager entityManager) {
        TypedQuery<Employee> query = entityManager.createQuery("select e from hrm$Employee e " +
                "join fetch e.orgUnit " +
                "join fetch e.user " +
                "where e.user.login = ?1", Employee.class);
        query.setParameter(1, userName);
        return query.getFirstResult();
    }

    private OrgUnit getOrgUnitByName(String orgUnitCode, EntityManager entityManager) {
        TypedQuery<OrgUnit> query = entityManager.createQuery("select e from hrm$OrgUnit e " +
                "where e.code = ?1", OrgUnit.class);
        query.setParameter(1, orgUnitCode);
        return query.getFirstResult();
    }

    private Employee createBlankEmployee() {
        OrgUnit orgUnit = metadata.create(OrgUnit.class);
        User user = metadata.create(User.class);
        Employee employee = metadata.create(Employee.class);
        employee.setOrgUnit(orgUnit);
        employee.setUser(user);
        return employee;
    }
}
