package app.auth.register.service;

import app.auth.common.PasswordHashUtil;
import app.auth.common.entity.AuthAccount;
import app.auth.register.dto.PendingRegisterRequestDto;
import app.auth.register.dto.RegisterRequest;
import app.auth.register.mapper.RegisterMapper;
import app.auth.register.repository.RegisterAccountRepository;
import app.auth.register.repository.RegisterEmployeeRepository;
import app.auth.register.validator.RegisterValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final RegisterAccountRepository registerAccountRepository;
    private final RegisterEmployeeRepository registerEmployeeRepository;
    private final RegisterMapper registerMapper;
    private final RegisterValidator registerValidator;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        registerValidator.validateUsername(request.getUsername());
        registerValidator.validatePassword(request.getPassword());

        String username = normalizeUsername(request.getUsername());
        validateUsernameAvailable(username);

        String fullName = trim(request.getFullName());
        String email = trim(request.getEmail());
        String phone = trim(request.getPhone());

        validateBasicRegisterFields(fullName, email, phone);
        email = normalizeEmail(email);
        phone = normalizePhone(phone);

        String roleCode = normalizeRoleCode(request.getRole());
        registerValidator.validateRole(roleCode);

        String departmentId = normalizeDepartment(request.getDepartment(), roleCode);
        validateDepartmentExists(departmentId);

        String staffId = registerEmployeeRepository.generateStaffId(roleCode, departmentId);
        String passwordHash = PasswordHashUtil.hashNew(request.getPassword().trim());
        AuthAccount account = registerMapper.toPendingAccount(request, passwordHash, roleCode);
        account.setId(staffId);
        account.setUsername(staffId);
        registerAccountRepository.save(account);
        registerEmployeeRepository.insertEmployee(staffId, departmentId, fullName, phone, email, "PENDING_APPROVAL");
    }

    @Override
    public boolean isUsernameAvailable(String username) {
        registerValidator.validateUsername(username);
        String normalized = normalizeUsername(username);
        return registerAccountRepository.countByUsernameIgnoreCase(normalized) == 0;
    }

    @Override
    public List<PendingRegisterRequestDto> readPendingRegisterRequests() {
        return registerEmployeeRepository.readPendingRegisterRequests();
    }

    @Override
    @Transactional
    public void reviewRegisterRequest(String accountId, boolean approve) {
        AuthAccount account = registerAccountRepository.findById(accountId).orElse(null);

        if (account == null) {
            throw new IllegalArgumentException("AUTH_REGISTER_REQUEST_NOT_FOUND");
        }

        String employeeStatus = registerEmployeeRepository.readEmployeeStatus(accountId);
        if (!"PENDING_APPROVAL".equals(employeeStatus)) {
            throw new IllegalArgumentException("AUTH_REGISTER_REQUEST_INVALID_STATE");
        }

        if (approve) {
            registerEmployeeRepository.updateEmployeeStatus(account.getId(), "ACTIVE");
        } else {
            registerEmployeeRepository.updateEmployeeStatus(account.getId(), "REJECTED_SIGNUP");
        }
    }

    private void validateUsernameAvailable(String username) {
        if (registerAccountRepository.countByUsernameIgnoreCase(username) > 0) {
            throw new IllegalArgumentException("AUTH_USERNAME_TAKEN");
        }
    }

    private void validateBasicRegisterFields(String fullName, String email, String phone) {
        if (!StringUtils.hasText(fullName)) {
            throw new IllegalArgumentException("AUTH_FULL_NAME_REQUIRED");
        }

        if (!StringUtils.hasText(phone)) {
            throw new IllegalArgumentException("AUTH_PHONE_REQUIRED");
        }

        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("AUTH_EMAIL_REQUIRED");
        }
    }

    private String normalizeUsername(String username) {
        if (username == null) {
            return "";
        }

        return username.trim().toLowerCase();
    }

    private String normalizeEmail(String rawEmail) {
        String email = trim(rawEmail).toLowerCase(Locale.ROOT);
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("AUTH_EMAIL_INVALID");
        }
        return email;
    }

    private String normalizePhone(String rawPhone) {
        String phone = trim(rawPhone).replaceAll("[^0-9]", "");
        if (phone.length() < 9 || phone.length() > 11) {
            throw new IllegalArgumentException("AUTH_PHONE_INVALID");
        }
        return phone;
    }

    private String normalizeRoleCode(String roleCode) {
        String normalized = trim(roleCode).toUpperCase(Locale.ROOT);
        if (!StringUtils.hasText(normalized)) {
            return "STAFF";
        }
        return normalized;
    }

    private String normalizeDepartment(String department, String roleCode) {
        String normalized = trim(department).toUpperCase(Locale.ROOT);

        if (!StringUtils.hasText(normalized)) {
            return defaultDepartment(roleCode);
        }

        return switch (normalized) {
            case "DEPT_MED", "INTERNAL_MEDICINE", "ORTHOPEDICS" -> "DEPT_MED";
            case "DEPT_NURSING", "NURSING", "NURSING_DEPARTMENT" -> "DEPT_NURSING";
            case "DEPT_DIAG", "COMMON", "RADIOLOGY", "LAB", "RECEPTION" -> "DEPT_DIAG";
            default -> normalized;
        };
    }

    private String defaultDepartment(String roleCode) {
        if ("DOCTOR".equals(roleCode)) {
            return "DEPT_MED";
        }
        if ("NURSE".equals(roleCode)) {
            return "DEPT_NURSING";
        }
        return "DEPT_DIAG";
    }

    private void validateDepartmentExists(String departmentId) {
        if (!registerEmployeeRepository.existsDepartment(departmentId)) {
            throw new IllegalArgumentException("AUTH_DEPARTMENT_INVALID");
        }
    }

    private String trim(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }
}
