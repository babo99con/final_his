-- Specimen -> specimen exam migration / result table creation
-- Run as a user with privileges to alter CHJ schema objects.

prompt === Normalize CHJ.SPECIMEN soft delete status ===

update chj.specimen
   set status = case upper(trim(status))
                  when 'Y' then 'ACTIVE'
                  when 'N' then 'INACTIVE'
                  else status
                end
 where upper(trim(status)) in ('Y', 'N');

commit;

prompt === Expand CHJ.SPECIMEN for specimen exam domain ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and column_name = 'TEST_EXECUTION_ID';

    if v_count = 0 then
        execute immediate 'alter table CHJ.SPECIMEN add (TEST_EXECUTION_ID varchar2(30))';
    end if;

    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and column_name = 'SPECIMEN_STATUS';

    if v_count = 0 then
        execute immediate q'[alter table CHJ.SPECIMEN add (SPECIMEN_STATUS varchar2(20))]';
    end if;

    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and column_name = 'COLLECTION_SITE';

    if v_count = 0 then
        execute immediate 'alter table CHJ.SPECIMEN add (COLLECTION_SITE varchar2(50))';
    end if;

    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and column_name = 'RECOLLECTION_YN';

    if v_count = 0 then
        execute immediate q'[alter table CHJ.SPECIMEN add (RECOLLECTION_YN char(1) default 'N')]';
    end if;
end;
/

prompt === Normalize timestamps / defaults ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and column_name = 'COLLECTED_AT'
       and data_type = 'DATE';

    if v_count = 1 then
        execute immediate 'alter table CHJ.SPECIMEN modify (COLLECTED_AT timestamp(6))';
    end if;

    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and column_name = 'CREATED_AT'
       and data_type = 'DATE';

    if v_count = 1 then
        execute immediate 'alter table CHJ.SPECIMEN modify (CREATED_AT timestamp(6))';
    end if;

    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and column_name = 'UPDATED_AT'
       and data_type = 'DATE';

    if v_count = 1 then
        execute immediate 'alter table CHJ.SPECIMEN modify (UPDATED_AT timestamp(6))';
    end if;
end;
/

update chj.specimen
   set created_at = systimestamp
 where created_at is null;

update chj.specimen
   set updated_at = created_at
 where updated_at is null
   and created_at is not null;

update chj.specimen
   set specimen_status = 'COLLECTED'
 where specimen_status is null;

update chj.specimen
   set recollection_yn = 'N'
 where recollection_yn is null;

commit;

alter table chj.specimen modify (
    status varchar2(20) default 'ACTIVE' not null,
    created_at timestamp(6) default systimestamp not null,
    updated_at timestamp(6),
    visit_id varchar2(30) not null,
    created_by varchar2(30) not null,
    specimen_status varchar2(20) not null,
    recollection_yn char(1) default 'N' not null
);

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_constraints
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and constraint_name = 'CK_CHJ_SPECIMEN_STATUS';

    if v_count = 0 then
        execute immediate q'[alter table CHJ.SPECIMEN add constraint CK_CHJ_SPECIMEN_STATUS check (STATUS in ('ACTIVE', 'INACTIVE'))]';
    end if;

    select count(*)
      into v_count
      from all_constraints
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and constraint_name = 'CK_CHJ_SPECIMEN_WORK_STATUS';

    if v_count = 0 then
        execute immediate q'[alter table CHJ.SPECIMEN add constraint CK_CHJ_SPECIMEN_WORK_STATUS check (SPECIMEN_STATUS in ('COLLECTED', 'RECEIVED', 'REJECTED', 'COMPLETED'))]';
    end if;

    select count(*)
      into v_count
      from all_constraints
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and constraint_name = 'CK_CHJ_SPECIMEN_RECOLLECTION';

    if v_count = 0 then
        execute immediate q'[alter table CHJ.SPECIMEN add constraint CK_CHJ_SPECIMEN_RECOLLECTION check (RECOLLECTION_YN in ('Y', 'N'))]';
    end if;
end;
/

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and index_name = 'IDX_SPECIMEN_VISIT_STATUS';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_SPECIMEN_VISIT_STATUS on CHJ.SPECIMEN (VISIT_ID, STATUS)';
    end if;

    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and table_name = 'SPECIMEN'
       and index_name = 'IDX_SPECIMEN_EXECUTION_ID';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_SPECIMEN_EXECUTION_ID on CHJ.SPECIMEN (TEST_EXECUTION_ID)';
    end if;
end;
/

prompt === Create CHJ.SPECIMEN_TEST_RESULT ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_TEST_RESULT';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.SPECIMEN_TEST_RESULT (
                SPECIMEN_TEST_RESULT_ID varchar2(30) not null,
                SPECIMEN_ID             varchar2(30) not null,
                RESULT_ITEM_CODE        varchar2(30) not null,
                RESULT_ITEM_NAME        varchar2(100),
                RESULT_VALUE            varchar2(100),
                UNIT                    varchar2(20),
                REFERENCE_RANGE         varchar2(100),
                JUDGEMENT               varchar2(20),
                RESULT_STATUS           varchar2(20) default 'DRAFT' not null,
                STATUS                  varchar2(20) default 'ACTIVE' not null,
                CREATED_AT              timestamp(6) default systimestamp not null,
                UPDATED_AT              timestamp(6),
                CONFIRMED_AT            timestamp(6),
                ENTERED_BY              varchar2(30),
                CONFIRMED_BY            varchar2(30),
                constraint PK_SPECIMEN_TEST_RESULT primary key (SPECIMEN_TEST_RESULT_ID),
                constraint FK_SPEC_TEST_RESULT_SPEC foreign key (SPECIMEN_ID)
                    references CHJ.SPECIMEN (SPECIMEN_ID),
                constraint CK_SPEC_TEST_RESULT_STATUS check (STATUS in ('ACTIVE', 'INACTIVE')),
                constraint CK_SPEC_TEST_RESULT_WORK check (RESULT_STATUS in ('DRAFT', 'CONFIRMED', 'CORRECTED'))
            )
        ]';
    end if;
end;
/

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_TEST_RESULT'
       and index_name = 'IDX_SPEC_TEST_RESULT_SPEC_ID';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_SPEC_TEST_RESULT_SPEC_ID on CHJ.SPECIMEN_TEST_RESULT (SPECIMEN_ID, STATUS)';
    end if;
end;
/

prompt === Done ===
