import java.sql.*;
public class CheckCmhPermissionSchema {
  public static void main(String[] args) throws Exception {
    Class.forName("oracle.jdbc.OracleDriver");
    try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "hospital", "1111")) {
      String[] queries = new String[] {
        "select owner, table_name from all_tables where owner='CMH' and table_name in ('MENU','AUTH_ROLE','AUTH_ROLE_MENU_PERMISSION','AUTH_USER_MENU_PERMISSION','AUTH_SESSION','LOGIN_HISTORY','STAFF') order by table_name",
        "select count(*) as CNT from cmh.menu",
        "select count(*) as CNT from cmh.auth_role_menu_permission",
        "select count(*) as CNT from cmh.auth_user_menu_permission",
        "select count(*) as CNT from cmh.staff"
      };
      for (String sql : queries) {
        System.out.println("SQL=" + sql);
        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
          ResultSetMetaData md = rs.getMetaData();
          int cc = md.getColumnCount();
          while (rs.next()) {
            for (int i = 1; i <= cc; i++) {
              if (i > 1) System.out.print(" | ");
              System.out.print(md.getColumnLabel(i) + "=" + rs.getString(i));
            }
            System.out.println();
          }
        }
      }
    }
  }
}
