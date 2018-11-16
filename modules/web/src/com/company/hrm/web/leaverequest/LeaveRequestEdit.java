package com.company.hrm.web.leaverequest;

import com.company.hrm.entity.LeaveRequest;
import com.haulmont.cuba.gui.WindowParams;
import com.haulmont.cuba.gui.components.AbstractEditor;
import com.haulmont.cuba.gui.components.FieldGroup;
import de.balvi.cuba.declarativecontrollers.web.editor.AnnotatableAbstractEditor;
import vn.tki.erp.cambpm.annotation.EnableBpmFrame;

import javax.inject.Inject;
import java.util.Map;

@EnableBpmFrame(taskRefreshActionContainerId = "processPanel",
        runningProcessActionContainerId = "processPanel",
        processDefinitionActionContainerId = "processPanel",
        taskActionContainerId = "taskPanel")
public class LeaveRequestEdit extends AnnotatableAbstractEditor<LeaveRequest> {
    @Inject
    private FieldGroup fieldGroup;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        LeaveRequest leaveRequest = (LeaveRequest) WindowParams.ITEM.getEntity(params);
        if (leaveRequest.getLocked() == null || !leaveRequest.getLocked()) {
            fieldGroup.setEditable(true);
        } else {
            fieldGroup.setEditable(false);
        }
    }
}