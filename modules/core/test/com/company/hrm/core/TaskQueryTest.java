package com.company.hrm.core;

import com.company.hrm.HrmTestContainer;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.Metadata;
import org.camunda.bpm.engine.ProcessEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import vn.tki.erp.cambpm.core.impl.CamPlatformImpl;
import vn.tki.erp.cambpm.entity.BpmTask;
import vn.tki.erp.cambpm.query.BpmTaskQuery;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TaskQueryTest {
    @ClassRule
    public static HrmTestContainer cont = HrmTestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;
    private DataManager dataManager;
    private ProcessEngine processEngine;

    @Before
    public void setUp() throws Exception {
        metadata = cont.metadata();
        persistence = cont.persistence();
        dataManager = AppBeans.get(DataManager.class);
        processEngine = AppBeans.get(CamPlatformImpl.class).getProcessEngine();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void queryTasks() {
        BpmTaskQuery taskQuery = new BpmTaskQuery();

        List<BpmTask> bpmTasks = taskQuery
                .processInstanceBusinessKey("hrm$LeaveRequest-59e39897-fcdb-fcaa-1246-c356a366445a")
                .or()
                .taskAssignee("admin")
                .taskCandidateGroupIn(Arrays.asList("Administrators"))
                .includeAssignedTasks()
                .endOr()
                .orderByTaskCreateTime()
                .asc()
                .list();
        assertEquals(1, bpmTasks.size());
    }
}