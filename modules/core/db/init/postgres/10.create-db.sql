-- begin HRM_ORG_UNIT
create table HRM_ORG_UNIT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(100) not null,
    NAME varchar(255) not null,
    ORG_LEVEL_ID uuid,
    TITLE varchar(255) not null,
    DESCRIPTION varchar(255),
    PARENT_ID uuid,
    SUPERVISOR_ID uuid,
    MANAGER_ID uuid,
    ORDER_ integer,
    --
    primary key (ID)
)^
-- end HRM_ORG_UNIT
-- begin HRM_EMPLOYEE
create table HRM_EMPLOYEE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(100) not null,
    FIRST_NAME varchar(255) not null,
    LAST_NAME varchar(255),
    USER_ID uuid,
    ORG_UNIT_ID uuid,
    --
    primary key (ID)
)^
-- end HRM_EMPLOYEE
-- begin HRM_ORG_UNIT_HIERARCHY
create table HRM_ORG_UNIT_HIERARCHY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ORG_UNIT_ID uuid not null,
    PARENT_ID uuid not null,
    HIERARCHY_LEVEL integer not null,
    --
    primary key (ID)
)^
-- end HRM_ORG_UNIT_HIERARCHY
-- begin HRM_ORG_LEVEL
create table HRM_ORG_LEVEL (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(255) not null,
    NAME varchar(255) not null,
    ORDER_ integer,
    ACTIVE boolean,
    --
    primary key (ID)
)^
-- end HRM_ORG_LEVEL
-- begin HRM_LEAVE_REQUEST
create table HRM_LEAVE_REQUEST (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    REQUESTER_ID uuid,
    REASON text,
    BEGIN_DATE date,
    END_DATE date,
    STATUS varchar(255),
    IS_LOCK boolean,
    --
    primary key (ID)
)^
-- end HRM_LEAVE_REQUEST
