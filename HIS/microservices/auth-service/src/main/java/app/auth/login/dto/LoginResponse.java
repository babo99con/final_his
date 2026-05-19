package app.auth.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String accessToken;
    private String tokenType;
    private long expiresIn;
    private LoginUserDto user;
    private boolean passwordChangeRequired;
}