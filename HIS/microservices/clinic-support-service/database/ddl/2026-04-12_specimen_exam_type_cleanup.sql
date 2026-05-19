prompt === Clear CHJ.SPECIMEN_EXAM.SPECIMEN_TYPE values ===

update CHJ.SPECIMEN_EXAM
   set SPECIMEN_TYPE = null
 where SPECIMEN_TYPE is not null;

commit;
