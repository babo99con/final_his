package app.auth.menu.controller;

import app.auth.menu.dto.MenuResponse;
import app.auth.menu.service.MenuService;
import com.hms.util.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "메뉴", description = "로그인한 사용자가 화면에서 볼 수 있는 메뉴를 알려주는 API입니다.")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/api/menus")
    @Operation(
            summary = "내가 볼 수 있는 메뉴 조회",
            description = "로그인한 사용자의 권한을 기준으로 화면에 보여줄 메뉴 목록을 가져옵니다."
    )
    public ResponseEntity<ApiResponse<List<MenuResponse>>> getMenus(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("AUTH_UNAUTHORIZED"));
        }

        List<MenuResponse> menus = menuService.getMenus(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(menus));
    }
}
