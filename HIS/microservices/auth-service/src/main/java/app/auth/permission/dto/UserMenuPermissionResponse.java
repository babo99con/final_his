package app.auth.permission.dto;

import java.util.ArrayList;
import java.util.List;

public class UserMenuPermissionResponse {

    private Integer menuId;
    private Integer parentMenuId;
    private String menuCode;
    private String menuName;
    private String menuPath;
    private String menuIcon;
    private Integer sortOrder;
    private String isActive;
    private boolean roleCanView;
    private boolean roleCanCreate;
    private boolean roleCanUpdate;
    private boolean roleCanDelete;
    private String viewState;
    private String createState;
    private String updateState;
    private String deleteState;
    private boolean finalCanView;
    private boolean finalCanCreate;
    private boolean finalCanUpdate;
    private boolean finalCanDelete;
    private List<UserMenuPermissionResponse> children = new ArrayList<>();

    public UserMenuPermissionResponse() {
    }

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

    public boolean isRoleCanView() {
        return roleCanView;
    }

    public void setRoleCanView(boolean roleCanView) {
        this.roleCanView = roleCanView;
    }

    public boolean isRoleCanCreate() {
        return roleCanCreate;
    }

    public void setRoleCanCreate(boolean roleCanCreate) {
        this.roleCanCreate = roleCanCreate;
    }

    public boolean isRoleCanUpdate() {
        return roleCanUpdate;
    }

    public void setRoleCanUpdate(boolean roleCanUpdate) {
        this.roleCanUpdate = roleCanUpdate;
    }

    public boolean isRoleCanDelete() {
        return roleCanDelete;
    }

    public void setRoleCanDelete(boolean roleCanDelete) {
        this.roleCanDelete = roleCanDelete;
    }

    public String getViewState() {
        return viewState;
    }

    public void setViewState(String viewState) {
        this.viewState = viewState;
    }

    public String getCreateState() {
        return createState;
    }

    public void setCreateState(String createState) {
        this.createState = createState;
    }

    public String getUpdateState() {
        return updateState;
    }

    public void setUpdateState(String updateState) {
        this.updateState = updateState;
    }

    public String getDeleteState() {
        return deleteState;
    }

    public void setDeleteState(String deleteState) {
        this.deleteState = deleteState;
    }

    public boolean isFinalCanView() {
        return finalCanView;
    }

    public void setFinalCanView(boolean finalCanView) {
        this.finalCanView = finalCanView;
    }

    public boolean isFinalCanCreate() {
        return finalCanCreate;
    }

    public void setFinalCanCreate(boolean finalCanCreate) {
        this.finalCanCreate = finalCanCreate;
    }

    public boolean isFinalCanUpdate() {
        return finalCanUpdate;
    }

    public void setFinalCanUpdate(boolean finalCanUpdate) {
        this.finalCanUpdate = finalCanUpdate;
    }

    public boolean isFinalCanDelete() {
        return finalCanDelete;
    }

    public void setFinalCanDelete(boolean finalCanDelete) {
        this.finalCanDelete = finalCanDelete;
    }

    public List<UserMenuPermissionResponse> getChildren() {
        return children;
    }

    public void setChildren(List<UserMenuPermissionResponse> children) {
        this.children = children;
    }
}
