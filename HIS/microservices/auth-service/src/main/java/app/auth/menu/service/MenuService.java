package app.auth.menu.service;

import app.auth.menu.dto.MenuResponse;

import java.util.List;
import java.util.Set;

public interface MenuService {

    List<MenuResponse> getMenus(String username);

    Set<String> getAuthorizedMenuPaths(String username);
}
