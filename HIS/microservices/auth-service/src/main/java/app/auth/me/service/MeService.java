package app.auth.me.service;

import app.auth.common.dto.AuthUserInfo;
import app.auth.me.dto.ChangePasswordRequest;

public interface MeService {

    AuthUserInfo getCurrentUserInfo(String username);

    void changeMyPassword(String username, ChangePasswordRequest request);
}
