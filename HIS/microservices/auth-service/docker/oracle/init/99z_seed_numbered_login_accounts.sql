-- Seed legacy numbered login accounts and keep them idempotent for local rebuilds.
SET DEFINE OFF;

DECLARE
  PROCEDURE upsert_auth(p_login_id VARCHAR2, p_role_code VARCHAR2) IS
  BEGIN
    MERGE INTO CMH.AUTH_USER target
    USING (
      SELECT p_login_id AS id,
             p_login_id AS login_id,
             p_login_id AS username,
             '1111' AS password_hash,
             p_role_code AS role_code,
             'ACTIVE' AS account_status
      FROM dual
    ) source
    ON (LOWER(target.LOGIN_ID) = LOWER(source.login_id))
    WHEN MATCHED THEN UPDATE SET
      target.USERNAME = source.username,
      target.PASSWORD_HASH = source.password_hash,
      target.ROLE_CODE = source.role_code,
      target.ACCOUNT_STATUS = source.account_status,
      target.UPDATED_AT = SYSTIMESTAMP
    WHEN NOT MATCHED THEN INSERT (
      ID, LOGIN_ID, USERNAME, PASSWORD_HASH, ROLE_CODE, ACCOUNT_STATUS, CREATED_AT, UPDATED_AT
    ) VALUES (
      source.id, source.login_id, source.username, source.password_hash, source.role_code, source.account_status, SYSTIMESTAMP, SYSTIMESTAMP
    );
  END;

  PROCEDURE upsert_jch_employee(p_staff_id VARCHAR2, p_dept_id VARCHAR2, p_name VARCHAR2) IS
  BEGIN
    MERGE INTO JCH.EMPLOYEE target
    USING (
      SELECT p_staff_id AS staff_id,
             p_dept_id AS dept_id,
             p_name AS real_name,
             p_name AS name,
             'ACTIVE' AS status,
             '01000000000' AS phone,
             LOWER(REPLACE(p_staff_id, '-', '')) || '@hospital.local' AS email
      FROM dual
    ) source
    ON (target.STAFF_ID = source.staff_id)
    WHEN MATCHED THEN UPDATE SET
      target.DEPT_ID = source.dept_id,
      target.REAL_NAME = source.real_name,
      target.NAME = source.name,
      target.STATUS = source.status,
      target.PHONE = source.phone,
      target.EMAIL = source.email,
      target.UPDATED_AT = SYSDATE
    WHEN NOT MATCHED THEN INSERT (
      STAFF_ID, DEPT_ID, REAL_NAME, NAME, PHONE, EMAIL, STATUS, CREATED_AT, UPDATED_AT
    ) VALUES (
      source.staff_id, source.dept_id, source.real_name, source.name, source.phone, source.email, source.status, SYSDATE, SYSDATE
    );
  END;

  PROCEDURE upsert_cmh_staff(p_num_id NUMBER, p_login_id VARCHAR2, p_role_code VARCHAR2, p_name VARCHAR2) IS
  BEGIN
    MERGE INTO CMH.STAFF target
    USING (
      SELECT p_num_id AS id,
             p_login_id AS username,
             p_login_id AS staff_id,
             p_role_code AS domain_role,
             p_name AS full_name,
             '1111' AS password_hash,
             'ACTIVE' AS status,
             'ACTIVE' AS employment_status
      FROM dual
    ) source
    ON (target.STAFF_ID = source.staff_id)
    WHEN MATCHED THEN UPDATE SET
      target.USERNAME = source.username,
      target.PASSWORD_HASH = source.password_hash,
      target.STATUS = source.status,
      target.DOMAIN_ROLE = source.domain_role,
      target.FULL_NAME = source.full_name,
      target.EMPLOYMENT_STATUS = source.employment_status,
      target.UPDATED_AT = SYSDATE
    WHEN NOT MATCHED THEN INSERT (
      ID, USERNAME, PASSWORD_HASH, STATUS, DOMAIN_ROLE, FULL_NAME, CREATED_AT, UPDATED_AT, STAFF_ID, EMPLOYMENT_STATUS
    ) VALUES (
      source.id, source.username, source.password_hash, source.status, source.domain_role, source.full_name, SYSDATE, SYSDATE, source.staff_id, source.employment_status
    );
  END;
