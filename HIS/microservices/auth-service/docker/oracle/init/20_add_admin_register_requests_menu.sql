MERGE INTO CMH.MENU target_menu
USING (
    SELECT 1504 AS MENU_ID,
           1500 AS PARENT_ID,
           'ADMIN_REGISTER_REQUESTS' AS CODE,
           '가입 승인 관리' AS NAME,
           '/admin/register-requests' AS PATH,
           NULL AS ICON,
           4 AS SORT_ORDER,
           'Y' AS IS_ACTIVE
      FROM DUAL
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
        target_menu.UPDATED_AT = SYSTIMESTAMP
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
        SYSTIMESTAMP,
        SYSTIMESTAMP
    );

MERGE INTO CMH.AUTH_ROLE_MENU_PERMISSION target_permission
USING (
    SELECT ROLE_CODE,
           1504 AS MENU_ID,
           CASE WHEN ROLE_CODE = 'ADMIN' THEN 'Y' ELSE 'N' END AS CAN_VIEW
      FROM CMH.AUTH_ROLE
) source_permission
ON (
    target_permission.ROLE_CODE = source_permission.ROLE_CODE
    AND target_permission.MENU_ID = source_permission.MENU_ID
)
WHEN MATCHED THEN
    UPDATE SET
        target_permission.CAN_VIEW = source_permission.CAN_VIEW,
        target_permission.CAN_CREATE = 'N',
        target_permission.CAN_UPDATE = 'N',
        target_permission.CAN_DELETE = 'N'
WHEN NOT MATCHED THEN
    INSERT (ROLE_CODE, MENU_ID, CAN_VIEW, CAN_CREATE, CAN_UPDATE, CAN_DELETE)
    VALUES (
        source_permission.ROLE_CODE,
        source_permission.MENU_ID,
        source_permission.CAN_VIEW,
        'N',
        'N',
        'N'
    );

COMMIT;
