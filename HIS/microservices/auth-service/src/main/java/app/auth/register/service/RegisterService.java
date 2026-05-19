package app.auth.register.service;

import app.auth.register.dto.PendingRegisterRequestDto;
import app.auth.register.dto.RegisterRequest;

import java.util.List;

public interface RegisterService {

    void register(RegisterRequest request);

    boolean isUsernameAvailable(String username);

    List<PendingRegisterRequestDto> readPendingRegisterRequests();

    void reviewRegisterRequest(String accountId, boolean approve);
}
