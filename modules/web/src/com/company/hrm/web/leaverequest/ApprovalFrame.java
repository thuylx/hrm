package com.company.hrm.web.leaverequest;

import com.company.hrm.entity.LeaveRequest;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.data.Datasource;
import vn.tki.erp.cambpm.annotation.ProcessVariable;
import vn.tki.erp.cambpm.helper.EntityHelper;
import vn.tki.erp.cambpm.web.bpmtask.BpmTaskAbstractFrame;

import javax.inject.Inject;
import java.util.Map;

public class ApprovalFrame extends BpmTaskAbstractFrame {
    @Inject
    private Datasource<LeaveRequest> leaveRequestDs;
    @Inject
    private EntityHelper entityHelper;

    @ProcessVariable(name = "isApproved", loadValue = false)
    @Inject
    private CheckBox approvalCheckBox;

    @Override
    public void init(Map<String, Object> params) {
        leaveRequestDs.setItem((LeaveRequest) entityHelper.getEntity(getBpmTask(), leaveRequestDs.getView()));
        super.init(params);
    }

    @Override
    public void onCompleteTask() throws Exception {

    }
}