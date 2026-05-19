prompt === Create CHJ.SPECIMEN_EXAM ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_EXAM';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.SPECIMEN_EXAM (
                SPECIMEN_EXAM_ID varchar2(30) not null,
                TEST_EXECUTION_ID varchar2(30),
                SPECIMEN_TYPE     varchar2(30),
                SPECIMEN_STATUS   varchar2(20),
                COLLECTED_AT      timestamp(6),
                COLLECTED_BY_ID   varchar2(30),
                COLLECTION_SITE   varchar2(50),
                RECOLLECTION_YN   char(1) default 'N',
                STATUS            varchar2(20) default 'ACTIVE',
                CREATED_AT        timestamp(6) default systimestamp,
                UPDATED_AT        timestamp(6),
                constraint PK_SPECIMEN_EXAM primary key (SPECIMEN_EXAM_ID)
            )
        ]';
    end if;
end;
/

prompt === Create CHJ.SPECIMEN_EXAM_RESULT ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_EXAM_RESULT';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.SPECIMEN_EXAM_RESULT (
                SPECIMEN_EXAM_RESULT_ID varchar2(30) not null,
                SPECIMEN_EXAM_ID        varchar2(30),
                RESULT_ITEM_CODE        varchar2(30),
                RESULT_SUMMARY          varchar2(200),
                UNIT                    varchar2(20),
                REFERENCE_RANGE         varchar2(200),
                JUDGEMENT               varchar2(20),
                STATUS                  varchar2(20) default 'ACTIVE',
                CREATED_AT              timestamp(6) default systimestamp,
                UPDATED_AT              timestamp(6),
                constraint PK_SPECIMEN_EXAM_RESULT primary key (SPECIMEN_EXAM_RESULT_ID)
            )
        ]';
    end if;
end;
/

prompt === Create indexes for lookup ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and index_name = 'IDX_SPECIMEN_EXAM_EXEC';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_SPECIMEN_EXAM_EXEC on CHJ.SPECIMEN_EXAM (TEST_EXECUTION_ID)';
    end if;

    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and index_name = 'IDX_SPECIMEN_EXAM_RESULT_EXAM';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_SPECIMEN_EXAM_RESULT_EXAM on CHJ.SPECIMEN_EXAM_RESULT (SPECIMEN_EXAM_ID)';
    end if;
end;
/

prompt === Done ===
