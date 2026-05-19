prompt === Rename diagnostic result summary columns in CHJ schema ===

declare
    v_old_count number;
    v_new_count number;
begin
    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM_RESULT'
       and column_name = 'READING_SUMMARY';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM_RESULT'
       and column_name = 'RESULT_SUMMARY';

    if v_old_count > 0 and v_new_count = 0 then
        execute immediate 'alter table CHJ.IMAGING_EXAM_RESULT rename column READING_SUMMARY to RESULT_SUMMARY';
    end if;
end;
/

declare
    v_old_count number;
    v_new_count number;
begin
    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_EXAM_RESULT'
       and column_name = 'RESULT_VALUE';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_EXAM_RESULT'
       and column_name = 'RESULT_SUMMARY';

    if v_old_count > 0 and v_new_count = 0 then
        execute immediate 'alter table CHJ.SPECIMEN_EXAM_RESULT rename column RESULT_VALUE to RESULT_SUMMARY';
    end if;
end;
/

declare
    v_old_count number;
    v_new_count number;
begin
    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'ENDOSCOPY_EXAM_RESULT'
       and column_name = 'FINDING';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'ENDOSCOPY_EXAM_RESULT'
       and column_name = 'RESULT_SUMMARY';

    if v_old_count > 0 and v_new_count = 0 then
        execute immediate 'alter table CHJ.ENDOSCOPY_EXAM_RESULT rename column FINDING to RESULT_SUMMARY';
    end if;
end;
/

declare
    v_old_count number;
    v_new_count number;
begin
    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PHYSIOLOGICAL_EXAM_RESULT'
       and column_name = 'RESULT_VALUE';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PHYSIOLOGICAL_EXAM_RESULT'
       and column_name = 'RESULT_SUMMARY';

    if v_old_count > 0 and v_new_count = 0 then
        execute immediate 'alter table CHJ.PHYSIOLOGICAL_EXAM_RESULT rename column RESULT_VALUE to RESULT_SUMMARY';
    end if;
end;
/

prompt === Done ===
