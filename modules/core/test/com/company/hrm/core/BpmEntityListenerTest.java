package com.company.hrm.core;

import com.company.hrm.HrmTestContainer;
import com.company.hrm.entity.Employee;
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

import static org.junit.Assert.assertEquals;

public class BpmEntityListenerTest {
    @ClassRule
    public static HrmTestContainer cont = HrmTestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;
    private DataManager dataManager;

    @Before
    public void setUp() throws Exception {
        metadata = cont.metadata();
        persistence = cont.persistence();
        dataManager = AppBeans.get(DataManager.class);
    }

    @After
    public void tearDown() throws Exception {
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            Query query = em.createNativeQuery("TRUNCATE TABLE HRM_EMPLOYEE CASCADE");

            query.executeUpdate();

            query = em.createNativeQuery("Delete from SEC_USER e where e.login not in ('admin','anonymous')");
            query.executeUpdate();

            query = em.createNativeQuery("TRUNCATE TABLE HRM_ORG_UNIT_HIERARCHY CASCADE");
            query.executeUpdate();

            query = em.createNativeQuery("TRUNCATE TABLE HRM_ORG_UNIT CASCADE");
            query.executeUpdate();

            query = em.createNativeQuery("TRUNCATE TABLE HRM_ORG_LEVEL CASCADE");
            query.executeUpdate();

            tx.commit();
        }
    }

    @Test
    public void testOrgHierarchy() {
        OrgUnit orgUnit1;
        OrgUnit orgUnit2;
        OrgUnit orgUnit3;

        orgUnit1 = metadata.create(OrgUnit.class);
        orgUnit1.setCode("OU1");
        orgUnit1.setName("Org Unit 1");
        orgUnit1.setTitle("Organizational Unit 1");
        dataManager.commit(orgUnit1);

        orgUnit2 = metadata.create(OrgUnit.class);
        orgUnit2.setCode("OU2");
        orgUnit2.setName("Org Unit 2");
        orgUnit2.setTitle("Organizational Unit 2");
        orgUnit2.setParent(orgUnit1);
        dataManager.commit(orgUnit2);

        orgUnit3 = metadata.create(OrgUnit.class);
        orgUnit3.setCode("OU3");
        orgUnit3.setName("Org Unit 3");
        orgUnit3.setTitle("Organizational Unit 3");
        orgUnit3.setParent(orgUnit2);
        dataManager.commit(orgUnit3);


        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            orgUnit1 = em.find(OrgUnit.class, orgUnit1.getId());
            orgUnit2 = em.find(OrgUnit.class, orgUnit2.getId());
            orgUnit3 = em.find(OrgUnit.class, orgUnit3.getId());

            assertEquals(0, orgUnit1.getUpHierarchyList().size());
            assertEquals(2, orgUnit1.getDownHierarchyList().size());


            assertEquals(1, orgUnit2.getUpHierarchyList().size());
            assertEquals(orgUnit1.getId(), orgUnit2.getUpHierarchyList().get(0).getParent().getId());
            assertEquals(1, orgUnit2.getDownHierarchyList().size());
            assertEquals(orgUnit3.getId(), orgUnit2.getDownHierarchyList().get(0).getOrgUnit().getId());

            assertEquals(0, orgUnit3.getDownHierarchyList().size());
            assertEquals(2, orgUnit3.getUpHierarchyList().size());
            assertEquals(orgUnit1.getId(), orgUnit3.getUpHierarchyList().get(0).getParent().getId());
            assertEquals(orgUnit2.getId(), orgUnit3.getUpHierarchyList().get(1).getParent().getId());


            orgUnit3.setParent(orgUnit1);
            orgUnit2.setParent(orgUnit3);
            tx.commit();
        }

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            orgUnit1 = em.find(OrgUnit.class, orgUnit1.getId());
            orgUnit2 = em.find(OrgUnit.class, orgUnit2.getId());
            orgUnit3 = em.find(OrgUnit.class, orgUnit3.getId());

            assertEquals(0, orgUnit1.getUpHierarchyList().size());
            assertEquals(2, orgUnit1.getDownHierarchyList().size());


            assertEquals(1, orgUnit3.getUpHierarchyList().size());
            assertEquals(orgUnit1.getId(), orgUnit3.getUpHierarchyList().get(0).getParent().getId());
            assertEquals(1, orgUnit3.getDownHierarchyList().size());
            assertEquals(orgUnit2.getId(), orgUnit3.getDownHierarchyList().get(0).getOrgUnit().getId());

            assertEquals(0, orgUnit2.getDownHierarchyList().size());
            assertEquals(2, orgUnit2.getUpHierarchyList().size());
            assertEquals(orgUnit1.getId(), orgUnit2.getUpHierarchyList().get(0).getParent().getId());
            assertEquals(orgUnit3.getId(), orgUnit2.getUpHierarchyList().get(1).getParent().getId());
        }
    }

    @Test
    public void testEmployeeUser() {
        User user;
        Employee employee;

        Group defaultGroup = dataManager.load(Group.class).one();

        user = metadata.create(User.class);
        user.setGroup(defaultGroup);
        user.setLogin("testUser");
        user.setPassword("123456");
        dataManager.commit(user);

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            user = em.find(User.class, user.getId());
            employee = metadata.create(Employee.class);
            employee.setCode("Test");
            employee.setFirstName("FirstName");
            employee.setLastName("LastName");
            employee.setUser(user);

            em.persist(employee);
            tx.commit();
        }

        assertEquals("FirstName", user.getFirstName());
        assertEquals("LastName", user.getLastName());

        employee = dataManager.load(Employee.class).id(employee.getId()).one();
        employee.setFirstName("AnotherFirstName");
        employee.setLastName("AnotherLastName");
        dataManager.commit(employee);

        user = dataManager.load(User.class).id(user.getId()).one();
        assertEquals("AnotherFirstName", user.getFirstName());
        assertEquals("AnotherLastName", user.getLastName());

        User user2 = metadata.create(User.class);
        user2.setGroup(defaultGroup);
        user2.setLogin("testUser2");
        user2.setPassword("123456");
        dataManager.commit(user2);

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            employee = em.find(Employee.class, employee.getId());
            user2 = em.find(User.class, user2.getId());
            employee.setUser(user2);
            tx.commit();
        }

        user2 = dataManager.load(User.class).id(user2.getId()).one();
        assertEquals("AnotherFirstName", user2.getFirstName());
        assertEquals("AnotherLastName", user2.getLastName());
    }
}