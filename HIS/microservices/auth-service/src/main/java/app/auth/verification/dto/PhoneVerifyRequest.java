package app.auth.verification.dto;

import lombok.Data;

@Data
public class PhoneVerifyRequest {
    private String phone;
    private String code;
}
