package app.auth.permission.entity;

public class UserMenuPermissionRow {

    private Integer menuId;
    private Integer parentMenuId;
    private String menuCode;
    private String menuName;
    private String menuPath;
    private String menuIcon;
    private Integer sortOrder;
    private String isActive;
    private String roleCanView;
    private String roleCanCreate;
    private String roleCanUpdate;
    private String roleCanDelete;
    private String userCanView;
    private String userCanCreate;
    private String userCanUpdate;
    private String userCanDelete;
    private String finalCanView;
    private String finalCanCreate;
    private String finalCanUpdate;
    private String finalCanDelete;

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

    public String getRoleCanView() {
        return roleCanView;
    }

    public void setRoleCanView(String roleCanView) {
        this.roleCanView = roleCanView;
    }

    public String getRoleCanCreate() {
        return roleCanCreate;
    }

    public void setRoleCanCreate(String roleCanCreate) {
        this.roleCanCreate = roleCanCreate;
    }

    public String getRoleCanUpdate() {
        return roleCanUpdate;
    }

    public void setRoleCanUpdate(String roleCanUpdate) {
        this.roleCanUpdate = roleCanUpdate;
    }

    public String getRoleCanDelete() {
        return roleCanDelete;
    }

    public void setRoleCanDelete(String roleCanDelete) {
        this.roleCanDelete = roleCanDelete;
    }

    public String getUserCanView() {
        return userCanView;
    }

    public void setUserCanView(String userCanView) {
        this.userCanView = userCanView;
    }

    public String getUserCanCreate() {
        return userCanCreate;
    }

    public void setUserCanCreate(String userCanCreate) {
        this.userCanCreate = userCanCreate;
    }

    public String getUserCanUpdate() {
        return userCanUpdate;
    }

    public void setUserCanUpdate(String userCanUpdate) {
        this.userCanUpdate = userCanUpdate;
    }

    public String getUserCanDelete() {
        return userCanDelete;
    }

    public void setUserCanDelete(String userCanDelete) {
        this.userCanDelete = userCanDelete;
    }

    public String getFinalCanView() {
        return finalCanView;
    }

    public void setFinalCanView(String finalCanView) {
        this.finalCanView = finalCanView;
    }

    public String getFinalCanCreate() {
        return finalCanCreate;
    }

    public void setFinalCanCreate(String finalCanCreate) {
        this.finalCanCreate = finalCanCreate;
    }

    public String getFinalCanUpdate() {
        return finalCanUpdate;
    }

    public void setFinalCanUpdate(String finalCanUpdate) {
        this.finalCanUpdate = finalCanUpdate;
    }

    public String getFinalCanDelete() {
        return finalCanDelete;
    }

    public void setFinalCanDelete(String finalCanDelete) {
        this.finalCanDelete = finalCanDelete;
    }
}
