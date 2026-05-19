import java.sql.*;
public class PreviewRows {
  public static void main(String[] args) throws Exception {
    try (Connection con = DriverManager.getConnection("jdbc:oracle:thin:@//localhost:1521/xepdb1", "hospital", "1111")) {
      preview(con, "select * from (select id, visit_id, patient_id, patient_no, patient_name, doctor_id, dept_code, status from medical_encounter order by id desc) where rownum <= 5");
      preview(con, "select * from (select clinical_id, reception_id, clinical_type, clinical_status, doctor_id from clinical order by clinical_id desc) where rownum <= 5");
      preview(con, "select * from (select id, visit_no, patient_id, patient_no, patient_name, visit_type, status, dept_code, doctor_id from visit_reg order by id desc) where rownum <= 5");
      preview(con, "select * from (select reception_id, reception_no, patient_id, visit_type, department_id, doctor_id, reservation_id, status from reception order by reception_id desc) where rownum <= 5");
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
