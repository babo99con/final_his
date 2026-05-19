package app.auth.register.mapper;

import app.auth.common.entity.AuthAccount;
import app.auth.register.dto.RegisterRequest;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

    public AuthAccount toPendingAccount(
            RegisterRequest request,
            String passwordHash,
            String roleCode
    ) {
        AuthAccount account = new AuthAccount();
        account.setUsername(request.getUsername().trim().toLowerCase());
        account.setRole(roleCode);
        account.setPasswordHash(passwordHash);
        return account;
    }
}
