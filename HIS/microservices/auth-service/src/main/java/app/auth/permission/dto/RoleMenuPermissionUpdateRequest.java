package app.auth.permission.dto;

import java.util.ArrayList;
import java.util.List;

public class RoleMenuPermissionUpdateRequest {

    private List<RoleMenuPermissionItemRequest> permissions = new ArrayList<>();

    public RoleMenuPermissionUpdateRequest() {
    }

    public List<RoleMenuPermissionItemRequest> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<RoleMenuPermissionItemRequest> permissions) {
        this.permissions = permissions;
    }
}
