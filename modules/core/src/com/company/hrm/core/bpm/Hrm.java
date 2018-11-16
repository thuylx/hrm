package com.company.hrm.core.bpm;

import com.company.hrm.entity.Employee;
import org.springframework.stereotype.Component;
import vn.tki.erp.cambpm.core.BpmExposedBean;

@Component(Hrm.NAME)
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
