package app.auth.register.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PendingRegisterRequestDto {

    private final String accountId;
    private final String username;
    private final String fullName;
    private final String role;
    private final String departmentId;
    private final String departmentName;
    private final String phone;
    private final String email;
    private final String status;
    private final LocalDateTime createdAt;
}
