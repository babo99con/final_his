package app.auth.session.service;

import javax.servlet.http.HttpServletResponse;

public interface SessionService {

    String ACCESS_TOKEN_COOKIE = "his_access_token";
    String REFRESH_TOKEN_COOKIE = "his_refresh_token";

    void addAuthCookie(HttpServletResponse response, String token);

    void clearAuthCookie(HttpServletResponse response);

    void addRefreshCookie(HttpServletResponse response, String token);

    void clearRefreshCookie(HttpServletResponse response);

    void startSession(String username, String sid, String accessTokenJti, String refreshTokenJti);

    boolean isAccessTokenAliveAndTouch(String username, String sid, String accessTokenJti);

    boolean isRefreshTokenAlive(String username, String sid, String refreshTokenJti);

    void rotateSessionTokens(String username, String sid, String accessTokenJti, String refreshTokenJti);

    void invalidateSession(String username);
}