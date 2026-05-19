prompt === Add TEST_EXECUTION_ID to CHJ.IMAGING_EXAM ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM'
       and column_name = 'TEST_EXECUTION_ID';

    if v_count = 0 then
        execute immediate 'alter table CHJ.IMAGING_EXAM add (TEST_EXECUTION_ID varchar2(30))';
    end if;
end;
/

prompt === Create index for CHJ.IMAGING_EXAM(TEST_EXECUTION_ID) ===

declare
    v_count number;
begin
    select count(*)
      into v_count
      from all_indexes
     where owner = 'CHJ'
       and index_name = 'IDX_IMAGING_EXAM_EXEC';

    if v_count = 0 then
        execute immediate 'create index CHJ.IDX_IMAGING_EXAM_EXEC on CHJ.IMAGING_EXAM (TEST_EXECUTION_ID)';
    end if;
end;
/

prompt === Done ===
