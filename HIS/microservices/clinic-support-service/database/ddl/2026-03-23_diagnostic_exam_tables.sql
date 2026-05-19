prompt === Create CHJ.ENDOSCOPY_EXAM ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'ENDOSCOPY_EXAM';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.ENDOSCOPY_EXAM (
                ENDOSCOPY_EXAM_ID varchar2(30) not null,
                TEST_EXECUTION_ID varchar2(30),
                PROCEDURE_ROOM    varchar2(50),
                EQUIPMENT         varchar2(50),
                SEDATION_YN       char(1),
                OPERATION_ID      varchar2(30),
                PROCEDURE_AT      timestamp(6),
                STATUS            varchar2(20) default 'ACTIVE',
                CREATED_AT        timestamp(6) default systimestamp,
                UPDATED_AT        timestamp(6),
                constraint PK_ENDOSCOPY_EXAM primary key (ENDOSCOPY_EXAM_ID)
            )
        ]';
    end if;
end;
/

prompt === Create CHJ.ENDOSCOPY_EXAM_RESULT ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'ENDOSCOPY_EXAM_RESULT';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.ENDOSCOPY_EXAM_RESULT (
                ENDOSCOPY_RESULT_ID varchar2(30) not null,
                ENDOSCOPY_EXAM_ID   varchar2(30),
                RESULT_SUMMARY      varchar2(1000),
                BIOPSY_YN           char(1),
                CONFIRMED_AT        timestamp(6),
                READER_ID           varchar2(30),
                STATUS              varchar2(20) default 'ACTIVE',
                CREATED_AT          timestamp(6) default systimestamp,
                UPDATED_AT          timestamp(6),
                constraint PK_ENDOSCOPY_EXAM_RESULT primary key (ENDOSCOPY_RESULT_ID)
            )
        ]';
    end if;
end;
/

prompt === Create CHJ.PATHOLOGY_EXAM ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'PATHOLOGY_EXAM';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.PATHOLOGY_EXAM (
                PATHOLOGY_EXAM_ID  varchar2(30) not null,
                TEST_EXECUTION_ID  varchar2(30),
                TISSUE_STATUS      varchar2(20),
                COLLECTION_METHOD  varchar2(30),
                TISSUE_SITE        varchar2(50),
                TISSUE_TYPE        varchar2(50),
                COLLECTED_AT       timestamp(6),
                COLLECTED_BY_ID    varchar2(30),
                REEXAM_YN          char(1),
                STATUS             varchar2(20) default 'ACTIVE',
                CREATED_AT         timestamp(6) default systimestamp,
                UPDATED_AT         timestamp(6),
                constraint PK_PATHOLOGY_EXAM primary key (PATHOLOGY_EXAM_ID)
            )
        ]';
    end if;
end;
/

prompt === Create CHJ.PATHOLOGY_EXAM_RESULT ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'PATHOLOGY_EXAM_RESULT';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.PATHOLOGY_EXAM_RESULT (
                PATHOLOGY_EXAM_RESULT_ID varchar2(30) not null,
                PATHOLOGY_EXAM_ID        varchar2(30),
                RESULT_SUMMARY           varchar2(1000),
                JUDGED_AT                timestamp(6),
                CONFIRMED_AT             timestamp(6),
                READER_ID                varchar2(30),
                DIAGNOSIS_NAME           varchar2(100),
                STATUS                   varchar2(20) default 'ACTIVE',
                CREATED_AT               timestamp(6) default systimestamp,
                UPDATED_AT               timestamp(6),
                constraint PK_PATHOLOGY_EXAM_RESULT primary key (PATHOLOGY_EXAM_RESULT_ID)
            )
        ]';
    end if;
end;
/

prompt === Create CHJ.PHYSIOLOGICAL_EXAM ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'PHYSIOLOGICAL_EXAM';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.PHYSIOLOGICAL_EXAM (
                PHYSIOLOGICAL_EXAM_ID varchar2(30) not null,
                TEST_EXECUTION_ID     varchar2(30),
                EXAM_EQUIPMENT_ID     varchar2(30),
                RAW_DATA              varchar2(1000),
                REPORT_DOC_ID         varchar2(30),
                STATUS                varchar2(20) default 'ACTIVE',
                CREATED_AT            timestamp(6) default systimestamp,
                UPDATED_AT            timestamp(6),
                constraint PK_PHYSIOLOGICAL_EXAM primary key (PHYSIOLOGICAL_EXAM_ID)
            )
        ]';
    end if;
end;
/

prompt === Create CHJ.PHYSIOLOGICAL_EXAM_RESULT ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'PHYSIOLOGICAL_EXAM_RESULT';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.PHYSIOLOGICAL_EXAM_RESULT (
                PHYSIOLOGICAL_EXAM_RESULT_ID varchar2(30) not null,
                PHYSIOLOGICAL_EXAM_ID        varchar2(30),
                RESULT_SUMMARY               varchar2(200),
                REPORT                       varchar2(1000),
                MEASURED_ITEM_CODE           varchar2(30),
                STATUS                       varchar2(20) default 'ACTIVE',
                CREATED_AT                   timestamp(6) default systimestamp,
                UPDATED_AT                   timestamp(6),
                constraint PK_PHYSIOLOGICAL_EXAM_RESULT primary key (PHYSIOLOGICAL_EXAM_RESULT_ID)
            )
        ]';
    end if;
end;
/

prompt === Create indexes for list/detail lookup ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and index_name = 'IDX_ENDOSCOPY_EXAM_EXEC';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_ENDOSCOPY_EXAM_EXEC on CHJ.ENDOSCOPY_EXAM (TEST_EXECUTION_ID)';
    end if;

    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and index_name = 'IDX_ENDOSCOPY_RES_EXAM';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_ENDOSCOPY_RES_EXAM on CHJ.ENDOSCOPY_EXAM_RESULT (ENDOSCOPY_EXAM_ID)';
    end if;

    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and index_name = 'IDX_PATHOLOGY_EXAM_EXEC';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_PATHOLOGY_EXAM_EXEC on CHJ.PATHOLOGY_EXAM (TEST_EXECUTION_ID)';
    end if;

    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and index_name = 'IDX_PATHOLOGY_RES_EXAM';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_PATHOLOGY_RES_EXAM on CHJ.PATHOLOGY_EXAM_RESULT (PATHOLOGY_EXAM_ID)';
    end if;

    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and index_name = 'IDX_PHYSIO_EXAM_EXEC';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_PHYSIO_EXAM_EXEC on CHJ.PHYSIOLOGICAL_EXAM (TEST_EXECUTION_ID)';
    end if;

    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and index_name = 'IDX_PHYSIO_RES_EXAM';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_PHYSIO_RES_EXAM on CHJ.PHYSIOLOGICAL_EXAM_RESULT (PHYSIOLOGICAL_EXAM_ID)';
    end if;
end;
/

prompt === Done ===
