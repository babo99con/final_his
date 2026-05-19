import java.sql.*;
public class PreviewStaffPatient {
  public static void main(String[] args) throws Exception {
    try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "hospital", "1111")) {
      preview(con, "select * from (select patient_id, patient_no, name, status_code, gender from patient order by patient_id desc) where rownum <= 5");
      preview(con, "select * from (select id, username, full_name, domain_role, dept_id, position_id, status_code from staff order by id desc) where rownum <= 10");
      preview(con, "select * from (select id, name, dept_code from departments order by id desc) where rownum <= 10");
      preview(con, "select * from (select id, title, position_code from positions order by id desc) where rownum <= 10");
    }
  }
  static void preview(Connection con, String sql) throws Exception {
    System.out.println("SQL=" + sql);
    try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
      ResultSetMetaData md = rs.getMetaData();
      int c = md.getColumnCount();
      while (rs.next()) {
        for (int i = 1; i <= c; i++) {
          if (i > 1) System.out.print(" | ");
          System.out.print(md.getColumnLabel(i) + "=" + rs.getString(i));
        }
        System.out.println();
      }
    }
  }
}
