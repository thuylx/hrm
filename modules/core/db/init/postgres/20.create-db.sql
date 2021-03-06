-- begin HRM_ORG_UNIT
alter table HRM_ORG_UNIT add constraint FK_HRM_ORG_UNIT_ON_ORG_LEVEL foreign key (ORG_LEVEL_ID) references HRM_ORG_LEVEL(ID)^
alter table HRM_ORG_UNIT add constraint FK_HRM_ORG_UNIT_ON_PARENT foreign key (PARENT_ID) references HRM_ORG_UNIT(ID)^
alter table HRM_ORG_UNIT add constraint FK_HRM_ORG_UNIT_ON_SUPERVISOR foreign key (SUPERVISOR_ID) references HRM_EMPLOYEE(ID)^
alter table HRM_ORG_UNIT add constraint FK_HRM_ORG_UNIT_ON_MANAGER foreign key (MANAGER_ID) references HRM_EMPLOYEE(ID)^
create unique index IDX_HRM_ORG_UNIT_UK_CODE on HRM_ORG_UNIT (CODE) where DELETE_TS is null ^
create index IDX_HRM_ORG_UNIT_ON_ORG_LEVEL on HRM_ORG_UNIT (ORG_LEVEL_ID)^
create index IDX_HRM_ORG_UNIT_ON_PARENT on HRM_ORG_UNIT (PARENT_ID)^
create index IDX_HRM_ORG_UNIT_ON_SUPERVISOR on HRM_ORG_UNIT (SUPERVISOR_ID)^
create index IDX_HRM_ORG_UNIT_ON_MANAGER on HRM_ORG_UNIT (MANAGER_ID)^
-- end HRM_ORG_UNIT
-- begin HRM_EMPLOYEE
alter table HRM_EMPLOYEE add constraint FK_HRM_EMPLOYEE_ON_USER foreign key (USER_ID) references SEC_USER(ID)^
alter table HRM_EMPLOYEE add constraint FK_HRM_EMPLOYEE_ON_ORG_UNIT foreign key (ORG_UNIT_ID) references HRM_ORG_UNIT(ID)^
create unique index IDX_HRM_EMPLOYEE_UK_CODE on HRM_EMPLOYEE (CODE) where DELETE_TS is null ^
create index IDX_HRM_EMPLOYEE_ON_USER on HRM_EMPLOYEE (USER_ID)^
create index IDX_HRM_EMPLOYEE_ON_ORG_UNIT on HRM_EMPLOYEE (ORG_UNIT_ID)^
-- end HRM_EMPLOYEE
-- begin HRM_ORG_UNIT_HIERARCHY
alter table HRM_ORG_UNIT_HIERARCHY add constraint FK_HRM_ORG_UNIT_HIERARCHY_ON_ORG_UNIT foreign key (ORG_UNIT_ID) references HRM_ORG_UNIT(ID)^
alter table HRM_ORG_UNIT_HIERARCHY add constraint FK_HRM_ORG_UNIT_HIERARCHY_ON_PARENT foreign key (PARENT_ID) references HRM_ORG_UNIT(ID)^
create index IDX_HRM_ORG_UNIT_HIERARCHY_ON_ORG_UNIT on HRM_ORG_UNIT_HIERARCHY (ORG_UNIT_ID)^
create index IDX_HRM_ORG_UNIT_HIERARCHY_ON_PARENT on HRM_ORG_UNIT_HIERARCHY (PARENT_ID)^
-- end HRM_ORG_UNIT_HIERARCHY
-- begin HRM_ORG_LEVEL
create unique index IDX_HRM_ORG_LEVEL_UK_CODE on HRM_ORG_LEVEL (CODE) where DELETE_TS is null ^
-- end HRM_ORG_LEVEL
-- begin HRM_LEAVE_REQUEST
alter table HRM_LEAVE_REQUEST add constraint FK_HRM_LEAVE_REQUEST_ON_REQUESTER foreign key (REQUESTER_ID) references HRM_EMPLOYEE(ID)^
create index IDX_HRM_LEAVE_REQUEST_ON_REQUESTER on HRM_LEAVE_REQUEST (REQUESTER_ID)^
-- end HRM_LEAVE_REQUEST
