package app.auth.oauth.service;

import app.auth.oauth.entity.SocialVerification;
import app.auth.oauth.repository.OAuthAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class OAuthVerificationTokenService {

    private final OAuthAccountRepository oAuthRepository;

    public OAuthVerificationTokenService(OAuthAccountRepository oAuthRepository) {
        this.oAuthRepository = oAuthRepository;
    }

    public String issue(String provider, String providerId, String name, String email) {
        SocialVerification verification = new SocialVerification(provider, providerId, name, email);
        return oAuthRepository.issueSocialVerificationToken(verification);
    }

    public SocialVerification read(String token) {
        return oAuthRepository.readSocialVerification(token);
    }

    public void consume(String token) {
        oAuthRepository.consumeSocialVerification(token);
    }
}