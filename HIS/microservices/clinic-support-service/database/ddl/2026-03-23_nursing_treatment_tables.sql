prompt === Create CHJ.TREATMENT_RESULT ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'TREATMENT_RESULT';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.TREATMENT_RESULT (
                PROCEDURE_RESULT_ID varchar2(19) not null,
                ORDER_ITEM_ID       varchar2(30),
                STATUS              varchar2(20) default 'ACTIVE',
                PERFORMED_AT        char(18),
                PERFORMER_ID        char(18),
                DETAIL              varchar2(1000),
                constraint PK_TREATMENT_RESULT primary key (PROCEDURE_RESULT_ID)
            )
        ]';
    end if;
end;
/

prompt === Create CHJ.MEDICATION ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tables
     where owner = 'CHJ'
       and table_name = 'MEDICATION';

    if v_count = 0 then
        execute immediate q'[
            create table CHJ.MEDICATION (
                MEDICATION_ID   varchar2(19) not null,
                ORDER_ITEM_ID   varchar2(30),
                ADMINISTERED_AT char(18),
                DOSE_NUMBER     number(10,3),
                DOSE_UNIT       varchar2(20),
                NURSE_ID        varchar2(30),
                STATUS          varchar2(20) default 'ACTIVE',
                constraint PK_MEDICATION primary key (MEDICATION_ID)
            )
        ]';
    end if;
end;
/

prompt === Create indexes for CRUD lookup ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and table_name = 'TREATMENT_RESULT'
       and index_name = 'IDX_TREATMENT_RESULT_ORDER_ITEM';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_TREATMENT_RESULT_ORDER_ITEM on CHJ.TREATMENT_RESULT (ORDER_ITEM_ID)';
    end if;

    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and table_name = 'MEDICATION'
       and index_name = 'IDX_MEDICATION_ORDER_ITEM';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_MEDICATION_ORDER_ITEM on CHJ.MEDICATION (ORDER_ITEM_ID)';
    end if;
end;
/

prompt === Done ===
