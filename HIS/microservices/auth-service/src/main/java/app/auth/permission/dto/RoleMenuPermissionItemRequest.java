package app.auth.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "역할이 메뉴 하나를 어떻게 사용할 수 있는지 저장하는 정보입니다.")
public class RoleMenuPermissionItemRequest {

    @Schema(description = "권한을 바꿀 메뉴 번호입니다.")
    private Integer menuId;

    @Schema(description = "볼 수 있게 할지 여부입니다.")
    private Boolean canView;

    @Schema(description = "새로 만들 수 있게 할지 여부입니다.")
    private Boolean canCreate;

    @Schema(description = "수정할 수 있게 할지 여부입니다.")
    private Boolean canUpdate;

    @Schema(description = "삭제할 수 있게 할지 여부입니다.")
    private Boolean canDelete;

    public RoleMenuPermissionItemRequest() {
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Boolean getCanView() {
        return canView;
    }

    public void setCanView(Boolean canView) {
        this.canView = canView;
    }

    public Boolean getCanCreate() {
        return canCreate;
    }

    public void setCanCreate(Boolean canCreate) {
        this.canCreate = canCreate;
    }

    public Boolean getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(Boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public Boolean getCanDelete() {
        return canDelete;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }
}
