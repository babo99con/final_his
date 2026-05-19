package app.auth.oauth.entity;

public record OAuthProfile(String provider, String providerId, String email, String name) {
}
