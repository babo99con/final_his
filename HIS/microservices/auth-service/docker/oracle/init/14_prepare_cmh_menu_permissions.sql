DECLARE
    v_menu_count NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_menu_count
      FROM USER_TABLES
     WHERE TABLE_NAME = 'MENU';

    IF v_menu_count = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE CMH.MENU (
                MENU_ID NUMBER NOT NULL,
                PARENT_ID NUMBER,
                CODE VARCHAR2(50) NOT NULL,
                NAME VARCHAR2(100) NOT NULL,
                PATH VARCHAR2(200),
                ICON VARCHAR2(50),
                SORT_ORDER NUMBER NOT NULL,
                IS_ACTIVE CHAR(1) NOT NULL,
                CREATED_AT TIMESTAMP(6) NOT NULL,
                UPDATED_AT TIMESTAMP(6) NOT NULL,
                CONSTRAINT PK_CMH_MENU PRIMARY KEY (MENU_ID),
                CONSTRAINT CK_CMH_MENU_IS_ACTIVE CHECK (IS_ACTIVE IN (''Y'', ''N''))
            )';
    END IF;
END;
/

MERGE INTO CMH.MENU target_menu
USING (
    SELECT MENU_ID,
           PARENT_ID,
           CODE,
           NAME,
           PATH,
           ICON,
           SORT_ORDER,
           CASE WHEN TO_CHAR(IS_ACTIVE) IN ('Y', '1') THEN 'Y' ELSE 'N' END AS IS_ACTIVE,
           CREATED_AT,
           UPDATED_AT
    FROM HOSPITAL.MENU
) source_menu
ON (target_menu.MENU_ID = source_menu.MENU_ID)
WHEN MATCHED THEN
    UPDATE SET
        target_menu.PARENT_ID = source_menu.PARENT_ID,
        target_menu.CODE = source_menu.CODE,
        target_menu.NAME = source_menu.NAME,
        target_menu.PATH = source_menu.PATH,
        target_menu.ICON = source_menu.ICON,
        target_menu.SORT_ORDER = source_menu.SORT_ORDER,
        target_menu.IS_ACTIVE = source_menu.IS_ACTIVE,
        target_menu.CREATED_AT = source_menu.CREATED_AT,
        target_menu.UPDATED_AT = source_menu.UPDATED_AT
WHEN NOT MATCHED THEN
    INSERT (MENU_ID, PARENT_ID, CODE, NAME, PATH, ICON, SORT_ORDER, IS_ACTIVE, CREATED_AT, UPDATED_AT)
    VALUES (
        source_menu.MENU_ID,
        source_menu.PARENT_ID,
        source_menu.CODE,
        source_menu.NAME,
        source_menu.PATH,
        source_menu.ICON,
        source_menu.SORT_ORDER,
        source_menu.IS_ACTIVE,
        source_menu.CREATED_AT,
        source_menu.UPDATED_AT
    );

DECLARE
    v_table_count NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_table_count
      FROM USER_TABLES
     WHERE TABLE_NAME = 'AUTH_USER_MENU_PERMISSION';

    IF v_table_count = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE CMH.AUTH_USER_MENU_PERMISSION (
                USER_ID VARCHAR2(20) NOT NULL,
                MENU_ID NUMBER NOT NULL,
                CAN_VIEW CHAR(1),
                CAN_CREATE CHAR(1),
                CAN_UPDATE CHAR(1),
                CAN_DELETE CHAR(1),
                CONSTRAINT PK_AUTH_USER_MENU_PERMISSION PRIMARY KEY (USER_ID, MENU_ID),
                CONSTRAINT FK_AUMP_USER_ID FOREIGN KEY (USER_ID) REFERENCES CMH.AUTH_USER (ID),
                CONSTRAINT FK_AUMP_MENU_ID FOREIGN KEY (MENU_ID) REFERENCES CMH.MENU (MENU_ID),
                CONSTRAINT CK_AUMP_CAN_VIEW CHECK (CAN_VIEW IN (''Y'', ''N'') OR CAN_VIEW IS NULL),
                CONSTRAINT CK_AUMP_CAN_CREATE CHECK (CAN_CREATE IN (''Y'', ''N'') OR CAN_CREATE IS NULL),
                CONSTRAINT CK_AUMP_CAN_UPDATE CHECK (CAN_UPDATE IN (''Y'', ''N'') OR CAN_UPDATE IS NULL),
                CONSTRAINT CK_AUMP_CAN_DELETE CHECK (CAN_DELETE IN (''Y'', ''N'') OR CAN_DELETE IS NULL)
            )';
    END IF;
END;
/

DECLARE
    v_constraint_count NUMBER;
BEGIN
    SELECT COUNT(*)
      INTO v_constraint_count
      FROM USER_CONSTRAINTS
     WHERE CONSTRAINT_NAME = 'FK_ARMP_MENU_ID';

    IF v_constraint_count = 0 THEN
        EXECUTE IMMEDIATE '
            ALTER TABLE CMH.AUTH_ROLE_MENU_PERMISSION
            ADD CONSTRAINT FK_ARMP_MENU_ID FOREIGN KEY (MENU_ID) REFERENCES CMH.MENU (MENU_ID)';
    END IF;
END;
/

GRANT SELECT, INSERT, UPDATE, DELETE ON CMH.MENU TO HOSPITAL;
GRANT SELECT, INSERT, UPDATE, DELETE ON CMH.AUTH_USER_MENU_PERMISSION TO HOSPITAL;

COMMIT;
