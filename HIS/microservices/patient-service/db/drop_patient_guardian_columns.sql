/*------------------------------------------------------------------------------
-- PATIENT 테이블에서 가족(보호자) 관련 컬럼 제거
-- migrate_guardian_to_family.sql 실행 및 애플리케이션 코드 반영 후 실행하세요.
------------------------------------------------------------------------------*/

ALTER TABLE PATIENT DROP COLUMN GUARDIAN_NAME;
ALTER TABLE PATIENT DROP COLUMN GUARDIAN_PHONE;
ALTER TABLE PATIENT DROP COLUMN GUARDIAN_RELATION;
ALTER TABLE PATIENT DROP COLUMN CONTACT_PRIORITY;
