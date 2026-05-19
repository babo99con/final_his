import java.sql.*;
import java.util.*;
public class InspectCoreTables {
  public static void main(String[] args) throws Exception {
    try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "hospital", "1111")) {
      List<String> tables = Arrays.asList("RECEPTION","VISIT_REG","PAYMENT","BILL","MEDICAL_ORDER","ORDER_RESULT","SUPPORT_TEST_EXECUTION");
      for (String table : tables) {
        System.out.println("=== " + table + " ===");
        try (PreparedStatement ps = con.prepareStatement("select column_name, data_type, nullable from user_tab_columns where table_name=? order by column_id")) {
          ps.setString(1, table);
          try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
              System.out.println(rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getString(3));
            }
          }
        }
        try (PreparedStatement ps = con.prepareStatement("select cols.column_name from user_constraints cons join user_cons_columns cols on cons.constraint_name=cols.constraint_name where cons.table_name=? and cons.constraint_type='P' order by cols.position")) {
          ps.setString(1, table);
          try (ResultSet rs = ps.executeQuery()) {
            List<String> pk = new ArrayList<>();
            while (rs.next()) pk.add(rs.getString(1));
            System.out.println("PK=" + pk);
          }
        }
      }
    }
  }
}
