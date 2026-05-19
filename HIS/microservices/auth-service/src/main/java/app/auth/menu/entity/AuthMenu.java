package app.auth.menu.entity;

import app.auth.common.entity.AuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MENU", schema = "CMH")
public class AuthMenu extends AuditableEntity {

    @Id
    @Column(name = "MENU_ID", nullable = false)
    private Integer menuId;

    @Column(name = "PARENT_ID")
    private Integer parentMenuId;

    @Column(name = "CODE", nullable = false, length = 50)
    private String menuCode;

    @Column(name = "NAME", nullable = false, length = 100)
    private String menuName;

    @Column(name = "PATH", length = 200)
    private String menuPath;

    @Column(name = "ICON", length = 50)
    private String menuIcon;

    @Column(name = "SORT_ORDER", nullable = false)
    private Integer sortOrder;

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private String isActive;

    public AuthMenu() {
    }

    public AuthMenu(Integer menuId,
                    Integer parentMenuId,
                    String menuCode,
                    String menuName,
                    String menuPath,
                    Integer sortOrder) {
        this.menuId = menuId;
        this.parentMenuId = parentMenuId;
        this.menuCode = menuCode;
        this.menuName = menuName;
        this.menuPath = menuPath;
        this.sortOrder = sortOrder;
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
}
