package app.auth.session.service;

public interface SessionService {

    void startSession(String username, String sessionId);

    boolean isSessionAliveAndTouch(String username, String sessionId);

    void invalidateSession(String username, String sessionId);

    void invalidateUserSessions(String username);
}
