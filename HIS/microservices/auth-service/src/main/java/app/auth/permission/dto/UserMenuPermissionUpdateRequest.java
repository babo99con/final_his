package app.auth.permission.dto;

import java.util.ArrayList;
import java.util.List;

public class UserMenuPermissionUpdateRequest {

    private List<UserMenuPermissionItemRequest> permissions = new ArrayList<>();

    public UserMenuPermissionUpdateRequest() {
    }

    public List<UserMenuPermissionItemRequest> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<UserMenuPermissionItemRequest> permissions) {
        this.permissions = permissions;
    }
}
