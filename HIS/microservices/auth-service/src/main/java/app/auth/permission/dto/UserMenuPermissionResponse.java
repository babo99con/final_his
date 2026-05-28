package app.auth.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "사용자 한 명이 메뉴를 실제로 어떻게 사용할 수 있는지 보여주는 정보입니다.")
public class UserMenuPermissionResponse {

    @Schema(description = "메뉴 번호입니다.")
    private Integer menuId;

    @Schema(description = "상위 메뉴 번호입니다.")
    private Integer parentMenuId;

    @Schema(description = "메뉴 코드입니다.")
    private String menuCode;

    @Schema(description = "화면에 보이는 메뉴 이름입니다.")
    private String menuName;

    @Schema(description = "메뉴를 눌렀을 때 이동할 주소입니다.")
    private String menuPath;

    @Schema(description = "메뉴 아이콘 이름입니다.")
    private String menuIcon;

    @Schema(description = "메뉴가 보이는 순서입니다.")
    private Integer sortOrder;

    @Schema(description = "메뉴가 현재 사용 중인지 표시합니다. Y이면 사용 중입니다.")
    private String isActive;

    @Schema(description = "역할 기준으로 볼 수 있는지 여부입니다.")
    private boolean roleCanView;

    @Schema(description = "역할 기준으로 새로 만들 수 있는지 여부입니다.")
    private boolean roleCanCreate;

    @Schema(description = "역할 기준으로 수정할 수 있는지 여부입니다.")
    private boolean roleCanUpdate;

    @Schema(description = "역할 기준으로 삭제할 수 있는지 여부입니다.")
    private boolean roleCanDelete;

    @Schema(description = "개인별 보기 권한 상태입니다. INHERIT은 역할 권한을 그대로 따른다는 뜻입니다.")
    private String viewState;

    @Schema(description = "개인별 만들기 권한 상태입니다. ALLOW는 허용, DENY는 차단입니다.")
    private String createState;

    @Schema(description = "개인별 수정 권한 상태입니다. ALLOW는 허용, DENY는 차단입니다.")
    private String updateState;

    @Schema(description = "개인별 삭제 권한 상태입니다. ALLOW는 허용, DENY는 차단입니다.")
    private String deleteState;

    @Schema(description = "역할 권한과 개인 권한을 합쳐 최종적으로 볼 수 있는지 여부입니다.")
    private boolean finalCanView;

    @Schema(description = "최종적으로 새로 만들 수 있는지 여부입니다.")
    private boolean finalCanCreate;

    @Schema(description = "최종적으로 수정할 수 있는지 여부입니다.")
    private boolean finalCanUpdate;

    @Schema(description = "최종적으로 삭제할 수 있는지 여부입니다.")
    private boolean finalCanDelete;

    @Schema(description = "하위 메뉴 목록입니다.")
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
