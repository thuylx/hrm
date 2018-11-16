package com.company.hrm.core;

import com.company.hrm.HrmTestContainer;
import com.company.hrm.core.bpm.Hrm;
import com.company.hrm.entity.Employee;
import com.company.hrm.entity.OrgLevel;
import com.company.hrm.entity.OrgUnit;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.annotation.Nullable;

import static org.junit.Assert.assertEquals;

public class BpmHrmImplTest {
    private static final String PASSWORD = "123456";

    @ClassRule
    public static HrmTestContainer cont = HrmTestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;
    private DataManager dataManager;

    private Group defaultGroup;

    @Before
    public void setUp() throws Exception {
        metadata = cont.metadata();
        persistence = cont.persistence();
        dataManager = AppBeans.get(DataManager.class);
        defaultGroup = dataManager.load(Group.class).one();

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            OrgUnit parent = null;
            for (int i = 1; i < 4; i++) {
                parent = createOrgUnitAndRelevantEmployees(i, parent, em);
            }

            tx.commit();
        }
    }

    @After
    public void tearDown() throws Exception {

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            removeOrgUnitAndRelevantEmployees(em);
            tx.commit();
        }
    }

    private OrgUnit createOrgUnitAndRelevantEmployees(int ordinalNumber, @Nullable OrgUnit parent, EntityManager em) {
        OrgLevel level = metadata.create(OrgLevel.class);
        level.setCode(String.format("LEVEL%d", ordinalNumber));
        level.setName(String.format("Level %d", ordinalNumber));
        level.setOrder(ordinalNumber);
        level.setActive(true);
        em.persist(level);

        OrgUnit orgUnit = metadata.create(OrgUnit.class);
        orgUnit.setCode(String.format("OU%d", ordinalNumber));
        orgUnit.setName(String.format("Org Unit %d", ordinalNumber));
        orgUnit.setTitle(String.format("Organizational Unit %d", ordinalNumber));
        orgUnit.setOrgLevel(level);
        if (parent != null) orgUnit.setParent(parent);
        em.persist(orgUnit);

        User managerUser = metadata.create(User.class);
        managerUser.setGroup(defaultGroup);
        managerUser.setLogin(String.format("muser%d", ordinalNumber));
        managerUser.setPassword(PASSWORD);
        em.persist(managerUser);

        Employee managerEmployee = metadata.create(Employee.class);
        managerEmployee.setCode(String.format("ME%d", ordinalNumber));
        managerEmployee.setUser(managerUser);
        managerEmployee.setFirstName(String.format("Manager%d", ordinalNumber));
        managerEmployee.setLastName(String.format("Managing OU%d", ordinalNumber));
        em.persist(managerEmployee);

        User supervisorUser = metadata.create(User.class);
        supervisorUser.setGroup(defaultGroup);
        supervisorUser.setLogin(String.format("suser%d", ordinalNumber));
        supervisorUser.setPassword(PASSWORD);
        em.persist(supervisorUser);

        Employee supervisorEmployee = metadata.create(Employee.class);
        supervisorEmployee.setCode(String.format("SE%d", ordinalNumber));
        supervisorEmployee.setUser(supervisorUser);
        supervisorEmployee.setFirstName(String.format("Supervisor%d", ordinalNumber));
        supervisorEmployee.setLastName(String.format("Supervising OU%d", ordinalNumber));
        if (parent!=null) supervisorEmployee.setOrgUnit(parent);
        em.persist(supervisorEmployee);

        User staffUser = metadata.create(User.class);
        staffUser.setGroup(defaultGroup);
        staffUser.setLogin(String.format("user%d", ordinalNumber));
        staffUser.setPassword(PASSWORD);
        em.persist(staffUser);

        Employee staffEmployee = metadata.create(Employee.class);
        staffEmployee.setCode(String.format("E%d", ordinalNumber));
        staffEmployee.setUser(staffUser);
        staffEmployee.setFirstName(String.format("Employee%d", ordinalNumber));
        staffEmployee.setLastName(String.format("Member of %d", ordinalNumber));
        em.persist(staffEmployee);

        orgUnit.setManager(managerEmployee);
        orgUnit.setSupervisor(supervisorEmployee);
        staffEmployee.setOrgUnit(orgUnit);
        managerEmployee.setOrgUnit(orgUnit);

        return orgUnit;
    }

    private void removeOrgUnitAndRelevantEmployees(EntityManager em) {
        Query query;

        query = em.createNativeQuery("TRUNCATE TABLE HRM_EMPLOYEE CASCADE");
        query.executeUpdate();

        query = em.createNativeQuery("Delete from SEC_USER e where e.login not in ('admin','anonymous')");
        query.executeUpdate();

        query = em.createNativeQuery("TRUNCATE TABLE HRM_ORG_UNIT_HIERARCHY CASCADE");
        query.executeUpdate();

        query = em.createNativeQuery("TRUNCATE TABLE HRM_ORG_UNIT CASCADE");
        query.executeUpdate();

        query = em.createNativeQuery("TRUNCATE TABLE HRM_ORG_LEVEL CASCADE");
        query.executeUpdate();
    }

    @Test
    public void getLineManager() {

        Hrm hrmBean = AppBeans.get(Hrm.class);

        Employee lineManager = hrmBean.getLineManager("user3");
        assertEquals("muser3", lineManager.getUserName());

        Employee lineManager2 = hrmBean.getLineManager("muser3");
        assertEquals("muser2", lineManager2.getUserName());
    }

    @Test
    public void getTopManager() {
        Hrm hrmBean = AppBeans.get(Hrm.class);

        Employee topManager = hrmBean.getTopManager();
        assertEquals("muser1", topManager.getUserName());
    }

    @Test
    public void getSupervisor() {
        Hrm hrmBean = AppBeans.get(Hrm.class);

        Employee supervisor2 = hrmBean.getSupervisor("LEVEL2", "user3");
        assertEquals("suser2", supervisor2.getUserName());

        Employee supervisor3 = hrmBean.getSupervisor("LEVEL3", "user3");
        assertEquals("suser3", supervisor3.getUserName());

        Employee supervisor4 = hrmBean.getSupervisor("LEVEL3", "user2");
        assertEquals(null, supervisor4);

        Employee supByOU = hrmBean.getSupervisorByOrgUnit("OU2");
        assertEquals("suser2", supByOU.getUserName());
    }

    @Test
    public void getManager() {
        Hrm hrmBean = AppBeans.get(Hrm.class);

        Employee manager2 = hrmBean.getManager("LEVEL2", "user3");
        assertEquals("muser2", manager2.getUserName());

        Employee manager3 = hrmBean.getManager("LEVEL3", "user3");
        assertEquals("muser3", manager3.getUserName());

        Employee manager4 = hrmBean.getManager("LEVEL3", "user2");
        assertEquals(null, manager4);

        Employee managerByOU = hrmBean.getManagerByOrgUnit("OU2");
        assertEquals("muser2", managerByOU.getUserName());
    }

    @Test
    public void getManagerByOrgUnit() {
        Hrm hrmBean = AppBeans.get(Hrm.class);

        Employee manager = hrmBean.getManagerByOrgUnit("OU1");
        assertEquals("muser1", manager.getUserName());

        manager = hrmBean.getManagerByOrgUnit("OU2");
        assertEquals("muser2", manager.getUserName());
    }

    @Test
    public void getSupervisorByOrgUnit() {
        Hrm hrmBean = AppBeans.get(Hrm.class);

        Employee manager = hrmBean.getSupervisorByOrgUnit("OU1");
        assertEquals("suser1", manager.getUserName());

        manager = hrmBean.getSupervisorByOrgUnit("OU2");
        assertEquals("suser2", manager.getUserName());
    }

}