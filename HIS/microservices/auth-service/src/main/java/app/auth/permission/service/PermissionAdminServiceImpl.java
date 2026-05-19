package app.auth.permission.service;

import app.auth.common.dto.AuthUserSearchInfo;
import app.auth.permission.dto.PermissionRoleResponse;
import app.auth.permission.dto.PermissionUserResponse;
import app.auth.permission.dto.RoleMenuPermissionItemRequest;
import app.auth.permission.dto.RoleMenuPermissionResponse;
import app.auth.permission.dto.RoleMenuPermissionUpdateRequest;
import app.auth.permission.dto.UserMenuPermissionItemRequest;
import app.auth.permission.dto.UserMenuPermissionResponse;
import app.auth.permission.dto.UserMenuPermissionUpdateRequest;
import app.auth.permission.entity.AuthRoleMenuPermission;
import app.auth.permission.entity.AuthRoleMenuPermissionId;
import app.auth.permission.entity.AuthUserMenuPermission;
import app.auth.permission.entity.AuthUserMenuPermissionId;
import app.auth.permission.entity.RoleMenuPermissionRow;
import app.auth.permission.entity.UserMenuPermissionRow;
import app.auth.permission.repository.AuthRoleMenuPermissionRepository;
import app.auth.permission.repository.AuthRoleRepository;
import app.auth.permission.repository.AuthUserMenuPermissionRepository;
import app.auth.permission.repository.PermissionAdminRepository;
import app.auth.common.entity.AuthAccount;
import app.auth.common.repository.AuthUserProfileRepository;
import app.auth.register.repository.RegisterAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PermissionAdminServiceImpl implements PermissionAdminService {

    private static final String STATE_INHERIT = "INHERIT";
    private static final String STATE_ALLOW = "ALLOW";
    private static final String STATE_DENY = "DENY";

    private final AuthRoleRepository authRoleRepository;
    private final AuthUserProfileRepository authUserProfileRepository;
    private final RegisterAccountRepository registerAccountRepository;
    private final AuthRoleMenuPermissionRepository authRoleMenuPermissionRepository;
    private final AuthUserMenuPermissionRepository authUserMenuPermissionRepository;
    private final PermissionAdminRepository permissionAdminRepository;

    @Override
    public List<PermissionRoleResponse> getRoles() {
        List<PermissionRoleResponse> roles = new ArrayList<>(authRoleRepository.findRolesInUse());
        roles.sort(this::compareRoleOrder);
        return roles;
    }

    @Override
    public List<PermissionUserResponse> searchUsers(String keyword) {
        String normalizedKeyword = normalizeKeyword(keyword);
        List<AuthUserSearchInfo> users = authUserProfileRepository.searchUsers(normalizedKeyword, 20);

        return users.stream()
                .map(user -> new PermissionUserResponse(
                        user.getUserId(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getRoleCode(),
                        user.getStatus(),
                        user.getDepartmentName()
                ))
                .toList();
    }

    @Override
    public List<RoleMenuPermissionResponse> getRoleMenuPermissions(String roleCode) {
        ensureRoleExists(roleCode);

        List<RoleMenuPermissionRow> rows = permissionAdminRepository.findRoleMenuPermissions(normalizeRoleCode(roleCode));
        return buildRolePermissionTree(rows);
    }

    @Override
    @Transactional
    public void updateRoleMenuPermissions(String roleCode, RoleMenuPermissionUpdateRequest request) {
        String normalizedRoleCode = normalizeRoleCode(roleCode);
        ensureRoleExists(normalizedRoleCode);

        List<RoleMenuPermissionItemRequest> requestedPermissions = emptyIfNull(request == null ? null : request.getPermissions());

        authRoleMenuPermissionRepository.deleteByIdRoleCode(normalizedRoleCode);
        List<AuthRoleMenuPermission> permissionsToSave = new ArrayList<>();

        for (RoleMenuPermissionItemRequest permission : requestedPermissions) {
            if (permission == null || permission.getMenuId() == null) {
                continue;
            }

            String canView = booleanToFlag(permission.getCanView());
            String canCreate = booleanToFlag(permission.getCanCreate());
            String canUpdate = booleanToFlag(permission.getCanUpdate());
            String canDelete = booleanToFlag(permission.getCanDelete());

            if (isAllDenied(canView, canCreate, canUpdate, canDelete)) {
                continue;
            }

            AuthRoleMenuPermission entity = new AuthRoleMenuPermission();
            entity.setId(new AuthRoleMenuPermissionId(normalizedRoleCode, permission.getMenuId()));
            entity.setCanView(canView);
            entity.setCanCreate(canCreate);
            entity.setCanUpdate(canUpdate);
            entity.setCanDelete(canDelete);
            permissionsToSave.add(entity);
        }

        if (!permissionsToSave.isEmpty()) {
            authRoleMenuPermissionRepository.saveAll(permissionsToSave);
        }
    }

    @Override
    public List<UserMenuPermissionResponse> getUserMenuPermissions(String userId) {
        AuthAccount account = findAccountOrThrow(userId);
        List<UserMenuPermissionRow> rows = permissionAdminRepository.findUserMenuPermissions(account.getId(), account.getRole());
        return buildUserPermissionTree(rows);
    }

    @Override
    @Transactional
    public void updateUserMenuPermissions(String userId, UserMenuPermissionUpdateRequest request) {
        AuthAccount account = findAccountOrThrow(userId);
        List<UserMenuPermissionItemRequest> requestedPermissions = emptyIfNull(request == null ? null : request.getPermissions());

        authUserMenuPermissionRepository.deleteByIdUserId(account.getId());
        List<AuthUserMenuPermission> permissionsToSave = new ArrayList<>();

        for (UserMenuPermissionItemRequest permission : requestedPermissions) {
            if (permission == null || permission.getMenuId() == null) {
                continue;
            }

            String canView = stateToFlag(permission.getViewState());
            String canCreate = stateToFlag(permission.getCreateState());
            String canUpdate = stateToFlag(permission.getUpdateState());
            String canDelete = stateToFlag(permission.getDeleteState());

            if (canView == null && canCreate == null && canUpdate == null && canDelete == null) {
                continue;
            }

            AuthUserMenuPermission entity = new AuthUserMenuPermission();
            entity.setId(new AuthUserMenuPermissionId(account.getId(), permission.getMenuId()));
            entity.setCanView(canView);
            entity.setCanCreate(canCreate);
            entity.setCanUpdate(canUpdate);
            entity.setCanDelete(canDelete);
            permissionsToSave.add(entity);
        }

        if (!permissionsToSave.isEmpty()) {
            authUserMenuPermissionRepository.saveAll(permissionsToSave);
        }
    }

    private AuthAccount findAccountOrThrow(String userId) {
        AuthAccount account = registerAccountRepository.findById(userId).orElse(null);
        if (account == null) {
            throw new IllegalArgumentException("AUTH_ACCOUNT_NOT_FOUND");
        }

        return account;
    }

    private void ensureRoleExists(String roleCode) {
        if (!authRoleRepository.existsById(normalizeRoleCode(roleCode))) {
            throw new IllegalArgumentException("AUTH_ROLE_NOT_FOUND");
        }
    }

    private String normalizeRoleCode(String roleCode) {
        if (roleCode == null) {
            return "";
        }

        return roleCode.trim().toUpperCase();
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return "";
        }

        return keyword.trim();
    }

    private List<RoleMenuPermissionResponse> buildRolePermissionTree(List<RoleMenuPermissionRow> rows) {
        Map<Integer, RoleMenuPermissionResponse> responseMap = new LinkedHashMap<>();

        for (RoleMenuPermissionRow row : rows) {
            RoleMenuPermissionResponse response = new RoleMenuPermissionResponse();
            response.setMenuId(row.getMenuId());
            response.setParentMenuId(row.getParentMenuId());
            response.setMenuCode(row.getMenuCode());
            response.setMenuName(row.getMenuName());
            response.setMenuPath(row.getMenuPath());
            response.setMenuIcon(row.getMenuIcon());
            response.setSortOrder(row.getSortOrder());
            response.setIsActive(row.getIsActive());
            response.setCanView(isAllowed(row.getCanView()));
            response.setCanCreate(isAllowed(row.getCanCreate()));
            response.setCanUpdate(isAllowed(row.getCanUpdate()));
            response.setCanDelete(isAllowed(row.getCanDelete()));
            responseMap.put(response.getMenuId(), response);
        }

        List<RoleMenuPermissionResponse> rootMenus = new ArrayList<>();

        for (RoleMenuPermissionResponse response : responseMap.values()) {
            Integer parentMenuId = response.getParentMenuId();
            if (parentMenuId == null) {
                rootMenus.add(response);
                continue;
            }

            RoleMenuPermissionResponse parentMenu = responseMap.get(parentMenuId);
            if (parentMenu == null) {
                rootMenus.add(response);
                continue;
            }

            parentMenu.getChildren().add(response);
        }

        sortRoleMenus(rootMenus);
        return rootMenus;
    }

    private List<UserMenuPermissionResponse> buildUserPermissionTree(List<UserMenuPermissionRow> rows) {
        Map<Integer, UserMenuPermissionResponse> responseMap = new LinkedHashMap<>();

        for (UserMenuPermissionRow row : rows) {
            UserMenuPermissionResponse response = new UserMenuPermissionResponse();
            response.setMenuId(row.getMenuId());
            response.setParentMenuId(row.getParentMenuId());
            response.setMenuCode(row.getMenuCode());
            response.setMenuName(row.getMenuName());
            response.setMenuPath(row.getMenuPath());
            response.setMenuIcon(row.getMenuIcon());
            response.setSortOrder(row.getSortOrder());
            response.setIsActive(row.getIsActive());
            response.setRoleCanView(isAllowed(row.getRoleCanView()));
            response.setRoleCanCreate(isAllowed(row.getRoleCanCreate()));
            response.setRoleCanUpdate(isAllowed(row.getRoleCanUpdate()));
            response.setRoleCanDelete(isAllowed(row.getRoleCanDelete()));
            response.setViewState(flagToState(row.getUserCanView()));
            response.setCreateState(flagToState(row.getUserCanCreate()));
            response.setUpdateState(flagToState(row.getUserCanUpdate()));
            response.setDeleteState(flagToState(row.getUserCanDelete()));
            response.setFinalCanView(isAllowed(row.getFinalCanView()));
            response.setFinalCanCreate(isAllowed(row.getFinalCanCreate()));
            response.setFinalCanUpdate(isAllowed(row.getFinalCanUpdate()));
            response.setFinalCanDelete(isAllowed(row.getFinalCanDelete()));
            responseMap.put(response.getMenuId(), response);
        }

        List<UserMenuPermissionResponse> rootMenus = new ArrayList<>();

        for (UserMenuPermissionResponse response : responseMap.values()) {
            Integer parentMenuId = response.getParentMenuId();
            if (parentMenuId == null) {
                rootMenus.add(response);
                continue;
            }

            UserMenuPermissionResponse parentMenu = responseMap.get(parentMenuId);
            if (parentMenu == null) {
                rootMenus.add(response);
                continue;
            }

            parentMenu.getChildren().add(response);
        }

        sortUserMenus(rootMenus);
        return rootMenus;
    }

    private void sortRoleMenus(List<RoleMenuPermissionResponse> menus) {
        menus.sort(roleMenuComparator());

        for (RoleMenuPermissionResponse menu : menus) {
            if (!menu.getChildren().isEmpty()) {
                sortRoleMenus(menu.getChildren());
            }
        }
    }

    private void sortUserMenus(List<UserMenuPermissionResponse> menus) {
        menus.sort(userMenuComparator());

        for (UserMenuPermissionResponse menu : menus) {
            if (!menu.getChildren().isEmpty()) {
                sortUserMenus(menu.getChildren());
            }
        }
    }

    private Comparator<RoleMenuPermissionResponse> roleMenuComparator() {
        return Comparator
                .comparing(RoleMenuPermissionResponse::getSortOrder, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(RoleMenuPermissionResponse::getMenuId, Comparator.nullsLast(Integer::compareTo));
    }

    private Comparator<UserMenuPermissionResponse> userMenuComparator() {
        return Comparator
                .comparing(UserMenuPermissionResponse::getSortOrder, Comparator.nullsLast(Integer::compareTo))
                .thenComparing(UserMenuPermissionResponse::getMenuId, Comparator.nullsLast(Integer::compareTo));
    }

    private String booleanToFlag(Boolean value) {
        if (Boolean.TRUE.equals(value)) {
            return "Y";
        }

        return "N";
    }

    private boolean isAllowed(String value) {
        return "Y".equalsIgnoreCase(value);
    }

    private boolean isAllDenied(String canView, String canCreate, String canUpdate, String canDelete) {
        return "N".equals(canView)
                && "N".equals(canCreate)
                && "N".equals(canUpdate)
                && "N".equals(canDelete);
    }

    private String stateToFlag(String state) {
        String normalizedState = normalizeState(state);

        if (STATE_ALLOW.equals(normalizedState)) {
            return "Y";
        }

        if (STATE_DENY.equals(normalizedState)) {
            return "N";
        }

        return null;
    }

    private String flagToState(String flag) {
        if ("Y".equalsIgnoreCase(flag)) {
            return STATE_ALLOW;
        }

        if ("N".equalsIgnoreCase(flag)) {
            return STATE_DENY;
        }

        return STATE_INHERIT;
    }

    private String normalizeState(String state) {
        if (state == null || state.trim().isEmpty()) {
            return STATE_INHERIT;
        }

        String normalized = state.trim().toUpperCase();
        if (STATE_ALLOW.equals(normalized) || STATE_DENY.equals(normalized)) {
            return normalized;
        }

        return STATE_INHERIT;
    }

    private int compareRoleOrder(PermissionRoleResponse left, PermissionRoleResponse right) {
        return Integer.compare(roleOrder(left.getRoleCode()), roleOrder(right.getRoleCode()));
    }

    private int roleOrder(String roleCode) {
        if ("ADMIN".equals(roleCode)) {
            return 1;
        }
        if ("DOCTOR".equals(roleCode)) {
            return 2;
        }
        if ("NURSE".equals(roleCode)) {
            return 3;
        }
        if ("RECEPTION".equals(roleCode)) {
            return 4;
        }
        if ("STAFF".equals(roleCode)) {
            return 5;
        }

        return 99;
    }

    private <T> List<T> emptyIfNull(List<T> values) {
        if (values == null) {
            return new ArrayList<>();
        }

        return values;
    }
}
