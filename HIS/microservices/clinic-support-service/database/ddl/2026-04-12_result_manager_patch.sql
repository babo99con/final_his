prompt === Ensure RESULT_MANAGER_ID and RESULT_MANAGER_NAME exist on result tables ===

declare
    v_old_count number;
    v_new_count number;
begin
    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM_RESULT'
       and column_name = 'RESULT_WRITER_ID';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM_RESULT'
       and column_name = 'RESULT_MANAGER_ID';

    if v_old_count = 1 and v_new_count = 0 then
        execute immediate 'alter table CHJ.IMAGING_EXAM_RESULT rename column RESULT_WRITER_ID to RESULT_MANAGER_ID';
    elsif v_new_count = 0 then
        execute immediate 'alter table CHJ.IMAGING_EXAM_RESULT add (RESULT_MANAGER_ID varchar2(30))';
    end if;

    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM_RESULT'
       and column_name = 'RESULT_WRITER_NAME';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM_RESULT'
       and column_name = 'RESULT_MANAGER_NAME';

    if v_old_count = 1 and v_new_count = 0 then
        execute immediate 'alter table CHJ.IMAGING_EXAM_RESULT rename column RESULT_WRITER_NAME to RESULT_MANAGER_NAME';
    elsif v_new_count = 0 then
        execute immediate 'alter table CHJ.IMAGING_EXAM_RESULT add (RESULT_MANAGER_NAME varchar2(100))';
    end if;

    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_EXAM_RESULT'
       and column_name = 'RESULT_WRITER_ID';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_EXAM_RESULT'
       and column_name = 'RESULT_MANAGER_ID';

    if v_old_count = 1 and v_new_count = 0 then
        execute immediate 'alter table CHJ.SPECIMEN_EXAM_RESULT rename column RESULT_WRITER_ID to RESULT_MANAGER_ID';
    elsif v_new_count = 0 then
        execute immediate 'alter table CHJ.SPECIMEN_EXAM_RESULT add (RESULT_MANAGER_ID varchar2(30))';
    end if;

    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_EXAM_RESULT'
       and column_name = 'RESULT_WRITER_NAME';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'SPECIMEN_EXAM_RESULT'
       and column_name = 'RESULT_MANAGER_NAME';

    if v_old_count = 1 and v_new_count = 0 then
        execute immediate 'alter table CHJ.SPECIMEN_EXAM_RESULT rename column RESULT_WRITER_NAME to RESULT_MANAGER_NAME';
    elsif v_new_count = 0 then
        execute immediate 'alter table CHJ.SPECIMEN_EXAM_RESULT add (RESULT_MANAGER_NAME varchar2(100))';
    end if;

    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PATHOLOGY_EXAM_RESULT'
       and column_name = 'RESULT_WRITER_ID';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PATHOLOGY_EXAM_RESULT'
       and column_name = 'RESULT_MANAGER_ID';

    if v_old_count = 1 and v_new_count = 0 then
        execute immediate 'alter table CHJ.PATHOLOGY_EXAM_RESULT rename column RESULT_WRITER_ID to RESULT_MANAGER_ID';
    elsif v_new_count = 0 then
        execute immediate 'alter table CHJ.PATHOLOGY_EXAM_RESULT add (RESULT_MANAGER_ID varchar2(30))';
    end if;

    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PATHOLOGY_EXAM_RESULT'
       and column_name = 'RESULT_WRITER_NAME';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PATHOLOGY_EXAM_RESULT'
       and column_name = 'RESULT_MANAGER_NAME';

    if v_old_count = 1 and v_new_count = 0 then
        execute immediate 'alter table CHJ.PATHOLOGY_EXAM_RESULT rename column RESULT_WRITER_NAME to RESULT_MANAGER_NAME';
    elsif v_new_count = 0 then
        execute immediate 'alter table CHJ.PATHOLOGY_EXAM_RESULT add (RESULT_MANAGER_NAME varchar2(100))';
    end if;

    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'ENDOSCOPY_EXAM_RESULT'
       and column_name = 'RESULT_WRITER_ID';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'ENDOSCOPY_EXAM_RESULT'
       and column_name = 'RESULT_MANAGER_ID';

    if v_old_count = 1 and v_new_count = 0 then
        execute immediate 'alter table CHJ.ENDOSCOPY_EXAM_RESULT rename column RESULT_WRITER_ID to RESULT_MANAGER_ID';
    elsif v_new_count = 0 then
        execute immediate 'alter table CHJ.ENDOSCOPY_EXAM_RESULT add (RESULT_MANAGER_ID varchar2(30))';
    end if;

    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'ENDOSCOPY_EXAM_RESULT'
       and column_name = 'RESULT_WRITER_NAME';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'ENDOSCOPY_EXAM_RESULT'
       and column_name = 'RESULT_MANAGER_NAME';

    if v_old_count = 1 and v_new_count = 0 then
        execute immediate 'alter table CHJ.ENDOSCOPY_EXAM_RESULT rename column RESULT_WRITER_NAME to RESULT_MANAGER_NAME';
    elsif v_new_count = 0 then
        execute immediate 'alter table CHJ.ENDOSCOPY_EXAM_RESULT add (RESULT_MANAGER_NAME varchar2(100))';
    end if;

    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PHYSIOLOGICAL_EXAM_RESULT'
       and column_name = 'RESULT_WRITER_ID';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PHYSIOLOGICAL_EXAM_RESULT'
       and column_name = 'RESULT_MANAGER_ID';

    if v_old_count = 1 and v_new_count = 0 then
        execute immediate 'alter table CHJ.PHYSIOLOGICAL_EXAM_RESULT rename column RESULT_WRITER_ID to RESULT_MANAGER_ID';
    elsif v_new_count = 0 then
        execute immediate 'alter table CHJ.PHYSIOLOGICAL_EXAM_RESULT add (RESULT_MANAGER_ID varchar2(30))';
    end if;

    select count(*)
      into v_old_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PHYSIOLOGICAL_EXAM_RESULT'
       and column_name = 'RESULT_WRITER_NAME';

    select count(*)
      into v_new_count
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'PHYSIOLOGICAL_EXAM_RESULT'
       and column_name = 'RESULT_MANAGER_NAME';

    if v_old_count = 1 and v_new_count = 0 then
        execute immediate 'alter table CHJ.PHYSIOLOGICAL_EXAM_RESULT rename column RESULT_WRITER_NAME to RESULT_MANAGER_NAME';
    elsif v_new_count = 0 then
        execute immediate 'alter table CHJ.PHYSIOLOGICAL_EXAM_RESULT add (RESULT_MANAGER_NAME varchar2(100))';
    end if;
end;
/

prompt === Done ===
