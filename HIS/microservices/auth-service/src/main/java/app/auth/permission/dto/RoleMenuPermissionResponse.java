package app.auth.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "역할 하나가 메뉴를 어떻게 사용할 수 있는지 보여주는 정보입니다.")
public class RoleMenuPermissionResponse {

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

    @Schema(description = "이 메뉴를 볼 수 있는지 여부입니다.")
    private boolean canView;

    @Schema(description = "이 메뉴에서 새로 만들 수 있는지 여부입니다.")
    private boolean canCreate;

    @Schema(description = "이 메뉴에서 수정할 수 있는지 여부입니다.")
    private boolean canUpdate;

    @Schema(description = "이 메뉴에서 삭제할 수 있는지 여부입니다.")
    private boolean canDelete;

    @Schema(description = "하위 메뉴 목록입니다.")
    private List<RoleMenuPermissionResponse> children = new ArrayList<>();

    public RoleMenuPermissionResponse() {
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

    public boolean isCanView() {
        return canView;
    }

    public void setCanView(boolean canView) {
        this.canView = canView;
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public boolean isCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public List<RoleMenuPermissionResponse> getChildren() {
        return children;
    }

    public void setChildren(List<RoleMenuPermissionResponse> children) {
        this.children = children;
    }
}
