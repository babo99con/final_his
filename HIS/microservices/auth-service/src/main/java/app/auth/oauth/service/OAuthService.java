package app.auth.oauth.service;

import app.auth.login.dto.LoginResult;
import app.auth.oauth.entity.OAuthProfile;

public interface OAuthService {
    
    OAuthProfile mapProfile(String provider, java.util.Map<String, Object> attributes);
    
    String issueSocialVerificationToken(String provider, String providerId, String name, String email);
    
    LoginResult loginOrRegisterOAuth(String provider, String providerId, String email, String name);
    
    String normalizeProvider(String provider);
}