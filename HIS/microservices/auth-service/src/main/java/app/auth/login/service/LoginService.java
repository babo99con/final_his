package app.auth.login.service;

import app.auth.login.dto.LoginRequest;
import app.auth.login.dto.LoginResult;

public interface LoginService {

    LoginResult login(LoginRequest request);

    LoginResult refresh(String refreshToken);
}