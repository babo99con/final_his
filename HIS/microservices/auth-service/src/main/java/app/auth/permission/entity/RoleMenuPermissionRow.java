package app.auth.permission.entity;

public class RoleMenuPermissionRow {

    private Integer menuId;
    private Integer parentMenuId;
    private String menuCode;
    private String menuName;
    private String menuPath;
    private String menuIcon;
    private Integer sortOrder;
    private String isActive;
    private String canView;
    private String canCreate;
    private String canUpdate;
    private String canDelete;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(Integer parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCanView() {
        return canView;
    }

    public void setCanView(String canView) {
        this.canView = canView;
    }

    public String getCanCreate() {
        return canCreate;
    }

    public void setCanCreate(String canCreate) {
        this.canCreate = canCreate;
    }

    public String getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(String canUpdate) {
        this.canUpdate = canUpdate;
    }

    public String getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(String canDelete) {
        this.canDelete = canDelete;
    }
}
