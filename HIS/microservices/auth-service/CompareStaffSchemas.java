import java.sql.*;
public class CompareStaffSchemas {
  public static void main(String[] args) throws Exception {
    Class.forName("oracle.jdbc.OracleDriver");
    try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "hospital", "1111")) {
      dump(con, "CMH", "STAFF");
      dump(con, "JCH", "STAFF");
    }
  }

  private static void dump(Connection con, String owner, String table) throws Exception {
    System.out.println("=== " + owner + "." + table + " ===");
    try (PreparedStatement ps = con.prepareStatement(
        "select column_name, data_type, nullable from all_tab_columns where owner = ? and table_name = ? order by column_id")) {
      ps.setString(1, owner);
      ps.setString(2, table);
      try (ResultSet rs = ps.executeQuery()) {
        boolean found = false;
        while (rs.next()) {
          found = true;
          System.out.println(rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getString(3));
        }
        if (!found) {
          System.out.println("<not found>");
        }
      }
    }
  }
}
