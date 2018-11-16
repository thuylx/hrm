package com.company.hrm.core.bpm.impl;

import com.company.hrm.core.bpm.LeaveService;
import com.company.hrm.entity.LeaveRequest;
import com.haulmont.cuba.core.global.DataManager;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import vn.tki.erp.cambpm.core.ProcessApplication;

import javax.inject.Inject;

@Component(LeaveService.NAME)
public class LeaveImpl implements LeaveService {

    @Inject
    private ProcessApplication processApplication;
    @Inject
    private DataManager dataManager;

    @Override
    public void lock(DelegateExecution execution) {
        LeaveRequest leaveRequest = (LeaveRequest) processApplication.getEntity(execution);
        leaveRequest.setLocked(true);
        dataManager.commit(leaveRequest);
    }

    @Override
    public void unLock(DelegateExecution execution) {
        LeaveRequest leaveRequest = (LeaveRequest) processApplication.getEntity(execution);
        leaveRequest.setLocked(false);
        dataManager.commit(leaveRequest);
    }

    @Override
    public void updateStatus(DelegateExecution execution, String newStatus) {
        LeaveRequest leaveRequest = (LeaveRequest) processApplication.getEntity(execution);
        leaveRequest.setStatus(newStatus);
        dataManager.commit(leaveRequest);
    }
}
