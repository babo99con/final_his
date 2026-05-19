DECLARE
    v_created_at_exists NUMBER := 0;
    v_updated_at_exists NUMBER := 0;
BEGIN
    SELECT COUNT(*)
      INTO v_created_at_exists
      FROM ALL_TAB_COLUMNS
     WHERE OWNER = 'CMH'
       AND TABLE_NAME = 'AUTH_USER'
       AND COLUMN_NAME = 'CREATED_AT';

    SELECT COUNT(*)
      INTO v_updated_at_exists
      FROM ALL_TAB_COLUMNS
     WHERE OWNER = 'CMH'
       AND TABLE_NAME = 'AUTH_USER'
       AND COLUMN_NAME = 'UPDATED_AT';

    IF v_created_at_exists = 1 THEN
        EXECUTE IMMEDIATE q'[
            MERGE INTO JCH.EMPLOYEE e
            USING (
                SELECT ID, CREATED_AT
                FROM CMH.AUTH_USER
            ) a
               ON (e.STAFF_ID = a.ID)
            WHEN MATCHED THEN
                UPDATE SET
                    e.CREATED_AT = NVL(e.CREATED_AT, a.CREATED_AT)
        ]';

        EXECUTE IMMEDIATE 'ALTER TABLE CMH.AUTH_USER DROP COLUMN CREATED_AT';
    END IF;

    IF v_updated_at_exists = 1 THEN
        EXECUTE IMMEDIATE q'[
            MERGE INTO JCH.EMPLOYEE e
            USING (
                SELECT ID, UPDATED_AT
                FROM CMH.AUTH_USER
            ) a
               ON (e.STAFF_ID = a.ID)
            WHEN MATCHED THEN
                UPDATE SET
                    e.UPDATED_AT = NVL(e.UPDATED_AT, a.UPDATED_AT)
        ]';

        EXECUTE IMMEDIATE 'ALTER TABLE CMH.AUTH_USER DROP COLUMN UPDATED_AT';
    END IF;
END;
/
