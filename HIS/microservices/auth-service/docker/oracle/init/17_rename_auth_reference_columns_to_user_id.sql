-- Rename auth-only reference columns from STAFF_ID to USER_ID.
-- This targets only the lightweight auth tables under CMH.

DECLARE
    v_staff_id_count NUMBER;
    v_user_id_count NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_staff_id_count
      FROM USER_TAB_COLUMNS
     WHERE TABLE_NAME = 'AUTH_USER_MENU_PERMISSION'
       AND COLUMN_NAME = 'STAFF_ID';

    SELECT COUNT(*)
      INTO v_user_id_count
      FROM USER_TAB_COLUMNS
     WHERE TABLE_NAME = 'AUTH_USER_MENU_PERMISSION'
       AND COLUMN_NAME = 'USER_ID';

    IF v_staff_id_count = 1 AND v_user_id_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE CMH.AUTH_USER_MENU_PERMISSION RENAME COLUMN STAFF_ID TO USER_ID';
    END IF;
END;
/

DECLARE
    v_staff_id_count NUMBER;
    v_user_id_count NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_staff_id_count
      FROM USER_TAB_COLUMNS
     WHERE TABLE_NAME = 'AUTH_SESSION'
       AND COLUMN_NAME = 'STAFF_ID';

    SELECT COUNT(*)
      INTO v_user_id_count
      FROM USER_TAB_COLUMNS
     WHERE TABLE_NAME = 'AUTH_SESSION'
       AND COLUMN_NAME = 'USER_ID';

    IF v_staff_id_count = 1 AND v_user_id_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE CMH.AUTH_SESSION RENAME COLUMN STAFF_ID TO USER_ID';
    END IF;
END;
/

DECLARE
    v_staff_id_count NUMBER;
    v_user_id_count NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_staff_id_count
      FROM USER_TAB_COLUMNS
     WHERE TABLE_NAME = 'LOGIN_HISTORY'
       AND COLUMN_NAME = 'STAFF_ID';

    SELECT COUNT(*)
      INTO v_user_id_count
      FROM USER_TAB_COLUMNS
     WHERE TABLE_NAME = 'LOGIN_HISTORY'
       AND COLUMN_NAME = 'USER_ID';

    IF v_staff_id_count = 1 AND v_user_id_count = 0 THEN
        EXECUTE IMMEDIATE 'ALTER TABLE CMH.LOGIN_HISTORY RENAME COLUMN STAFF_ID TO USER_ID';
    END IF;
END;
/

DECLARE
    PROCEDURE rename_constraint_if_exists(p_table_name IN VARCHAR2,
                                          p_old_name IN VARCHAR2,
                                          p_new_name IN VARCHAR2) IS
        v_old_count NUMBER;
        v_new_count NUMBER;
    BEGIN
        SELECT COUNT(*)
          INTO v_old_count
          FROM USER_CONSTRAINTS
         WHERE TABLE_NAME = p_table_name
           AND CONSTRAINT_NAME = p_old_name;

        SELECT COUNT(*)
          INTO v_new_count
          FROM USER_CONSTRAINTS
         WHERE TABLE_NAME = p_table_name
           AND CONSTRAINT_NAME = p_new_name;

        IF v_old_count = 1 AND v_new_count = 0 THEN
            EXECUTE IMMEDIATE 'ALTER TABLE CMH.' || p_table_name || ' RENAME CONSTRAINT ' || p_old_name || ' TO ' || p_new_name;
        END IF;
    END;
BEGIN
    rename_constraint_if_exists('AUTH_USER_MENU_PERMISSION', 'FK_AUMP_STAFF_ID', 'FK_AUMP_USER_ID');
    rename_constraint_if_exists('AUTH_USER_MENU_PERMISSION', 'FK_AUMP_AUTH_USER_ID', 'FK_AUMP_USER_ID');
    rename_constraint_if_exists('AUTH_SESSION', 'FK_AUTH_SESSION_STAFF_ID', 'FK_AUTH_SESSION_USER_ID');
    rename_constraint_if_exists('AUTH_SESSION', 'FK_AUTH_SESSION_AUTH_USER_ID', 'FK_AUTH_SESSION_USER_ID');
    rename_constraint_if_exists('LOGIN_HISTORY', 'FK_LOGIN_HISTORY_STAFF_ID', 'FK_LOGIN_HISTORY_USER_ID');
    rename_constraint_if_exists('LOGIN_HISTORY', 'FK_LOGIN_HISTORY_AUTH_USER_ID', 'FK_LOGIN_HISTORY_USER_ID');
END;
/

COMMIT;
