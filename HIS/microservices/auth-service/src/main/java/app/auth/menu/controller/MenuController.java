package app.auth.menu.controller;

import app.auth.menu.dto.MenuResponse;
import app.auth.menu.service.MenuService;
import com.hms.util.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getMenus(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }

        List<MenuResponse> menus = menuService.getMenus(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(menus));
    }
}
