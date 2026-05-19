package app.auth.register.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterContactVerifyRequest {
    private String value;
    private String code;
}
