package app.auth.menu.service;

import app.auth.menu.dto.MenuResponse;
import app.auth.menu.entity.AuthMenu;
import app.auth.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public List<MenuResponse> getMenus(String username) {
        List<AuthMenu> authorizedMenus = menuRepository.findMenusByUsername(username);
        return buildMenuTree(authorizedMenus);
    }

    private List<MenuResponse> buildMenuTree(List<AuthMenu> authorizedMenus) {
        Map<Integer, MenuResponse> menuMap = new LinkedHashMap<>();

        for (AuthMenu authorizedMenu : authorizedMenus) {
            MenuResponse menuResponse = createMenuResponse(authorizedMenu);
            menuMap.put(menuResponse.getMenuId(), menuResponse);
        }

        List<MenuResponse> rootMenus = new ArrayList<>();

        for (MenuResponse menuResponse : menuMap.values()) {
            Integer parentMenuId = menuResponse.getParentMenuId();

            if (parentMenuId == null) {
                rootMenus.add(menuResponse);
                continue;
            }

            MenuResponse parentMenu = menuMap.get(parentMenuId);
            if (parentMenu == null) {
                rootMenus.add(menuResponse);
                continue;
            }

            parentMenu.getChildren().add(menuResponse);
        }

        sortMenus(rootMenus);
        return rootMenus;
    }

    @Override
    public Set<String> getAuthorizedMenuPaths(String username) {
        return menuRepository.findMenuPathsByUsername(username).stream()
                .filter(path -> path != null && !path.isBlank())
                .map(path -> path.trim().toLowerCase())
                .collect(Collectors.toSet());
    }

    private MenuResponse createMenuResponse(AuthMenu authorizedMenu) {
        MenuResponse menuResponse = new MenuResponse();
        menuResponse.setMenuId(authorizedMenu.getMenuId());
        menuResponse.setParentMenuId(authorizedMenu.getParentMenuId());
        menuResponse.setMenuCode(authorizedMenu.getMenuCode());
        menuResponse.setMenuName(authorizedMenu.getMenuName());
        menuResponse.setMenuPath(authorizedMenu.getMenuPath());
        menuResponse.setSortOrder(authorizedMenu.getSortOrder());
        return menuResponse;
    }

    private void sortMenus(List<MenuResponse> menus) {
        menus.sort(menuComparator());

        for (MenuResponse menu : menus) {
            if (!menu.getChildren().isEmpty()) {
                sortMenus(menu.getChildren());
            }
        }
    }

    private Comparator<MenuResponse> menuComparator() {
        return Comparator
                .comparing(MenuResponse::getSortOrder, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(MenuResponse::getMenuId, Comparator.nullsLast(Integer::compareTo));
    }
}
