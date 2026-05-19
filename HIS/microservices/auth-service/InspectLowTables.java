import java.sql.*;
import java.util.*;

public class InspectLowTables {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
        try (Connection con = DriverManager.getConnection(url, "hospital", "1111")) {
            List<String> tables = Arrays.asList(
                "ASSESSMENT","AUDIT_LOG","CLINICAL","CODE_DETAIL","CODE_GROUP","DEPARTMENTS","DIAGNOSIS","DOCTOR_NOTE",
                "EMERGENCY_DETAIL","EMERGENCY_TRIAGE","INPATIENT_ADMISSION","INPATIENT_ADMISSION_AUDIT","INPATIENT_ADMISSION_DECISION",
                "INPATIENT_BED_ASSIGNMENT","INPATIENT_BED_ASSIGNMENT_HIS","MEDICAL_ENCOUNTER","MEDICAL_ENCOUNTER_HISTORY","NO_SEQUENCE",
                "NURSE_SUPPORT_NOTE","NURSING_RECORD","ORDERS","ORDER_ITEM","PATIENT","PATIENT_BAK_260303_1","PATIENT_CONSENT",
                "PATIENT_CONSENT_HISTORY","PATIENT_FAMILY","PATIENT_FLAG","PATIENT_INFO_HISTORY","PATIENT_INSURANCE","PATIENT_INSURANCE_HISTORY",
                "PATIENT_MEMO","PATIENT_RESTRICTION","PATIENT_STATUS_HISTORY","POSITIONS","PRESCRIPTION","RECEPTION_AUDIT","RECEPTION_CALL_HISTORY",
                "RECEPTION_CLOSURE_REASON","RECEPTION_EMERGENCY","RECEPTION_INPATIENT","RECEPTION_OUTPATIENT_DETAIL","RECEPTION_PAYMENT",
                "RECEPTION_QUALIFICATION_ITEM","RECEPTION_QUALIFICATION_SNAP","RECEPTION_SETTLEMENT_SNAPSHOT","RECEPTION_STATUS_HISTORY",
                "RECEPTION_VISIT_CLOSURE","RECEPTION_VISIT_CLOSURE_HIS","RECEPTION_WAITING_QUEUE","RESERVATION","RESERVATION_BOOKING_RULE",
                "RESERVATION_DOCTOR_SCHEDULE","RESERVATION_STATUS_HISTORY","RESERVATION_TIME_SLOT","RESERVATION_TO_RECEPTION_HIS","STAFF","STAFF_AUDIT_LOG",
                "STAFF_BOARD_POST","STAFF_COMMON_DOC","STAFF_CREDENTIAL","STAFF_STATUS_CODES","SUPPORT_SPECIMEN","VISIT_HISTORY","VITAL_SIGNS"
            );
            for (String table : tables) {
                System.out.println("=== " + table + " ===");
                try (PreparedStatement ps = con.prepareStatement(
                        "select column_name, data_type, nullable from user_tab_columns where table_name = ? order by column_id")) {
                    ps.setString(1, table);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            System.out.println(rs.getString(1) + "|" + rs.getString(2) + "|" + rs.getString(3));
                        }
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "select cols.column_name from user_constraints cons join user_cons_columns cols on cons.constraint_name = cols.constraint_name where cons.table_name = ? and cons.constraint_type='P' order by cols.position")) {
                    ps.setString(1, table);
                    try (ResultSet rs = ps.executeQuery()) {
                        List<String> pk = new ArrayList<>();
                        while (rs.next()) pk.add(rs.getString(1));
                        System.out.println("PK=" + pk);
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "select a.column_name, c_pk.table_name r_table, b.column_name r_col from user_constraints c join user_cons_columns a on c.constraint_name=a.constraint_name join user_constraints c_pk on c.r_constraint_name = c_pk.constraint_name join user_cons_columns b on c_pk.constraint_name=b.constraint_name and a.position=b.position where c.constraint_type='R' and c.table_name=? order by a.position")) {
                    ps.setString(1, table);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            System.out.println("FK=" + rs.getString(1) + "->" + rs.getString(2) + "." + rs.getString(3));
                        }
                    }
                }
            }
        }
    }
}
