prompt === Make CHJ.IMAGING_EXAM.UPDATED_AT nullable ===

declare
    v_nullable varchar2(1);
begin
    select nullable
      into v_nullable
      from all_tab_columns
     where owner = 'CHJ'
       and table_name = 'IMAGING_EXAM'
       and column_name = 'UPDATED_AT';

    if v_nullable = 'N' then
        execute immediate 'alter table CHJ.IMAGING_EXAM modify UPDATED_AT null';
    end if;
end;
/

prompt === Done ===
