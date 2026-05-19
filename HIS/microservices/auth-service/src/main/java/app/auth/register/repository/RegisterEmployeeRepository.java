package app.auth.register.repository;

import app.auth.register.dto.PendingRegisterRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.List;
import java.util.Set;

@Repository
@AllArgsConstructor
public class RegisterEmployeeRepository {

    private static final Set<String> VALID_DEPARTMENT_CODES = Set.of(
            "DEPT_MED",
            "DEPT_NURSING",
            "DEPT_DIAG"
    );

    private final JdbcTemplate jdbcTemplate;

    public boolean existsDepartment(String deptId) {
        if (deptId == null) {
            return false;
        }

        return VALID_DEPARTMENT_CODES.contains(deptId.trim().toUpperCase(Locale.ROOT));
    }

    public List<PendingRegisterRequestDto> readPendingRegisterRequests() {
        return jdbcTemplate.query(
                """
                SELECT
                    a.ID AS accountId,
                    a.LOGIN_ID AS username,
                    e.NAME AS fullName,
                    a.ROLE_CODE AS roleCode,
                    e.STATUS AS status,
                    e.CREATED_AT AS createdAt,
                    e.DEPT_ID AS departmentId,
                    e.PHONE AS phone,
                    e.EMAIL AS email
                FROM CMH.AUTH_USER a
                JOIN JCH.EMPLOYEE e ON e.STAFF_ID = a.ID
                WHERE e.STATUS = 'PENDING_APPROVAL'
                ORDER BY e.CREATED_AT ASC
                """,
                (rs, rowNum) -> {
                    String departmentId = rs.getString("departmentId");
                    Timestamp createdAt = rs.getTimestamp("createdAt");

                    return new PendingRegisterRequestDto(
                            rs.getString("accountId"),
                            rs.getString("username"),
                            rs.getString("fullName"),
                            rs.getString("roleCode"),
                            departmentId,
                            resolveDepartmentName(departmentId),
                            rs.getString("phone"),
                            rs.getString("email"),
                            rs.getString("status"),
                            createdAt == null ? null : createdAt.toLocalDateTime()
                    );
                }
        );
    }

    public String readEmployeeStatus(String staffId) {
        List<String> results = jdbcTemplate.query(
                """
                SELECT e.STATUS
                FROM JCH.EMPLOYEE e
                WHERE e.STAFF_ID = ?
                """,
                (rs, rowNum) -> rs.getString("STATUS"),
                staffId
        );

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

    public String generateStaffId(String roleCode, String deptId) {
        StaffIdRule rule = resolveRule(roleCode, deptId);
        int nextExistingValue = findMaxFourDigitSuffix(rule.regexPattern()) + 1;
        int nextValue = Math.max(rule.startValue(), nextExistingValue);

        if (nextValue > rule.endValue()) {
            throw new IllegalStateException("AUTH_STAFF_ID_RANGE_EXHAUSTED");
        }

        return "26-" + String.format("%04d", nextValue);
    }

    public void insertEmployee(
            String staffId,
            String deptId,
            String fullName,
            String phone,
            String email,
            String status
    ) {
        jdbcTemplate.update(
                """
                INSERT INTO JCH.EMPLOYEE (
                    STAFF_ID,
                    DEPT_ID,
                    NAME,
                    PHONE,
                    EMAIL,
                    STATUS,
                    CREATED_AT,
                    UPDATED_AT
                ) VALUES (?, ?, ?, ?, ?, ?, SYSDATE, SYSDATE)
                """,
                staffId,
                deptId,
                fullName,
                emptyToNull(phone),
                emptyToNull(email),
                status
        );
    }

    public void updateEmployeeStatus(String staffId, String status) {
        jdbcTemplate.update(
                "UPDATE JCH.EMPLOYEE SET STATUS = ?, UPDATED_AT = SYSDATE WHERE STAFF_ID = ?",
                status,
                staffId
        );
    }

    private int findMaxFourDigitSuffix(String regexPattern) {
        Integer maxValue = jdbcTemplate.queryForObject(
                """
                SELECT NVL(MAX(TO_NUMBER(REGEXP_SUBSTR(STAFF_ID, '[0-9]{4}$'))), 0)
                FROM (
                    SELECT STAFF_ID FROM JCH.EMPLOYEE
                    UNION ALL
                    SELECT STAFF_ID FROM JCH.EMPLOYEE_DOCTOR
                    UNION ALL
                    SELECT STAFF_ID FROM JCH.EMPLOYEE_NURSE
                    UNION ALL
                    SELECT STAFF_ID FROM JCH.EMPLOYEE_RECEPTION
                    UNION ALL
                    SELECT STAFF_ID FROM JCH.EMPLOYEE_PRIVATE
                )
                WHERE REGEXP_LIKE(STAFF_ID, ?)
                """,
                Integer.class,
                regexPattern
        );
        return maxValue == null ? 0 : maxValue;
    }

    private StaffIdRule resolveRule(String roleCode, String deptId) {
        String normalizedRole = roleCode == null ? "" : roleCode.trim().toUpperCase(Locale.ROOT);

        if ("DOCTOR".equals(normalizedRole)) {
            return new StaffIdRule(1001, 1999, "^26-1[0-9]{3}$");
        }

        if ("NURSE".equals(normalizedRole)) {
            return new StaffIdRule(2001, 2999, "^26-2[0-9]{3}$");
        }

        if ("RECEPTION".equals(normalizedRole) || "STAFF".equals(normalizedRole)) {
            return new StaffIdRule(3001, 3999, "^26-3[0-9]{3}$");
        }

        if ("RADIOLOGY_TECH".equals(normalizedRole)) {
            return new StaffIdRule(4001, 4999, "^26-4[0-9]{3}$");
        }

        if ("ADMIN".equals(normalizedRole)) {
            return new StaffIdRule(6001, 6999, "^26-6[0-9]{3}$");
        }

        return new StaffIdRule(5001, 5999, "^26-5[0-9]{3}$");
    }

    private String emptyToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String resolveDepartmentName(String departmentId) {
        if (departmentId == null) {
            return null;
        }

        String normalized = departmentId.trim().toUpperCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            return null;
        }

        return switch (normalized) {
            case "DEPT_MED", "INTERNAL_MEDICINE", "ORTHOPEDICS" -> "내과";
            case "DEPT_NURSING", "NURSING", "NURSING_DEPARTMENT" -> "간호부";
            case "DEPT_DIAG", "COMMON", "RADIOLOGY", "LAB", "RECEPTION" -> "진료지원";
            default -> departmentId;
        };
    }

    private record StaffIdRule(int startValue, int endValue, String regexPattern) {
    }
}
