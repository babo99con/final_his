package app.auth.common.repository;

import app.auth.common.dto.AuthUserProfileInfo;
import app.auth.common.dto.AuthUserSearchInfo;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Locale;

@Repository
@AllArgsConstructor
public class AuthUserProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuthUserProfileInfo readProfileInfo(String userId) {
        // Dev/heterogeneous DB environments may not grant access to JCH schema.
        // In that case, we still want auth/login to work with reduced profile fields.
        List<AuthUserProfileInfo> results;
        try {
            results = jdbcTemplate.query(
                    """
                    SELECT
                        e.NAME AS fullName,
                        e.STATUS AS status,
                        e.DEPT_ID AS departmentId
                    FROM HOSPITAL.EMPLOYEE e
                    WHERE e.STAFF_ID = ?
                    """,
                    (rs, rowNum) -> {
                        String departmentId = rs.getString("departmentId");
                        return new AuthUserProfileInfo(
                                rs.getString("fullName"),
                                rs.getString("status"),
                                departmentId,
                                resolveDepartmentName(departmentId)
                        );
                    },
                    userId
            );
        } catch (DataAccessException ex) {
            return new AuthUserProfileInfo(null, null, null, null);
        }

        if (results.isEmpty()) {
            return new AuthUserProfileInfo(null, null, null, null);
        }

        return results.get(0);
    }

    public List<AuthUserSearchInfo> searchUsers(String keyword, int limit) {
        if (limit <= 0) {
            return List.of();
        }

        String normalizedKeyword = normalizeKeyword(keyword);
        if (!StringUtils.hasText(normalizedKeyword)) {
            try {
                return jdbcTemplate.query(
                        """
                        SELECT * FROM (
                            SELECT
                                a.ID AS userId,
                                a.LOGIN_ID AS username,
                                a.ROLE_CODE AS roleCode,
                                e.NAME AS fullName,
                                e.STATUS AS status,
                                e.DEPT_ID AS departmentId
                            FROM HOSPITAL.AUTH_USER a
                            LEFT JOIN HOSPITAL.EMPLOYEE e ON e.STAFF_ID = a.ID
                            ORDER BY a.LOGIN_ID ASC
                        )
                        WHERE ROWNUM <= ?
                        """,
                        (rs, rowNum) -> mapSearchInfo(
                                rs.getString("userId"),
                                rs.getString("username"),
                                rs.getString("roleCode"),
                                rs.getString("fullName"),
                                rs.getString("status"),
                                rs.getString("departmentId")
                        ),
                        limit
                );
            } catch (DataAccessException ex) {
                // JCH ?ㅽ궎留??뚯씠釉붿씠 ?녿뒗 ?섍꼍?먯꽌???꾨줈??蹂닿컯 ?놁씠 湲곕낯 怨꾩젙 ?뺣낫留?諛섑솚?쒕떎.
                return jdbcTemplate.query(
                        """
                        SELECT * FROM (
                            SELECT
                                a.ID AS userId,
                                a.LOGIN_ID AS username,
                                a.ROLE_CODE AS roleCode
                            FROM HOSPITAL.AUTH_USER a
                            ORDER BY a.LOGIN_ID ASC
                        )
                        WHERE ROWNUM <= ?
                        """,
                        (rs, rowNum) -> mapSearchInfo(
                                rs.getString("userId"),
                                rs.getString("username"),
                                rs.getString("roleCode"),
                                null,
                                null,
                                null
                        ),
                        limit
                );
            }
        }

        String likeKeyword = toLikeKeyword(normalizedKeyword);
        try {
            return jdbcTemplate.query(
                    """
                    SELECT * FROM (
                        SELECT
                            a.ID AS userId,
                            a.LOGIN_ID AS username,
                            a.ROLE_CODE AS roleCode,
                            e.NAME AS fullName,
                            e.STATUS AS status,
                            e.DEPT_ID AS departmentId
                        FROM HOSPITAL.AUTH_USER a
                        LEFT JOIN HOSPITAL.EMPLOYEE e ON e.STAFF_ID = a.ID
                        WHERE LOWER(a.ID) LIKE ? ESCAPE '\\'
                           OR LOWER(a.LOGIN_ID) LIKE ? ESCAPE '\\'
                           OR LOWER(NVL(e.NAME, '')) LIKE ? ESCAPE '\\'
                        ORDER BY a.LOGIN_ID ASC
                    )
                    WHERE ROWNUM <= ?
                    """,
                    (rs, rowNum) -> mapSearchInfo(
                            rs.getString("userId"),
                            rs.getString("username"),
                            rs.getString("roleCode"),
                            rs.getString("fullName"),
                            rs.getString("status"),
                            rs.getString("departmentId")
                    ),
                    likeKeyword,
                    likeKeyword,
                    likeKeyword,
                    limit
            );
        } catch (DataAccessException ex) {
            return jdbcTemplate.query(
                    """
                    SELECT * FROM (
                        SELECT
                            a.ID AS userId,
                            a.LOGIN_ID AS username,
                            a.ROLE_CODE AS roleCode
                        FROM HOSPITAL.AUTH_USER a
                        WHERE LOWER(a.ID) LIKE ? ESCAPE '\\'
                           OR LOWER(a.LOGIN_ID) LIKE ? ESCAPE '\\'
                        ORDER BY a.LOGIN_ID ASC
                    )
                    WHERE ROWNUM <= ?
                    """,
                    (rs, rowNum) -> mapSearchInfo(
                            rs.getString("userId"),
                            rs.getString("username"),
                            rs.getString("roleCode"),
                            null,
                            null,
                            null
                    ),
                    likeKeyword,
                    likeKeyword,
                    limit
            );
        }
    }

    private AuthUserSearchInfo mapSearchInfo(String userId,
                                             String username,
                                             String roleCode,
                                             String fullName,
                                             String status,
                                             String departmentId) {
        return new AuthUserSearchInfo(
                userId,
                username,
                StringUtils.hasText(fullName) ? fullName : username,
                roleCode,
                StringUtils.hasText(status) ? status : "INACTIVE",
                resolveDepartmentName(departmentId)
        );
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return "";
        }

        return keyword.trim().toLowerCase(Locale.ROOT);
    }

    private String toLikeKeyword(String keyword) {
        return "%" + keyword
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_") + "%";
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
            case "DEPT_MED", "INTERNAL_MEDICINE", "ORTHOPEDICS" -> "Internal Medicine";
            case "DEPT_NURSING", "NURSING", "NURSING_DEPARTMENT" -> "Nursing";
            case "DEPT_DIAG", "COMMON", "RADIOLOGY", "LAB", "RECEPTION" -> "Diagnostics";
            default -> departmentId;
        };
    }
}
