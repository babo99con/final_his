package app.auth.oauth.entity;

public record SocialVerification(String provider, String providerId, String name, String email) {
}