BEGIN
  upsert_auth('26-1001', 'DOCTOR');
  upsert_auth('26-2001', 'NURSE');
  upsert_auth('26-3001', 'RECEPTION');
  upsert_auth('26-4001', 'RADIOLOGY_TECH');
  upsert_auth('26-5001', 'CLINICAL_LAB_TECH');
  upsert_auth('26-6001', 'ADMIN');

  upsert_jch_employee('26-1001', 'DEPT_DIAG', 'Doctor 26-1001');
  upsert_jch_employee('26-2001', 'DEPT_DIAG', 'Nurse 26-2001');
  upsert_jch_employee('26-3001', 'DEPT_ADMIN', 'Reception 26-3001');
  upsert_jch_employee('26-4001', 'DEPT_DIAG', 'Radiology 26-4001');
  upsert_jch_employee('26-5001', 'DEPT_DIAG', 'Lab 26-5001');
  upsert_jch_employee('26-6001', 'DEPT_ADMIN', 'Admin 26-6001');

  upsert_cmh_staff(1001, '26-1001', 'DOCTOR', 'Doctor 26-1001');
  upsert_cmh_staff(2001, '26-2001', 'NURSE', 'Nurse 26-2001');
  upsert_cmh_staff(3001, '26-3001', 'RECEPTION', 'Reception 26-3001');
  upsert_cmh_staff(4001, '26-4001', 'RADIOLOGY_TECH', 'Radiology 26-4001');
  upsert_cmh_staff(5001, '26-5001', 'CLINICAL_LAB_TECH', 'Lab 26-5001');
  upsert_cmh_staff(6001, '26-6001', 'ADMIN', 'Admin 26-6001');

  MERGE INTO JCH.EMPLOYEE_DOCTOR target
  USING (SELECT '26-1001' AS staff_id, 'LIC-26-1001' AS license_no FROM dual) source
  ON (target.STAFF_ID = source.staff_id)
  WHEN MATCHED THEN UPDATE SET target.LICENSE_NO = source.license_no, target.DOCTOR_TYPE = 'FULL_TIME', target.UPDATED_AT = SYSDATE
  WHEN NOT MATCHED THEN INSERT (STAFF_ID, LICENSE_NO, DOCTOR_TYPE, CREATED_AT, UPDATED_AT)
    VALUES (source.staff_id, source.license_no, 'FULL_TIME', SYSDATE, SYSDATE);

  MERGE INTO JCH.EMPLOYEE_NURSE target
  USING (SELECT '26-2001' AS staff_id, 'NUR-LIC-26-2001' AS license_no FROM dual) source
  ON (target.STAFF_ID = source.staff_id)
  WHEN MATCHED THEN UPDATE SET target.LICENSE_NO = source.license_no, target.NURSE_TYPE = 'WARD', target.SHIFT_TYPE = 'DAY', target.EMPLOYMENT_TYPE = 'FULL_TIME', target.UPDATED_AT = SYSDATE
  WHEN NOT MATCHED THEN INSERT (STAFF_ID, LICENSE_NO, NURSE_TYPE, SHIFT_TYPE, EMPLOYMENT_TYPE, CREATED_AT, UPDATED_AT)
    VALUES (source.staff_id, source.license_no, 'WARD', 'DAY', 'FULL_TIME', SYSDATE, SYSDATE);

  MERGE INTO JCH.EMPLOYEE_RECEPTION target
  USING (SELECT '26-3001' AS staff_id FROM dual) source
  ON (target.STAFF_ID = source.staff_id)
  WHEN MATCHED THEN UPDATE SET target.JOB_TYPE_CD = 'RECEPTION', target.DESK_NO = 'R-01', target.SHIFT_TYPE = 'DAY', target.RECEPTION_TYPE = 'GENERAL', target.EXT_NO = '3001'
  WHEN NOT MATCHED THEN INSERT (STAFF_ID, JOB_TYPE_CD, DESK_NO, SHIFT_TYPE, RECEPTION_TYPE, EXT_NO)
    VALUES (source.staff_id, 'RECEPTION', 'R-01', 'DAY', 'GENERAL', '3001');

  DELETE FROM CMH.AUTH_SESSION
   WHERE USER_ID NOT LIKE '26-%';

  DELETE FROM CMH.LOGIN_HISTORY
   WHERE (USER_ID IS NOT NULL AND USER_ID NOT LIKE '26-%')
      OR LOWER(NVL(LOGIN_ID, USERNAME)) NOT LIKE '26-%'
      OR LOWER(USERNAME) NOT LIKE '26-%';

  DELETE FROM CMH.AUTH_USER_MENU_PERMISSION
   WHERE USER_ID NOT LIKE '26-%';

  DELETE FROM CMH.AUTH_USER
   WHERE ID NOT LIKE '26-%'
      OR LOGIN_ID NOT LIKE '26-%'
      OR USERNAME NOT LIKE '26-%';

  DELETE FROM JCH.EMPLOYEE_DOCTOR
   WHERE STAFF_ID NOT LIKE '26-%';

  DELETE FROM JCH.EMPLOYEE_NURSE
   WHERE STAFF_ID NOT LIKE '26-%';

  DELETE FROM JCH.EMPLOYEE_RECEPTION
   WHERE STAFF_ID NOT LIKE '26-%';

  DELETE FROM JCH.EMPLOYEE_PRIVATE
   WHERE STAFF_ID NOT LIKE '26-%';

  DELETE FROM JCH.EMPLOYEE
   WHERE STAFF_ID NOT LIKE '26-%';

  DELETE FROM CMH.STAFF_CREDENTIAL
   WHERE STAFF_ID IN (SELECT ID FROM CMH.STAFF WHERE STAFF_ID NOT LIKE '26-%');

  DELETE FROM CMH.STAFF_HISTORY
   WHERE STAFF_ID IN (SELECT ID FROM CMH.STAFF WHERE STAFF_ID NOT LIKE '26-%');

  DELETE FROM CMH.STAFF_CHANGE_REQUEST
   WHERE STAFF_ID IN (SELECT ID FROM CMH.STAFF WHERE STAFF_ID NOT LIKE '26-%');

  DELETE FROM CMH.STAFF
   WHERE STAFF_ID NOT LIKE '26-%'
      OR USERNAME NOT LIKE '26-%';

  COMMIT;
END;
/
