import java.sql.*;

public class DumpHospitalMenu {
    public static void main(String[] args) throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "cmh", "1111");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MENU_ID, PARENT_ID, CODE, NAME, PATH, IS_ACTIVE FROM HOSPITAL.MENU ORDER BY MENU_ID")) {
            while (rs.next()) {
                System.out.println(rs.getInt(1) + "|" + rs.getString(2) + "|" + rs.getString(3) + "|" + rs.getString(4) + "|" + rs.getString(5) + "|" + rs.getString(6));
            }
        }
    }
}
