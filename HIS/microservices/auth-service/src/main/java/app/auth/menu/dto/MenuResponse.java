package app.auth.menu.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "화면에 보여줄 메뉴 정보입니다.")
public class MenuResponse {

    @Schema(description = "메뉴 번호입니다.")
    private Integer menuId;

    @Schema(description = "상위 메뉴 번호입니다. 최상위 메뉴면 비어 있을 수 있습니다.")
    private Integer parentMenuId;

    @Schema(description = "메뉴를 구분하는 코드입니다.")
    private String menuCode;

    @Schema(description = "화면에 보이는 메뉴 이름입니다.")
    private String menuName;

    @Schema(description = "메뉴를 눌렀을 때 이동할 주소입니다.")
    private String menuPath;

    @Schema(description = "메뉴가 보이는 순서입니다.")
    private Integer sortOrder;

    @Schema(description = "하위 메뉴 목록입니다.")
    private List<MenuResponse> children = new ArrayList<>();

    public MenuResponse() {
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

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<MenuResponse> getChildren() {
        return children;
    }

    public void setChildren(List<MenuResponse> children) {
        this.children = children;
    }
}
