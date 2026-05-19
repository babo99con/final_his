DECLARE
    v_full_name_exists NUMBER := 0;
    v_status_exists NUMBER := 0;
    v_employee_status_constraint_exists NUMBER := 0;
BEGIN
    SELECT COUNT(*)
      INTO v_full_name_exists
      FROM ALL_TAB_COLUMNS
     WHERE OWNER = 'CMH'
       AND TABLE_NAME = 'AUTH_USER'
       AND COLUMN_NAME = 'FULL_NAME';

    SELECT COUNT(*)
      INTO v_status_exists
      FROM ALL_TAB_COLUMNS
     WHERE OWNER = 'CMH'
       AND TABLE_NAME = 'AUTH_USER'
       AND COLUMN_NAME = 'STATUS';

    SELECT COUNT(*)
      INTO v_employee_status_constraint_exists
      FROM ALL_CONSTRAINTS
     WHERE OWNER = 'JCH'
       AND TABLE_NAME = 'EMPLOYEE'
       AND CONSTRAINT_NAME = 'CK_EMPLOYEE_STATUS';

    IF v_employee_status_constraint_exists = 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE JCH.EMPLOYEE DROP CONSTRAINT CK_EMPLOYEE_STATUS';
    END IF;

    EXECUTE IMMEDIATE q'[
        ALTER TABLE JCH.EMPLOYEE
        ADD CONSTRAINT CK_EMPLOYEE_STATUS
        CHECK (STATUS IN ('ACTIVE', 'INACTIVE', 'LEAVE', 'RETIRE', 'PENDING_APPROVAL', 'REJECTED_SIGNUP'))
    ]';

    IF v_full_name_exists = 1 AND v_status_exists = 1 THEN
        EXECUTE IMMEDIATE q'[
            MERGE INTO JCH.EMPLOYEE e
            USING (
                SELECT
                    ID,
                    USERNAME,
                    NVL(FULL_NAME, USERNAME) AS FULL_NAME,
                    NVL(STATUS, 'INACTIVE') AS STATUS
                FROM CMH.AUTH_USER
            ) a
               ON (e.STAFF_ID = a.ID)
            WHEN NOT MATCHED THEN
                INSERT (
                    STAFF_ID,
                    DEPT_ID,
                    NAME,
                    STATUS,
                    CREATED_AT,
                    UPDATED_AT
                )
                VALUES (
                    a.ID,
                    'DEPT_DIAG',
                    a.FULL_NAME,
                    a.STATUS,
                    SYSDATE,
                    SYSDATE
                )
        ]';
    END IF;

    IF v_full_name_exists = 1 THEN
        EXECUTE IMMEDIATE q'[
            MERGE INTO JCH.EMPLOYEE e
            USING (
                SELECT ID, FULL_NAME
                FROM CMH.AUTH_USER
            ) a
               ON (e.STAFF_ID = a.ID)
            WHEN MATCHED THEN
                UPDATE SET
                    e.NAME = CASE
                        WHEN e.NAME IS NULL OR TRIM(e.NAME) = '' THEN a.FULL_NAME
                        ELSE e.NAME
                    END,
                    e.UPDATED_AT = SYSDATE
        ]';
    END IF;

    IF v_status_exists = 1 THEN
        EXECUTE IMMEDIATE q'[
            MERGE INTO JCH.EMPLOYEE e
            USING (
                SELECT ID, STATUS
                FROM CMH.AUTH_USER
            ) a
               ON (e.STAFF_ID = a.ID)
            WHEN MATCHED THEN
                UPDATE SET
                    e.STATUS = a.STATUS,
                    e.UPDATED_AT = SYSDATE
        ]';
    END IF;

    IF v_full_name_exists = 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE CMH.AUTH_USER DROP COLUMN FULL_NAME';
    END IF;

    IF v_status_exists = 1 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE CMH.AUTH_USER DROP COLUMN STATUS';
    END IF;
END;
/
