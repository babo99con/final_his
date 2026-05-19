-- Rename the lightweight auth account table from CMH.STAFF to CMH.AUTH_USER.
-- Reference column renames are handled in the follow-up auth migration script.

DECLARE
    v_staff_count NUMBER;
    v_auth_user_count NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_staff_count
      FROM USER_TABLES
     WHERE TABLE_NAME = 'STAFF';

    SELECT COUNT(*)
      INTO v_auth_user_count
      FROM USER_TABLES
     WHERE TABLE_NAME = 'AUTH_USER';

    IF v_staff_count = 1 AND v_auth_user_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE CMH.STAFF RENAME TO AUTH_USER';
    END IF;
END;
/

DECLARE
    v_staff_seq_count NUMBER;
    v_auth_user_seq_count NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_staff_seq_count
      FROM USER_SEQUENCES
     WHERE SEQUENCE_NAME = 'STAFF_ID_SEQ';

    SELECT COUNT(*)
      INTO v_auth_user_seq_count
      FROM USER_SEQUENCES
     WHERE SEQUENCE_NAME = 'AUTH_USER_ID_SEQ';

    IF v_staff_seq_count = 1 AND v_auth_user_seq_count = 0 THEN
        EXECUTE IMMEDIATE 'RENAME STAFF_ID_SEQ TO AUTH_USER_ID_SEQ';
    END IF;
END;
/

DECLARE
    v_auth_user_count NUMBER;
    v_target NUMBER;
    v_next NUMBER;
    v_increment NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_auth_user_count
      FROM USER_TABLES
     WHERE TABLE_NAME = 'AUTH_USER';

    IF v_auth_user_count = 1 THEN
        SELECT NVL(MAX(TO_NUMBER(REGEXP_SUBSTR(ID, '[0-9]{4}$'))), 0) + 1
          INTO v_target
          FROM CMH.AUTH_USER;

        SELECT CMH.AUTH_USER_ID_SEQ.NEXTVAL
          INTO v_next
          FROM DUAL;

        IF v_next < v_target THEN
            v_increment := v_target - v_next;
            EXECUTE IMMEDIATE 'ALTER SEQUENCE CMH.AUTH_USER_ID_SEQ INCREMENT BY ' || v_increment;
            SELECT CMH.AUTH_USER_ID_SEQ.NEXTVAL
              INTO v_next
              FROM DUAL;
            EXECUTE IMMEDIATE 'ALTER SEQUENCE CMH.AUTH_USER_ID_SEQ INCREMENT BY 1';
        END IF;
    END IF;
END;
/

BEGIN
    EXECUTE IMMEDIATE 'GRANT SELECT, INSERT, UPDATE, DELETE ON CMH.AUTH_USER TO HOSPITAL';
    EXECUTE IMMEDIATE 'GRANT SELECT ON CMH.AUTH_USER_ID_SEQ TO HOSPITAL';
EXCEPTION
    WHEN OTHERS THEN
        IF SQLCODE NOT IN (-942, -2289, -1917) THEN
            RAISE;
        END IF;
END;
/

COMMIT;
