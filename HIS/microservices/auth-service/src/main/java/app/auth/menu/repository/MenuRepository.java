package app.auth.menu.repository;

import app.auth.menu.entity.AuthMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<AuthMenu, Integer> {

    @Query(value = """
        SELECT DISTINCT
            m.MENU_ID,
            m.PARENT_ID,
            m.CODE,
            m.NAME,
            m.PATH,
            m.ICON,
            m.SORT_ORDER,
            m.IS_ACTIVE,
            m.CREATED_AT,
            m.UPDATED_AT
        FROM CMH.AUTH_USER u
        JOIN CMH.MENU m ON m.IS_ACTIVE = 'Y'
        LEFT JOIN CMH.AUTH_ROLE_MENU_PERMISSION rp
            ON rp.ROLE_CODE = u.ROLE_CODE
           AND rp.MENU_ID = m.MENU_ID
        LEFT JOIN CMH.AUTH_USER_MENU_PERMISSION up
            ON up.USER_ID = u.ID
           AND up.MENU_ID = m.MENU_ID
        WHERE LOWER(u.LOGIN_ID) = LOWER(:username)
          AND NVL(up.CAN_VIEW, NVL(rp.CAN_VIEW, 'N')) = 'Y'
        ORDER BY NVL(m.PATH, '')
        """, nativeQuery = true)
    List<AuthMenu> findMenusByUsername(@Param("username") String username);

    @Query(value = """
        SELECT DISTINCT
            m.PATH
        FROM CMH.AUTH_USER u
        JOIN CMH.MENU m ON m.IS_ACTIVE = 'Y'
        LEFT JOIN CMH.AUTH_ROLE_MENU_PERMISSION rp
            ON rp.ROLE_CODE = u.ROLE_CODE
           AND rp.MENU_ID = m.MENU_ID
        LEFT JOIN CMH.AUTH_USER_MENU_PERMISSION up
            ON up.USER_ID = u.ID
           AND up.MENU_ID = m.MENU_ID
        WHERE LOWER(u.LOGIN_ID) = LOWER(:username)
          AND NVL(up.CAN_VIEW, NVL(rp.CAN_VIEW, 'N')) = 'Y'
        ORDER BY NVL(m.PATH, '')
        """, nativeQuery = true)
    List<String> findMenuPathsByUsername(@Param("username") String username);
}
