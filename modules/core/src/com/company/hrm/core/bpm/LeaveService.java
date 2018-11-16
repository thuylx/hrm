package com.company.hrm.core.bpm;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;
import vn.tki.erp.cambpm.core.BpmExposedBean;

@Component(LeaveService.NAME)
public interface LeaveService extends BpmExposedBean {
    String NAME = "leaveSrv";

    void lock(DelegateExecution execution);
    void unLock(DelegateExecution execution);
    void updateStatus(DelegateExecution execution, String newStatus);
}
