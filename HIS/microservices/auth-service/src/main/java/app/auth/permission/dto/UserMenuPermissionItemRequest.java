package app.auth.permission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 한 명에게 메뉴 하나의 예외 권한을 저장하는 정보입니다.")
public class UserMenuPermissionItemRequest {

    @Schema(description = "권한을 바꿀 메뉴 번호입니다.")
    private Integer menuId;

    @Schema(description = "보기 권한 상태입니다. INHERIT은 역할 권한 그대로, ALLOW는 허용, DENY는 차단입니다.")
    private String viewState;

    @Schema(description = "만들기 권한 상태입니다. INHERIT, ALLOW, DENY 중 하나를 씁니다.")
    private String createState;

    @Schema(description = "수정 권한 상태입니다. INHERIT, ALLOW, DENY 중 하나를 씁니다.")
    private String updateState;

    @Schema(description = "삭제 권한 상태입니다. INHERIT, ALLOW, DENY 중 하나를 씁니다.")
    private String deleteState;

    public UserMenuPermissionItemRequest() {
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
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
}
