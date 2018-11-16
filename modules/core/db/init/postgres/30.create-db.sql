--init predefined ORG LEVEL
insert into HRM_ORG_LEVEL 
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, CODE, NAME, ORDER_, ACTIVE) 
values ('a14b84fe-305a-bfa9-f3b4-585c0083edd3', 1, '2018-11-07 17:51:57', 'admin', '2018-11-07 17:51:57', null, null, null, 'COM', 'Company', 1, true);
insert into HRM_ORG_LEVEL 
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, CODE, NAME, ORDER_, ACTIVE) 
values ('e30fc520-8ceb-643d-24de-625ae0691b11', 1, '2018-11-07 17:52:09', 'admin', '2018-11-07 17:52:09', null, null, null, 'FUNC', 'Function', 2, true);
insert into HRM_ORG_LEVEL 
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, CODE, NAME, ORDER_, ACTIVE) 
values ('2ec4d845-83fe-c109-5eb4-d0ecf65f3238', 2, '2018-11-07 17:52:18', 'admin', '2018-11-07 17:52:46', 'admin', null, null, 'DEPT', 'Department', 3, true);
insert into HRM_ORG_LEVEL 
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, CODE, NAME, ORDER_, ACTIVE) 
values ('e9da95f7-3866-de71-09b8-c312675998ed', 2, '2018-11-07 17:52:35', 'admin', '2018-11-07 17:52:46', 'admin', null, null, 'GROUP', 'Wowrk group', 4, true);
insert into HRM_ORG_LEVEL 
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, CODE, NAME, ORDER_, ACTIVE) 
values ('0abef405-577c-9bcb-dcc9-67806179d0fc', 2, '2018-11-08 16:44:15', 'admin', '2018-11-08 16:44:23', 'admin', null, null, 'TEAM', 'Working team', 5, true);

-- init OU
insert into HRM_ORG_UNIT 
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, CODE, NAME, ORG_LEVEL_ID, TITLE, DESCRIPTION, PARENT_ID, SUPERVISOR_ID, MANAGER_ID, ORDER_) 
values ('3fd01002-ae82-eff5-82c5-e3d9dc74c4df', 4, '2018-11-08 23:45:11', 'admin', '2018-11-08 23:49:30', 'admin', null, null, 'OU01', 'Company', 'a14b84fe-305a-bfa9-f3b4-585c0083edd3', 'Company', null, null, null, null, 1);
insert into HRM_ORG_UNIT 
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, CODE, NAME, ORG_LEVEL_ID, TITLE, DESCRIPTION, PARENT_ID, SUPERVISOR_ID, MANAGER_ID, ORDER_) 
values ('c38ed5c8-3190-b7f5-497f-db6754da18c2', 4, '2018-11-08 23:47:30', 'admin', '2018-11-08 23:50:08', 'admin', null, null, 'OU011', 'Technical', 'e30fc520-8ceb-643d-24de-625ae0691b11', 'Technical', null, '3fd01002-ae82-eff5-82c5-e3d9dc74c4df', null, null, 1);
insert into HRM_ORG_UNIT 
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, CODE, NAME, ORG_LEVEL_ID, TITLE, DESCRIPTION, PARENT_ID, SUPERVISOR_ID, MANAGER_ID, ORDER_) 
values ('9c536fe2-8fb4-9566-88f0-d21995e83510', 3, '2018-11-08 23:46:27', 'admin', '2018-11-08 23:50:03', 'admin', null, null, 'OU012', 'Marketing', 'e30fc520-8ceb-643d-24de-625ae0691b11', 'Marketing', null, '3fd01002-ae82-eff5-82c5-e3d9dc74c4df', null, null, 2);
insert into HRM_ORG_UNIT 
(ID, VERSION, CREATE_TS, CREATED_BY, UPDATE_TS, UPDATED_BY, DELETE_TS, DELETED_BY, CODE, NAME, ORG_LEVEL_ID, TITLE, DESCRIPTION, PARENT_ID, SUPERVISOR_ID, MANAGER_ID, ORDER_) 
values ('00e888b3-6f6f-caff-b774-c0193bd3c75c', 2, '2018-11-08 23:48:01', 'admin', '2018-11-08 23:48:59', 'admin', null, null, 'OU0121', 'IT department', '2ec4d845-83fe-c109-5eb4-d0ecf65f3238', 'Technical/IT department', null, 'c38ed5c8-3190-b7f5-497f-db6754da18c2', null, null, 1);
