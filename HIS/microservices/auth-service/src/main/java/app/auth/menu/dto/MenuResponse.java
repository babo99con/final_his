package app.auth.menu.dto;

import java.util.ArrayList;
import java.util.List;

public class MenuResponse {

    private Integer menuId;
    private Integer parentMenuId;
    private String menuCode;
    private String menuName;
    private String menuPath;
    private Integer sortOrder;
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
