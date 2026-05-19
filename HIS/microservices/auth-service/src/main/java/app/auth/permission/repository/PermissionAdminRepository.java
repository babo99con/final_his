package app.auth.permission.repository;

import app.auth.permission.entity.RoleMenuPermissionRow;
import app.auth.permission.entity.UserMenuPermissionRow;
import app.auth.menu.entity.AuthMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionAdminRepository extends JpaRepository<AuthMenu, Integer> {

    @Query(value = """
        SELECT
            m.MENU_ID AS menuId,
            m.PARENT_ID AS parentMenuId,
            m.CODE AS menuCode,
            m.NAME AS menuName,
            m.PATH AS menuPath,
            m.ICON AS menuIcon,
            m.SORT_ORDER AS sortOrder,
            m.IS_ACTIVE AS isActive,
            NVL(p.CAN_VIEW, 'N') AS canView,
            NVL(p.CAN_CREATE, 'N') AS canCreate,
            NVL(p.CAN_UPDATE, 'N') AS canUpdate,
            NVL(p.CAN_DELETE, 'N') AS canDelete
        FROM CMH.MENU m
        LEFT JOIN CMH.AUTH_ROLE_MENU_PERMISSION p
          ON p.ROLE_CODE = :roleCode
         AND p.MENU_ID = m.MENU_ID
        ORDER BY NVL(m.PARENT_ID, 0), m.SORT_ORDER, m.MENU_ID
        """, nativeQuery = true)
    List<RoleMenuPermissionView> findRoleMenuPermissionViews(@Param("roleCode") String roleCode);

    @Query(value = """
        SELECT
            m.MENU_ID AS menuId,
            m.PARENT_ID AS parentMenuId,
            m.CODE AS menuCode,
            m.NAME AS menuName,
            m.PATH AS menuPath,
            m.ICON AS menuIcon,
            m.SORT_ORDER AS sortOrder,
            m.IS_ACTIVE AS isActive,
            NVL(rp.CAN_VIEW, 'N') AS roleCanView,
            NVL(rp.CAN_CREATE, 'N') AS roleCanCreate,
            NVL(rp.CAN_UPDATE, 'N') AS roleCanUpdate,
            NVL(rp.CAN_DELETE, 'N') AS roleCanDelete,
            up.CAN_VIEW AS userCanView,
            up.CAN_CREATE AS userCanCreate,
            up.CAN_UPDATE AS userCanUpdate,
            up.CAN_DELETE AS userCanDelete,
            NVL(up.CAN_VIEW, NVL(rp.CAN_VIEW, 'N')) AS finalCanView,
            NVL(up.CAN_CREATE, NVL(rp.CAN_CREATE, 'N')) AS finalCanCreate,
            NVL(up.CAN_UPDATE, NVL(rp.CAN_UPDATE, 'N')) AS finalCanUpdate,
            NVL(up.CAN_DELETE, NVL(rp.CAN_DELETE, 'N')) AS finalCanDelete
        FROM CMH.MENU m
        LEFT JOIN CMH.AUTH_ROLE_MENU_PERMISSION rp
          ON rp.ROLE_CODE = :roleCode
         AND rp.MENU_ID = m.MENU_ID
        LEFT JOIN CMH.AUTH_USER_MENU_PERMISSION up
          ON up.USER_ID = :userId
         AND up.MENU_ID = m.MENU_ID
        ORDER BY NVL(m.PARENT_ID, 0), m.SORT_ORDER, m.MENU_ID
        """, nativeQuery = true)
    List<UserMenuPermissionView> findUserMenuPermissionViews(@Param("roleCode") String roleCode,
                                                             @Param("userId") String userId);

    default List<RoleMenuPermissionRow> findRoleMenuPermissions(String roleCode) {
        return findRoleMenuPermissionViews(roleCode).stream()
                .map(view -> {
                    RoleMenuPermissionRow row = new RoleMenuPermissionRow();
                    row.setMenuId(view.getMenuId());
                    row.setParentMenuId(view.getParentMenuId());
                    row.setMenuCode(view.getMenuCode());
                    row.setMenuName(view.getMenuName());
                    row.setMenuPath(view.getMenuPath());
                    row.setMenuIcon(view.getMenuIcon());
                    row.setSortOrder(view.getSortOrder());
                    row.setIsActive(view.getIsActive());
                    row.setCanView(view.getCanView());
                    row.setCanCreate(view.getCanCreate());
                    row.setCanUpdate(view.getCanUpdate());
                    row.setCanDelete(view.getCanDelete());
                    return row;
                })
                .toList();
    }

    default List<UserMenuPermissionRow> findUserMenuPermissions(String userId, String roleCode) {
        return findUserMenuPermissionViews(roleCode, userId).stream()
                .map(view -> {
                    UserMenuPermissionRow row = new UserMenuPermissionRow();
                    row.setMenuId(view.getMenuId());
                    row.setParentMenuId(view.getParentMenuId());
                    row.setMenuCode(view.getMenuCode());
                    row.setMenuName(view.getMenuName());
                    row.setMenuPath(view.getMenuPath());
                    row.setMenuIcon(view.getMenuIcon());
                    row.setSortOrder(view.getSortOrder());
                    row.setIsActive(view.getIsActive());
                    row.setRoleCanView(view.getRoleCanView());
                    row.setRoleCanCreate(view.getRoleCanCreate());
                    row.setRoleCanUpdate(view.getRoleCanUpdate());
                    row.setRoleCanDelete(view.getRoleCanDelete());
                    row.setUserCanView(view.getUserCanView());
                    row.setUserCanCreate(view.getUserCanCreate());
                    row.setUserCanUpdate(view.getUserCanUpdate());
                    row.setUserCanDelete(view.getUserCanDelete());
                    row.setFinalCanView(view.getFinalCanView());
                    row.setFinalCanCreate(view.getFinalCanCreate());
                    row.setFinalCanUpdate(view.getFinalCanUpdate());
                    row.setFinalCanDelete(view.getFinalCanDelete());
                    return row;
                })
                .toList();
    }

    interface RoleMenuPermissionView {
        Integer getMenuId();
        Integer getParentMenuId();
        String getMenuCode();
        String getMenuName();
        String getMenuPath();
        String getMenuIcon();
        Integer getSortOrder();
        String getIsActive();
        String getCanView();
        String getCanCreate();
        String getCanUpdate();
        String getCanDelete();
    }

    interface UserMenuPermissionView {
        Integer getMenuId();
        Integer getParentMenuId();
        String getMenuCode();
        String getMenuName();
        String getMenuPath();
        String getMenuIcon();
        Integer getSortOrder();
        String getIsActive();
        String getRoleCanView();
        String getRoleCanCreate();
        String getRoleCanUpdate();
        String getRoleCanDelete();
        String getUserCanView();
        String getUserCanCreate();
        String getUserCanUpdate();
        String getUserCanDelete();
        String getFinalCanView();
        String getFinalCanCreate();
        String getFinalCanUpdate();
        String getFinalCanDelete();
    }
}
