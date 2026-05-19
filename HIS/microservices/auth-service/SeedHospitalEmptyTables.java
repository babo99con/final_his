import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SeedHospitalEmptyTables {
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
    private static final String USER = "hospital";
    private static final String PASSWORD = "1111";

    private static final long SAMPLE_APPT_RULE_BASE = 810000L;
    private static final long SAMPLE_SCHEDULE_BASE = 811000L;
    private static final long SAMPLE_SLOT_BASE = 812000L;
    private static final long SAMPLE_APPOINTMENT_BASE = 813000L;
    private static final long SAMPLE_APPT_STATUS_BASE = 814000L;
    private static final long SAMPLE_APPT_TO_RCPT_BASE = 815000L;

    private static final long SAMPLE_RECEPTION_BASE = 820000L;
    private static final long SAMPLE_RECEPTION_AUDIT_BASE = 821000L;
    private static final long SAMPLE_RECEPTION_STATUS_BASE = 822000L;
    private static final long SAMPLE_OUTPATIENT_BASE = 823000L;
    private static final long SAMPLE_QUAL_SNAPSHOT_BASE = 824000L;
    private static final long SAMPLE_QUAL_ITEM_BASE = 825000L;
    private static final long SAMPLE_WAITING_QUEUE_BASE = 826000L;
    private static final long SAMPLE_CALL_HISTORY_BASE = 827000L;
    private static final long SAMPLE_VISIT_CLOSURE_BASE = 828000L;
    private static final long SAMPLE_VISIT_CLOSURE_HIS_BASE = 829000L;

    private static final long SAMPLE_CLINICAL_VISIT_BASE = 830000L;
    private static final long SAMPLE_CLINICAL_QUEUE_BASE = 831000L;
    private static final long SAMPLE_CLINICAL_VISIT_STATUS_BASE = 832000L;
    private static final long SAMPLE_NOTE_BASE = 833000L;
    private static final long SAMPLE_NOTE_ATTACHMENT_BASE = 834000L;
    private static final long SAMPLE_NOTE_HISTORY_BASE = 835000L;
    private static final long SAMPLE_SOAP_BASE = 836000L;
    private static final long SAMPLE_DIAGNOSIS_BASE = 837000L;
    private static final long SAMPLE_CLINICAL_NOTE_BASE = 838000L;
    private static final long SAMPLE_CLINICAL_NOTE_HISTORY_BASE = 839000L;
    private static final long SAMPLE_CLINICAL_ORDER_BASE = 840000L;
    private static final long SAMPLE_CLINICAL_ORDER_ITEM_BASE = 841000L;
    private static final long SAMPLE_CLINICAL_ORDER_RESULT_BASE = 842000L;
    private static final long SAMPLE_MEDICAL_ORDER_BASE = 843000L;
    private static final long SAMPLE_ORDER_RESULT_BASE = 844000L;
    private static final long SAMPLE_PROCEDURE_RESULT_BASE = 845000L;

    private static final long SAMPLE_ADMISSION_BASE = 850000L;
    private static final long SAMPLE_ADMISSION_DECISION_BASE = 851000L;
    private static final long SAMPLE_BED_ASSIGNMENT_BASE = 852000L;
    private static final long SAMPLE_BED_ASSIGNMENT_HIS_BASE = 853000L;
    private static final long SAMPLE_ADMISSION_AUDIT_BASE = 854000L;

    private static final long SAMPLE_VISIT_BASE = 860000L;
    private static final long SAMPLE_VISIT_RESERVATION_BASE = 861000L;
    private static final long SAMPLE_VISIT_EMERGENCY_BASE = 862000L;
    private static final long SAMPLE_VISIT_INPATIENT_BASE = 863000L;

    private static final long SAMPLE_ENCOUNTER_STATUS_BASE = 870000L;
    private static final long SAMPLE_ENCOUNTER_SUMMARY_BASE = 871000L;
    private static final long SAMPLE_MEDICAL_ENCOUNTER_DIAG_BASE = 872000L;
    private static final long SAMPLE_MEDICAL_ENCOUNTER_ASSET_BASE = 873000L;

    private static final long SAMPLE_BILL_BASE = 880000L;
    private static final long SAMPLE_PAYMENT_BASE = 880500L;
    private static final long SAMPLE_BILL_ITEM_BASE = 881000L;
    private static final long SAMPLE_BILL_HISTORY_BASE = 882000L;
    private static final long SAMPLE_PAYMENT_CANCEL_BASE = 883000L;
    private static final long SAMPLE_PAYMENT_METHOD_STAT_BASE = 884000L;
    private static final long SAMPLE_UNPAID_BASE = 885000L;

    private static final long SAMPLE_STAFF_CHANGE_BASE = 890000L;
    private static final long SAMPLE_STAFF_HISTORY_BASE = 891000L;
    private static final long SAMPLE_DOC_BASE = 892000L;
    private static final long SAMPLE_DOC_LINE_BASE = 893000L;

    private static final int BOOKING_RULE_COUNT = 50;
    private static final int SCHEDULE_COUNT = 60;
    private static final int SLOT_COUNT = 180;
    private static final int APPOINTMENT_COUNT = 90;
    private static final int RECEPTION_COUNT = 110;
    private static final int EMERGENCY_RECEPTION_COUNT = 50;
    private static final int ADMISSION_COUNT = 50;
    private static final int LEGACY_VISIT_GROUP_COUNT = 50;
    private static final int BILL_COUNT = 80;

    private final Random random = new Random(20260316L);
    private final List<String> symptomSamples = Arrays.asList(
        "발열과 오한", "기침과 가래", "복통과 소화불량", "두통과 어지럼증", "흉통과 호흡곤란",
        "무릎 통증", "허리 통증", "목 통증", "피부 발진", "만성 피로",
        "혈압 조절 상담", "당뇨 추적 검사", "건강검진 이상 소견 확인", "구토와 설사", "배뇨통"
    );
    private final List<String> diagnosisCodes = Arrays.asList(
        "J20.9", "J18.9", "K29.7", "M54.5", "R51", "E11.9", "I10", "Z00.0", "N39.0", "L50.9"
    );
    private final List<String> diagnosisNames = Arrays.asList(
        "급성 기관지염", "폐렴 의증", "위염", "요통", "두통", "제2형 당뇨병", "본태성 고혈압", "일반 건강검진", "요로감염", "두드러기"
    );
    private final List<String> assessmentSamples = Arrays.asList(
        "문진 및 진찰 결과 상기도 감염 가능성이 높습니다.",
        "복부 압통은 경미하며 소화기 추적 관찰이 필요합니다.",
        "만성 통증으로 생활 습관 교정과 약물 조절을 권고합니다.",
        "기저질환 추적 관찰 중이며 활력징후는 안정적입니다.",
        "추가 검사 후 외래 경과 관찰 계획입니다."
    );
    private final List<String> planSamples = Arrays.asList(
        "혈액검사와 영상검사를 시행하고 1주 뒤 재내원 안내.",
        "대증치료 후 증상 악화 시 응급실 방문 안내.",
        "약물 복용법 설명 후 생활습관 교정 교육 시행.",
        "주치의 외래 예약과 추적 검사를 연계합니다.",
        "간호교육 후 수납 및 귀가 절차를 진행합니다."
    );
    private final List<String> orderItemNames = Arrays.asList(
        "CBC", "CRP", "혈당 검사", "흉부 X-ray", "복부 초음파",
        "소변 검사", "간기능 검사", "심전도", "주사 처치", "소독 처치"
    );
    private final List<String> visitPurposes = Arrays.asList(
        "초진", "재진", "건강검진 결과 상담", "만성질환 추적", "응급 내원"
    );
    private final List<String> consultationTypes = Arrays.asList(
        "대면진료", "전화상담", "검사결과 상담", "응급진료", "입원연계"
    );
    private final List<String> emergencyRoutes = Arrays.asList(
        "직접내원", "119 이송", "타병원 전원", "보호자 동반"
    );
    private final List<String> closureStatuses = Arrays.asList(
        "CLOSED", "DISCHARGED", "TRANSFERRED", "CANCELED"
    );
    private final List<String> samplePaymentMethodNames = Arrays.asList(
        "현금", "신용카드", "체크카드", "계좌이체", "카카오페이",
        "네이버페이", "삼성페이", "제로페이", "보험청구", "기업후불"
    );
    private final List<String> sampleBillTypeNames = Arrays.asList(
        "초진 진찰료", "재진 진찰료", "응급 진찰료", "혈액검사", "소변검사",
        "간기능 검사", "흉부 X-ray", "복부 초음파", "심전도", "주사 처치",
        "상처 소독", "재활치료", "물리치료", "도수치료", "약제비",
        "주사제 비용", "입원료", "병실료", "식대", "간호 처치료"
    );
    private final List<String> closureReasonNames = Arrays.asList(
        "진료 완료", "귀가 조치", "입원 전환", "타과 전과", "타병원 전원",
        "환자 요청 취소", "보험정보 미확인", "수납 대기 후 종료", "검사 결과 후 종료", "응급처치 후 귀가"
    );
    private final List<String> encounterStatusNames = Arrays.asList(
        "접수대기", "접수완료", "문진대기", "문진중", "진료대기",
        "진료중", "검사대기", "검사중", "수납대기", "수납완료"
    );
    private final List<String> staffHistoryFields = Arrays.asList(
        "PHONE", "DEPT_ID", "STATUS", "OFFICE_LOCATION", "BIO"
    );

    private Connection connection;
    private List<PatientRef> patients;
    private List<DepartmentRef> clinicalDepartments;
    private List<EncounterRef> encounters;
    private StaffRef admin;
    private StaffRef doctor;
    private StaffRef nurse;
    private StaffRef reception;

    public static void main(String[] args) throws Exception {
        new SeedHospitalEmptyTables().run();
    }

    private void run() throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            connection = con;
            connection.setAutoCommit(false);

            loadReferenceData();
            cleanupSampleRows();

            List<String> paymentMethodCodes = seedPaymentMethods();
            List<String> billTypeCodes = seedBillTypes();
            List<String> closureReasonCodes = seedClosureReasons();
            seedEncounterStatuses();
            List<Long> sampleDocIds = seedSampleDocuments();
            seedStaffCommonDocLines(sampleDocIds);
            seedStaffChangeRequests();
            seedStaffHistories();
            seedAppointmentBookingRules();
            seedReceptionNoSequences();

            List<DoctorScheduleRef> schedules = buildSchedules();
            insertSchedules(schedules);

            List<AppointmentRef> appointments = buildAppointments(schedules);
            insertAppointments(appointments);
            insertTimeSlots(schedules, appointments);
            seedAppointmentStatusHistory(appointments);

            List<ReceptionRef> receptions = buildReceptions(appointments);
            insertReceptions(receptions);
            seedAppointmentConversions(receptions);
            seedReceptionSideTables(receptions, closureReasonCodes);

            List<ClinicalVisitRef> clinicalVisits = buildClinicalVisits(receptions);
            insertClinicalVisits(clinicalVisits);
            seedClinicalSideTables(clinicalVisits);

            List<AdmissionRef> admissions = buildAdmissions(receptions);
            insertAdmissions(admissions);
            seedAdmissionSideTables(admissions);

            List<LegacyVisitRef> legacyVisits = buildLegacyVisits(receptions, admissions, appointments);
            insertLegacyVisits(legacyVisits);
            seedLegacyVisitSideTables(legacyVisits);

            seedEncounterSideTables();
            seedBillingSideTables(receptions, paymentMethodCodes, billTypeCodes);

            connection.commit();
            printSummary();
        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        }
    }

    private void loadReferenceData() throws SQLException {
        patients = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
            "select patient_id, patient_no, name from patient where status_code is not null order by patient_id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                PatientRef patient = new PatientRef();
                patient.id = rs.getLong("patient_id");
                patient.patientNo = rs.getString("patient_no");
                patient.name = rs.getString("name");
                patients.add(patient);
            }
        }

        Map<String, StaffRef> staffMap = new HashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(
            "select id, username, full_name, domain_role, dept_id from staff order by id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                StaffRef staff = new StaffRef();
                staff.id = rs.getLong("id");
                staff.username = rs.getString("username");
                staff.fullName = rs.getString("full_name");
                staff.role = rs.getString("domain_role");
                staff.deptId = rs.getLong("dept_id");
                if (rs.wasNull()) {
                    staff.deptId = 0L;
                }
                staffMap.put(staff.username, staff);
            }
        }

        admin = staffMap.get("admin");
        doctor = staffMap.get("doctor");
        nurse = staffMap.get("nurse");
        reception = staffMap.get("reception");

        if (admin == null || doctor == null || nurse == null || reception == null) {
            throw new IllegalStateException("admin, doctor, nurse, reception staff rows are required.");
        }

        clinicalDepartments = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
            "select id, name from departments where is_active = 'Y' order by id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DepartmentRef department = new DepartmentRef();
                department.id = rs.getLong("id");
                department.name = rs.getString("name");
                if (department.id == 2L || department.id == 4L || department.id >= 11L) {
                    clinicalDepartments.add(department);
                }
            }
        }

        if (clinicalDepartments.isEmpty()) {
            throw new IllegalStateException("No active clinical departments were found.");
        }

        encounters = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
            "select id, visit_id, patient_id from medical_encounter order by id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                EncounterRef encounter = new EncounterRef();
                encounter.id = rs.getLong("id");
                encounter.visitId = rs.getLong("visit_id");
                encounter.patientId = rs.getLong("patient_id");
                encounters.add(encounter);
            }
        }
    }

    private void cleanupSampleRows() throws SQLException {
        deleteByRange("APPT_TO_RCPT_CONVERSION_HIS", "CONVERSION_ID", SAMPLE_APPT_TO_RCPT_BASE);
        deleteByRange("APPT_RESERVATION_STATUS_HIS", "APPT_STATUS_HIS_ID", SAMPLE_APPT_STATUS_BASE);
        deleteByRange("APPT_TIME_SLOT", "SLOT_ID", SAMPLE_SLOT_BASE);
        deleteByRange("APPT_RESERVATION", "APPT_ID", SAMPLE_APPOINTMENT_BASE);
        deleteByRange("APPT_DOCTOR_SCHEDULE", "SCHEDULE_ID", SAMPLE_SCHEDULE_BASE);
        deleteByRange("APPT_BOOKING_RULE", "RULE_ID", SAMPLE_APPT_RULE_BASE);

        deleteByRange("ADM_ADMISSION_AUDIT", "AUDIT_ID", SAMPLE_ADMISSION_AUDIT_BASE);
        deleteByRange("ADM_BED_ASSIGNMENT_HIS", "BED_ASSIGNMENT_HIS_ID", SAMPLE_BED_ASSIGNMENT_HIS_BASE);
        deleteByRange("ADM_BED_ASSIGNMENT", "BED_ASSIGNMENT_ID", SAMPLE_BED_ASSIGNMENT_BASE);
        deleteByRange("ADM_ADMISSION_DECISION", "DECISION_ID", SAMPLE_ADMISSION_DECISION_BASE);
        deleteByRange("ADM_INPATIENT_ADMISSION", "ADMISSION_ID", SAMPLE_ADMISSION_BASE);

        deleteByRange("EMR_EMERGENCY_DETAIL", "EMERGENCY_DETAIL_ID", SAMPLE_RECEPTION_BASE);
        deleteByRange("EMR_TRIAGE", "TRIAGE_ID", SAMPLE_RECEPTION_BASE);

        deleteByRange("RCPT_CALL_HISTORY", "CALL_HISTORY_ID", SAMPLE_CALL_HISTORY_BASE);
        deleteByRange("RCPT_WAITING_QUEUE", "QUEUE_ID", SAMPLE_WAITING_QUEUE_BASE);
        deleteByRange("RCPT_VISIT_CLOSURE_HIS", "CLOSURE_HISTORY_ID", SAMPLE_VISIT_CLOSURE_HIS_BASE);
        deleteByRange("RCPT_VISIT_CLOSURE", "CLOSURE_ID", SAMPLE_VISIT_CLOSURE_BASE);
        deleteByRange("RCPT_STATUS_HISTORY", "STATUS_HISTORY_ID", SAMPLE_RECEPTION_STATUS_BASE);
        deleteByRange("RCPT_RECEPTION_AUDIT", "AUDIT_ID", SAMPLE_RECEPTION_AUDIT_BASE);
        deleteByRange("RCPT_QUALIFICATION_ITEM", "SNAPSHOT_ITEM_ID", SAMPLE_QUAL_ITEM_BASE);
        deleteByRange("RCPT_QUALIFICATION_SNAPSHOT", "SNAPSHOT_ID", SAMPLE_QUAL_SNAPSHOT_BASE);
        deleteByRange("RCPT_OUTPATIENT_DETAIL", "OUTPATIENT_DETAIL_ID", SAMPLE_OUTPATIENT_BASE);
        deleteByRange("RCPT_RECEPTION", "RECEPTION_ID", SAMPLE_RECEPTION_BASE);

        deleteByRange("CLINICAL_ORDER_RESULT", "RESULT_ID", SAMPLE_CLINICAL_ORDER_RESULT_BASE);
        deleteByRange("CLINICAL_ORDER_ITEM", "ORDER_ITEM_ID", SAMPLE_CLINICAL_ORDER_ITEM_BASE);
        deleteByRange("CLINICAL_ORDER", "ORDER_ID", SAMPLE_CLINICAL_ORDER_BASE);
        deleteByRange("PROCEDURE_RESULT", "PROCEDURE_RESULT_ID", SAMPLE_PROCEDURE_RESULT_BASE);
        deleteByRange("ORDER_RESULT", "RESULT_ID", SAMPLE_ORDER_RESULT_BASE);
        deleteByPrefix("SUPPORT_TEST_EXECUTION", "TEST_EXECUTION_ID", "SMP-TEX-%");
        deleteByRange("CLINICAL_DIAGNOSIS", "DIAGNOSIS_ID", SAMPLE_DIAGNOSIS_BASE);
        deleteByRange("SOAP_SECTION", "SECTION_ID", SAMPLE_SOAP_BASE);
        deleteByRange("NOTE_ATTACHMENT", "ATTACHMENT_ID", SAMPLE_NOTE_ATTACHMENT_BASE);
        deleteByRange("NOTE_HISTORY", "HISTORY_ID", SAMPLE_NOTE_HISTORY_BASE);
        deleteByRange("NOTE", "NOTE_ID", SAMPLE_NOTE_BASE);
        deleteByRange("CLINICAL_NOTE_HISTORY", "HISTORY_ID", SAMPLE_CLINICAL_NOTE_HISTORY_BASE);
        deleteByRange("CLINICAL_NOTE", "NOTE_ID", SAMPLE_CLINICAL_NOTE_BASE);
        deleteByRange("CLINICAL_VISIT_STATUS_HISTORY", "HISTORY_ID", SAMPLE_CLINICAL_VISIT_STATUS_BASE);
        deleteByRange("CLINICAL_VISIT_QUEUE", "QUEUE_ID", SAMPLE_CLINICAL_QUEUE_BASE);
        deleteByRange("CLINICAL_VISIT", "VISIT_ID", SAMPLE_CLINICAL_VISIT_BASE);
        deleteByRange("MEDICAL_ORDER", "ORDER_ID", SAMPLE_MEDICAL_ORDER_BASE);

        deleteByRange("VISIT_RESERVATION", "VISIT_ID", SAMPLE_VISIT_RESERVATION_BASE);
        deleteByRange("VISIT_EMERGENCY", "VISIT_ID", SAMPLE_VISIT_EMERGENCY_BASE);
        deleteByRange("VISIT_INPATIENT", "VISIT_ID", SAMPLE_VISIT_INPATIENT_BASE);
        deleteByRange("VISIT", "VISIT_ID", SAMPLE_VISIT_BASE);

        deleteByRange("MEDICAL_ENCOUNTER_DIAGNOSIS", "ID", SAMPLE_MEDICAL_ENCOUNTER_DIAG_BASE);
        deleteByRange("MEDICAL_ENCOUNTER_ASSET", "ID", SAMPLE_MEDICAL_ENCOUNTER_ASSET_BASE);
        deleteByRange("ENCOUNTER_SUMMARY", "SUMMARY_ID", SAMPLE_ENCOUNTER_SUMMARY_BASE);
        deleteByRange("ENCOUNTER_STATUS", "STATUS_ID", SAMPLE_ENCOUNTER_STATUS_BASE);

        deleteByRange("PAYMENT_CANCEL", "PAYMENT_CANCEL_ID", SAMPLE_PAYMENT_CANCEL_BASE);
        deleteByRange("UNPAID", "UNPAID_ID", SAMPLE_UNPAID_BASE);
        deleteByRange("BILL_HISTORY", "BILL_HISTORY_ID", SAMPLE_BILL_HISTORY_BASE);
        deleteByRange("BILL_ITEM", "BILL_ITEM_ID", SAMPLE_BILL_ITEM_BASE);
        deleteByRange("PAYMENT", "PAYMENT_ID", SAMPLE_PAYMENT_BASE);
        deleteByRange("BILL", "BILL_ID", SAMPLE_BILL_BASE);
        deleteByRange("PAYMENT_METHOD_STAT", "STAT_ID", SAMPLE_PAYMENT_METHOD_STAT_BASE);
        deleteByPrefix("PAYMENT_METHOD", "METHOD_CODE", "SMP_PM_%");
        deleteByPrefix("BILL_TYPE", "BILL_TYPE_CODE", "SMP_BT_%");

        deleteByRange("STAFF_CHANGE_REQUEST", "ID", SAMPLE_STAFF_CHANGE_BASE);
        deleteByRange("STAFF_HISTORY", "ID", SAMPLE_STAFF_HISTORY_BASE);
        deleteByRange("STAFF_COMMON_DOC_LINE", "ID", SAMPLE_DOC_LINE_BASE);
        deleteByRange("STAFF_COMMON_DOC", "ID", SAMPLE_DOC_BASE);
        deleteByPrefix("RCPT_CLOSURE_REASON", "REASON_CD", "SMP_CR_%");
        deleteByPrefix("RECEPTION_NO_SEQUENCE", "SEQ_TYPE", "SMP_%");
    }

    private List<String> seedPaymentMethods() throws SQLException {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String code = String.format("SMP_PM_%03d", i + 1);
            String baseName = samplePaymentMethodNames.get(i % samplePaymentMethodNames.size());
            String methodName = baseName + " 채널 " + String.format("%02d", (i / samplePaymentMethodNames.size()) + 1);
            execute(
                "insert into PAYMENT_METHOD (METHOD_CODE, METHOD_NAME) values (?, ?)",
                code, methodName
            );
            codes.add(code);
        }
        return codes;
    }

    private List<String> seedBillTypes() throws SQLException {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String code = String.format("SMP_BT_%03d", i + 1);
            String baseName = sampleBillTypeNames.get(i % sampleBillTypeNames.size());
            String typeName = baseName + " 항목 " + String.format("%02d", (i / sampleBillTypeNames.size()) + 1);
            execute(
                "insert into BILL_TYPE (BILL_TYPE_CODE, BILL_TYPE_NAME, DESCRIPTION) values (?, ?, ?)",
                code, typeName, typeName + " 샘플 청구 분류"
            );
            codes.add(code);
        }
        return codes;
    }

    private List<String> seedClosureReasons() throws SQLException {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String code = String.format("SMP_CR_%03d", i + 1);
            String reasonName = closureReasonNames.get(i % closureReasonNames.size()) + " " + String.format("%02d", (i / closureReasonNames.size()) + 1);
            String reasonGroup = i % 2 == 0 ? "NORMAL" : "EXCEPTION";
            execute(
                "insert into RCPT_CLOSURE_REASON (REASON_CD, REASON_NAME, REASON_GROUP_CD, USABLE_YN, SORT_ORDER) values (?, ?, ?, 'Y', ?)",
                code, reasonName, reasonGroup, i + 1
            );
            codes.add(code);
        }
        return codes;
    }

    private void seedEncounterStatuses() throws SQLException {
        for (int i = 0; i < 50; i++) {
            long statusId = SAMPLE_ENCOUNTER_STATUS_BASE + i;
            String statusName = encounterStatusNames.get(i % encounterStatusNames.size()) + " 단계 " + String.format("%02d", (i / encounterStatusNames.size()) + 1);
            execute(
                "insert into ENCOUNTER_STATUS (STATUS_ID, STATUS_NAME, DESCRIPTION) values (?, ?, ?)",
                statusId, statusName, statusName + "에 대한 샘플 설명"
            );
        }
    }

    private List<Long> seedSampleDocuments() throws SQLException {
        List<Long> docIds = new ArrayList<>();
        List<String> categories = Arrays.asList("규정", "매뉴얼", "양식", "교육자료", "공문");

        for (int i = 0; i < 25; i++) {
            long docId = SAMPLE_DOC_BASE + i;
            DepartmentRef senderDept = clinicalDepartments.get(i % clinicalDepartments.size());
            DepartmentRef receiverDept = clinicalDepartments.get((i + 1) % clinicalDepartments.size());
            String category = categories.get(i % categories.size());
            String title = "[샘플] " + category + " 문서 " + String.format("%02d", i + 1);
            String approvalStatus = i % 4 == 0 ? "APPROVED" : (i % 4 == 1 ? "PENDING" : "REJECTED");

            execute(
                "insert into STAFF_COMMON_DOC (" +
                    "ID, CATEGORY, TITLE, CONTENT, VERSION_LABEL, OWNER_NAME, " +
                    "SENDER_DEPT_ID, SENDER_DEPT_NAME, RECEIVER_DEPT_ID, RECEIVER_DEPT_NAME, " +
                    "APPROVER_ID, APPROVER_NAME, APPROVAL_STATUS, REJECTION_REASON, " +
                    "AUTHOR_ID, AUTHOR_NAME, IS_DELETED, CREATED_AT, UPDATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                docId,
                category,
                title,
                title + " 본문입니다.",
                "v2." + (i + 1),
                admin.fullName,
                senderDept.id,
                senderDept.name,
                receiverDept.id,
                receiverDept.name,
                doctor.username,
                doctor.fullName,
                approvalStatus,
                approvalStatus.equals("REJECTED") ? "내용 보완 필요" : null,
                admin.username,
                admin.fullName,
                "N",
                sqlDate(daysAgo(40 - i)),
                sqlDate(daysAgo(20 - (i / 2)))
            );
            docIds.add(docId);
        }
        return docIds;
    }

    private void seedStaffCommonDocLines(List<Long> docIds) throws SQLException {
        for (int i = 0; i < 60; i++) {
            long lineId = SAMPLE_DOC_LINE_BASE + i;
            long docId = docIds.get(i % docIds.size());
            boolean approvalLine = i % 2 == 0;

            execute(
                "insert into STAFF_COMMON_DOC_LINE (" +
                    "ID, DOC_ID, LINE_ORDER, LINE_TYPE, APPROVER_ID, APPROVER_NAME, " +
                    "ACTION_STATUS, ACTION_COMMENT, ACTED_AT, CREATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                lineId,
                docId,
                (i % 3) + 1,
                approvalLine ? "APPROVAL" : "REFERENCE",
                approvalLine ? doctor.username : nurse.username,
                approvalLine ? doctor.fullName : nurse.fullName,
                approvalLine ? (i % 3 == 0 ? "APPROVED" : "PENDING") : "VIEWED",
                approvalLine ? "검토 완료" : "참조 확인",
                approvalLine && i % 3 == 0 ? sqlDate(daysAgo(5 - (i % 5))) : null,
                sqlDate(daysAgo(10 - (i % 5)))
            );
        }
    }

    private void seedStaffChangeRequests() throws SQLException {
        List<String> requestTypes = Arrays.asList("DEPT_CHANGE", "PHONE_UPDATE", "ROLE_CHANGE", "STATUS_CHANGE", "PROFILE_UPDATE");

        for (int i = 0; i < 50; i++) {
            StaffRef staff = selectStaff(i);
            String requestType = requestTypes.get(i % requestTypes.size());
            String status = i % 3 == 0 ? "APPROVED" : (i % 3 == 1 ? "PENDING" : "REJECTED");
            String payload = "{\"requestType\":\"" + requestType + "\",\"targetStaff\":\"" + staff.username + "\",\"sample\":true}";

            execute(
                "insert into STAFF_CHANGE_REQUEST (" +
                    "ID, STAFF_ID, REQUEST_TYPE, REQUEST_PAYLOAD, REASON, STATUS, " +
                    "REQUESTED_BY, REQUESTED_AT, REVIEWED_BY, REVIEWED_AT, REVIEW_COMMENT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                SAMPLE_STAFF_CHANGE_BASE + i,
                staff.id,
                requestType,
                payload,
                "운영 샘플 데이터 생성용 요청",
                status,
                staff.username,
                sqlDate(daysAgo(30 - (i % 15))),
                status.equals("PENDING") ? null : admin.username,
                status.equals("PENDING") ? null : sqlDate(daysAgo(10 - (i % 7))),
                status.equals("REJECTED") ? "보완 후 재신청 필요" : "승인 처리 완료"
            );
        }
    }

    private void seedStaffHistories() throws SQLException {
        for (int i = 0; i < 50; i++) {
            StaffRef staff = selectStaff(i);
            String fieldName = staffHistoryFields.get(i % staffHistoryFields.size());

            execute(
                "insert into STAFF_HISTORY (" +
                    "ID, STAFF_ID, EVENT_TYPE, FIELD_NAME, OLD_VALUE, NEW_VALUE, REASON, CHANGED_BY, CHANGED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                SAMPLE_STAFF_HISTORY_BASE + i,
                staff.id,
                "UPDATE",
                fieldName,
                "기존값-" + String.format("%02d", i + 1),
                "변경값-" + String.format("%02d", i + 1),
                "운영 샘플 변경 이력",
                admin.username,
                sqlDate(daysAgo(25 - (i % 12)))
            );
        }
    }

    private void seedAppointmentBookingRules() throws SQLException {
        for (int i = 0; i < BOOKING_RULE_COUNT; i++) {
            DepartmentRef department = clinicalDepartments.get(i % clinicalDepartments.size());
            execute(
                "insert into APPT_BOOKING_RULE (" +
                    "RULE_ID, DEPT_ID, DOCTOR_ID, MIN_LEAD_MIN, MAX_LEAD_DAY, OVERBOOK_ALLOW_YN, " +
                    "CANCEL_DEADLINE_MIN, PRIORITY_EXPR, ACTIVE_YN" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, 'Y')",
                SAMPLE_APPT_RULE_BASE + i,
                department.id,
                doctor.id,
                30 + (i % 4) * 30,
                14 + (i % 10),
                i % 5 == 0 ? "Y" : "N",
                60 + (i % 6) * 30,
                "PRIORITY=" + ((i % 3) + 1)
            );
        }
    }

    private void seedReceptionNoSequences() throws SQLException {
        LocalDate start = LocalDate.of(2026, 3, 1);
        for (int i = 0; i < 25; i++) {
            LocalDate date = start.plusDays(i);
            execute(
                "insert into RECEPTION_NO_SEQUENCE (SEQ_DATE, SEQ_TYPE, LAST_NO) values (?, ?, ?)",
                sqlDate(date), "SMP_OPD", 80 + i
            );
            execute(
                "insert into RECEPTION_NO_SEQUENCE (SEQ_DATE, SEQ_TYPE, LAST_NO) values (?, ?, ?)",
                sqlDate(date), "SMP_EMG", 40 + i
            );
        }
    }

    private List<DoctorScheduleRef> buildSchedules() {
        List<DoctorScheduleRef> schedules = new ArrayList<>();
        LocalDate startDate = LocalDate.of(2026, 3, 10);

        for (int i = 0; i < SCHEDULE_COUNT; i++) {
            DoctorScheduleRef schedule = new DoctorScheduleRef();
            schedule.id = SAMPLE_SCHEDULE_BASE + i;
            schedule.department = clinicalDepartments.get(i % clinicalDepartments.size());
            schedule.doctorId = doctor.id;
            schedule.date = startDate.plusDays(i % 20);
            schedule.startHour = i % 3 == 0 ? 9 : (i % 3 == 1 ? 10 : 14);
            schedule.endHour = schedule.startHour + 2;
            schedule.maxCapacity = 6;
            schedules.add(schedule);
        }

        return schedules;
    }

    private void insertSchedules(List<DoctorScheduleRef> schedules) throws SQLException {
        for (DoctorScheduleRef schedule : schedules) {
            execute(
                "insert into APPT_DOCTOR_SCHEDULE (" +
                    "SCHEDULE_ID, DOCTOR_ID, DEPT_ID, SCHEDULE_DATE, START_TIME, END_TIME, MAX_CAPACITY, ACTIVE_YN" +
                ") values (?, ?, ?, ?, ?, ?, ?, 'Y')",
                schedule.id,
                schedule.doctorId,
                schedule.department.id,
                sqlDate(schedule.date),
                String.format("%02d:00", schedule.startHour),
                String.format("%02d:00", schedule.endHour),
                schedule.maxCapacity
            );
        }
    }

    private List<AppointmentRef> buildAppointments(List<DoctorScheduleRef> schedules) {
        List<AppointmentRef> appointments = new ArrayList<>();
        LocalDate baseDate = LocalDate.of(2026, 3, 10);

        for (int i = 0; i < APPOINTMENT_COUNT; i++) {
            DoctorScheduleRef schedule = schedules.get(i % schedules.size());
            PatientRef patient = patients.get(i % patients.size());
            AppointmentRef appointment = new AppointmentRef();
            appointment.id = SAMPLE_APPOINTMENT_BASE + i;
            appointment.slotId = SAMPLE_SLOT_BASE + i;
            appointment.scheduleId = schedule.id;
            appointment.patient = patient;
            appointment.departmentId = schedule.department.id;
            appointment.doctorId = doctor.id;
            appointment.dateTime = LocalDateTime.of(schedule.date, java.time.LocalTime.of(schedule.startHour, (i % 3) * 20));
            appointment.status = i < 60 ? "COMPLETED" : (i < 75 ? "CONFIRMED" : "CANCELED");
            appointment.apptNo = String.format("APT-%s-%04d", baseDate.plusDays(i % 20).toString().replace("-", ""), i + 1);
            appointment.reason = symptomSamples.get(i % symptomSamples.size());
            appointments.add(appointment);
        }

        return appointments;
    }

    private void insertAppointments(List<AppointmentRef> appointments) throws SQLException {
        for (int i = 0; i < appointments.size(); i++) {
            AppointmentRef appointment = appointments.get(i);
            execute(
                "insert into APPT_RESERVATION (" +
                    "APPT_ID, APPT_NO, PATIENT_ID, DEPT_ID, DOCTOR_ID, SCHEDULE_ID, SLOT_ID, " +
                    "APPT_DATETIME, APPT_STATUS_CD, REASON, CHANNEL_CD, ACTIVE_YN, " +
                    "CREATED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Y', ?, ?, ?, ?)",
                appointment.id,
                appointment.apptNo,
                appointment.patient.id,
                appointment.departmentId,
                appointment.doctorId,
                appointment.scheduleId,
                appointment.slotId,
                sqlTimestamp(appointment.dateTime),
                appointment.status,
                appointment.reason,
                i % 3 == 0 ? "APP" : (i % 3 == 1 ? "PHONE" : "COUNTER"),
                reception.id,
                sqlTimestamp(appointment.dateTime.minusDays(3)),
                reception.id,
                sqlTimestamp(appointment.dateTime.minusHours(1))
            );
        }
    }

    private void insertTimeSlots(List<DoctorScheduleRef> schedules, List<AppointmentRef> appointments) throws SQLException {
        Map<Long, AppointmentRef> appointmentBySlotId = new HashMap<>();
        for (AppointmentRef appointment : appointments) {
            appointmentBySlotId.put(appointment.slotId, appointment);
        }

        for (int i = 0; i < SLOT_COUNT; i++) {
            long slotId = SAMPLE_SLOT_BASE + i;
            DoctorScheduleRef schedule = schedules.get(i % schedules.size());
            LocalDateTime startTime = LocalDateTime.of(schedule.date, java.time.LocalTime.of(schedule.startHour, (i % 3) * 20));
            AppointmentRef appointment = appointmentBySlotId.get(slotId);
            String status = appointment == null ? "AVAILABLE" : (appointment.status.equals("CANCELED") ? "BLOCKED" : "RESERVED");

            execute(
                "insert into APPT_TIME_SLOT (" +
                    "SLOT_ID, SCHEDULE_ID, SLOT_START_DATETIME, SLOT_END_DATETIME, SLOT_STATUS_CD, APPT_ID" +
                ") values (?, ?, ?, ?, ?, ?)",
                slotId,
                schedule.id,
                sqlTimestamp(startTime),
                sqlTimestamp(startTime.plusMinutes(20)),
                status,
                appointment == null ? null : appointment.id
            );
        }
    }

    private void seedAppointmentStatusHistory(List<AppointmentRef> appointments) throws SQLException {
        long id = SAMPLE_APPT_STATUS_BASE;
        for (AppointmentRef appointment : appointments) {
            execute(
                "insert into APPT_RESERVATION_STATUS_HIS (" +
                    "APPT_STATUS_HIS_ID, APPT_ID, BEFORE_STATUS_CD, AFTER_STATUS_CD, CHANGED_AT, CHANGED_BY, CHANGE_REASON" +
                ") values (?, ?, ?, ?, ?, ?, ?)",
                id++,
                appointment.id,
                null,
                "BOOKED",
                sqlTimestamp(appointment.dateTime.minusDays(3)),
                reception.id,
                "예약 등록"
            );
            execute(
                "insert into APPT_RESERVATION_STATUS_HIS (" +
                    "APPT_STATUS_HIS_ID, APPT_ID, BEFORE_STATUS_CD, AFTER_STATUS_CD, CHANGED_AT, CHANGED_BY, CHANGE_REASON" +
                ") values (?, ?, ?, ?, ?, ?, ?)",
                id++,
                appointment.id,
                "BOOKED",
                appointment.status,
                sqlTimestamp(appointment.dateTime.minusHours(1)),
                reception.id,
                appointment.status.equals("CANCELED") ? "예약 취소" : "내원 상태 반영"
            );
        }
    }

    private List<ReceptionRef> buildReceptions(List<AppointmentRef> appointments) {
        List<ReceptionRef> receptions = new ArrayList<>();

        for (int i = 0; i < RECEPTION_COUNT; i++) {
            ReceptionRef receptionRef = new ReceptionRef();
            receptionRef.id = SAMPLE_RECEPTION_BASE + i;

            if (i < 60) {
                AppointmentRef appointment = appointments.get(i);
                receptionRef.patient = appointment.patient;
                receptionRef.departmentId = appointment.departmentId;
                receptionRef.doctorId = appointment.doctorId;
                receptionRef.sourceApptId = appointment.id;
                receptionRef.receptionType = "OUTPATIENT";
                receptionRef.dateTime = appointment.dateTime.plusMinutes(10);
                receptionRef.status = i < 50 ? "CLOSED" : "IN_PROGRESS";
            } else {
                PatientRef patient = patients.get(i % patients.size());
                DepartmentRef department = clinicalDepartments.get((i + 2) % clinicalDepartments.size());
                receptionRef.patient = patient;
                receptionRef.departmentId = department.id;
                receptionRef.doctorId = doctor.id;
                receptionRef.sourceApptId = null;
                receptionRef.receptionType = "EMERGENCY";
                receptionRef.dateTime = LocalDateTime.of(2026, 3, 12 + (i % 10), 18 + (i % 4), (i % 3) * 15);
                receptionRef.status = i < 90 ? "CLOSED" : "IN_PROGRESS";
            }

            receptionRef.receptionNo = String.format(
                "RCP-%s-%04d",
                receptionRef.dateTime.toLocalDate().toString().replace("-", ""),
                i + 1
            );
            receptionRef.symptomSummary = symptomSamples.get(i % symptomSamples.size());
            receptions.add(receptionRef);
        }

        return receptions;
    }

    private void insertReceptions(List<ReceptionRef> receptions) throws SQLException {
        for (ReceptionRef receptionRef : receptions) {
            execute(
                "insert into RCPT_RECEPTION (" +
                    "RECEPTION_ID, RECEPTION_NO, PATIENT_ID, RECEPTION_TYPE_CD, RECEPTION_STATUS_CD, " +
                    "RECEPTION_DATETIME, DEPT_ID, DOCTOR_ID, SYMPTOM_SUMMARY, SOURCE_APPT_ID, ACTIVE_YN, " +
                    "CREATED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Y', ?, ?, ?, ?)",
                receptionRef.id,
                receptionRef.receptionNo,
                receptionRef.patient.id,
                receptionRef.receptionType,
                receptionRef.status,
                sqlTimestamp(receptionRef.dateTime),
                receptionRef.departmentId,
                receptionRef.doctorId,
                receptionRef.symptomSummary,
                receptionRef.sourceApptId,
                reception.id,
                sqlTimestamp(receptionRef.dateTime.minusMinutes(15)),
                reception.id,
                sqlTimestamp(receptionRef.dateTime.plusMinutes(30))
            );
        }
    }

    private void seedAppointmentConversions(List<ReceptionRef> receptions) throws SQLException {
        long id = SAMPLE_APPT_TO_RCPT_BASE;
        for (ReceptionRef receptionRef : receptions) {
            if (receptionRef.sourceApptId == null) {
                continue;
            }

            execute(
                "insert into APPT_TO_RCPT_CONVERSION_HIS (" +
                    "CONVERSION_ID, APPT_ID, RECEPTION_ID, CONVERTED_AT, CONVERTED_BY, RESULT_CD, MESSAGE" +
                ") values (?, ?, ?, ?, ?, ?, ?)",
                id++,
                receptionRef.sourceApptId,
                receptionRef.id,
                sqlTimestamp(receptionRef.dateTime.minusMinutes(5)),
                reception.id,
                "SUCCESS",
                "예약이 접수로 전환되었습니다."
            );
        }
    }

    private void seedReceptionSideTables(List<ReceptionRef> receptions, List<String> closureReasonCodes) throws SQLException {
        long auditId = SAMPLE_RECEPTION_AUDIT_BASE;
        long statusHistoryId = SAMPLE_RECEPTION_STATUS_BASE;
        long outpatientId = SAMPLE_OUTPATIENT_BASE;
        long snapshotId = SAMPLE_QUAL_SNAPSHOT_BASE;
        long snapshotItemId = SAMPLE_QUAL_ITEM_BASE;
        long queueId = SAMPLE_WAITING_QUEUE_BASE;
        long callHistoryId = SAMPLE_CALL_HISTORY_BASE;
        long closureId = SAMPLE_VISIT_CLOSURE_BASE;
        long closureHistoryId = SAMPLE_VISIT_CLOSURE_HIS_BASE;
        long triageId = SAMPLE_RECEPTION_BASE;
        long emergencyId = SAMPLE_RECEPTION_BASE + 500L;

        for (int i = 0; i < receptions.size(); i++) {
            ReceptionRef receptionRef = receptions.get(i);

            execute(
                "insert into RCPT_RECEPTION_AUDIT (" +
                    "AUDIT_ID, RECEPTION_ID, CHANGE_TYPE_CD, CHANGE_FIELD_NM, BEFORE_VALUE, AFTER_VALUE, CHANGE_REASON, CHANGED_BY, CHANGED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                auditId++,
                receptionRef.id,
                "STATUS",
                "RECEPTION_STATUS_CD",
                "WAITING",
                receptionRef.status,
                "샘플 접수 진행",
                reception.id,
                sqlTimestamp(receptionRef.dateTime.plusMinutes(5))
            );

            execute(
                "insert into RCPT_STATUS_HISTORY (" +
                    "STATUS_HISTORY_ID, RECEPTION_ID, BEFORE_STATUS_CD, AFTER_STATUS_CD, CHANGE_DATETIME, CHANGE_USER_ID, CHANGE_REASON, EVENT_SOURCE_CD" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?)",
                statusHistoryId++,
                receptionRef.id,
                null,
                "WAITING",
                sqlTimestamp(receptionRef.dateTime.minusMinutes(3)),
                reception.id,
                "접수 생성",
                "FRONT"
            );
            execute(
                "insert into RCPT_STATUS_HISTORY (" +
                    "STATUS_HISTORY_ID, RECEPTION_ID, BEFORE_STATUS_CD, AFTER_STATUS_CD, CHANGE_DATETIME, CHANGE_USER_ID, CHANGE_REASON, EVENT_SOURCE_CD" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?)",
                statusHistoryId++,
                receptionRef.id,
                "WAITING",
                receptionRef.status,
                sqlTimestamp(receptionRef.dateTime.plusMinutes(20)),
                reception.id,
                "진행 상태 반영",
                "SYSTEM"
            );

            if ("OUTPATIENT".equals(receptionRef.receptionType)) {
                execute(
                    "insert into RCPT_OUTPATIENT_DETAIL (" +
                        "OUTPATIENT_DETAIL_ID, RECEPTION_ID, VISIT_PURPOSE_CD, RESERVATION_LINK_ID, PRIMARY_SYMPTOM, CONSULTATION_TYPE_CD, " +
                        "INSURANCE_APPLY_YN, ACTIVE_YN, CREATED_AT, UPDATED_AT" +
                    ") values (?, ?, ?, ?, ?, ?, ?, 'Y', ?, ?)",
                    outpatientId++,
                    receptionRef.id,
                    visitPurposes.get(i % visitPurposes.size()),
                    receptionRef.sourceApptId,
                    receptionRef.symptomSummary,
                    consultationTypes.get(i % consultationTypes.size()),
                    i % 4 == 0 ? "N" : "Y",
                    sqlTimestamp(receptionRef.dateTime),
                    sqlTimestamp(receptionRef.dateTime.plusMinutes(15))
                );
            } else {
                execute(
                    "insert into EMR_TRIAGE (" +
                        "TRIAGE_ID, RECEPTION_ID, TRIAGE_LEVEL_CD, TRIAGE_DATETIME, TRIAGE_USER_ID, TRIAGE_NOTE, PRIORITY_SCORE, ACTIVE_YN" +
                    ") values (?, ?, ?, ?, ?, ?, ?, 'Y')",
                    triageId++,
                    receptionRef.id,
                    "LEVEL_" + ((i % 5) + 1),
                    sqlTimestamp(receptionRef.dateTime.plusMinutes(2)),
                    nurse.id,
                    "응급도 분류 완료",
                    (i % 5) + 1
                );

                execute(
                    "insert into EMR_EMERGENCY_DETAIL (" +
                        "EMERGENCY_DETAIL_ID, RECEPTION_ID, ARRIVAL_DATETIME, ARRIVAL_ROUTE_CD, ARRIVAL_TRANSPORT_CD, " +
                        "CONSCIOUSNESS_CD, BLOOD_PRESSURE, PULSE_RATE, TEMPERATURE, EMERGENCY_NOTE, ACTIVE_YN" +
                    ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Y')",
                    emergencyId++,
                    receptionRef.id,
                    sqlTimestamp(receptionRef.dateTime),
                    emergencyRoutes.get(i % emergencyRoutes.size()),
                    i % 3 == 0 ? "AMBULANCE" : "PRIVATE_CAR",
                    i % 4 == 0 ? "DROWSY" : "ALERT",
                    (110 + (i % 20)) + "/" + (70 + (i % 15)),
                    70 + (i % 30),
                    36.4 + (i % 5) * 0.1,
                    "응급실 초기평가 완료"
                );
            }

            long currentSnapshotId = snapshotId++;
            execute(
                "insert into RCPT_QUALIFICATION_SNAPSHOT (" +
                    "SNAPSHOT_ID, RECEPTION_ID, PATIENT_ID, SNAPSHOT_DATETIME, RESULT_CD, PAYER_TYPE_CD, INSURANCE_TYPE_CD, VALID_YN, SOURCE_SYSTEM_CD" +
                ") values (?, ?, ?, ?, ?, ?, ?, 'Y', ?)",
                currentSnapshotId,
                receptionRef.id,
                receptionRef.patient.id,
                sqlTimestamp(receptionRef.dateTime.plusMinutes(1)),
                i % 5 == 0 ? "REVIEW" : "PASS",
                i % 3 == 0 ? "SELF" : "NHI",
                i % 4 == 0 ? "SPECIAL" : "GENERAL",
                "SAMPLE_ENGINE"
            );

            execute(
                "insert into RCPT_QUALIFICATION_ITEM (" +
                    "SNAPSHOT_ITEM_ID, SNAPSHOT_ID, ITEM_NAME, ITEM_VALUE, ITEM_STATUS_CD, DISPLAY_ORDER" +
                ") values (?, ?, ?, ?, ?, ?)",
                snapshotItemId++,
                currentSnapshotId,
                "보험유형",
                i % 3 == 0 ? "본인부담" : "건강보험",
                "VALID",
                1
            );
            execute(
                "insert into RCPT_QUALIFICATION_ITEM (" +
                    "SNAPSHOT_ITEM_ID, SNAPSHOT_ID, ITEM_NAME, ITEM_VALUE, ITEM_STATUS_CD, DISPLAY_ORDER" +
                ") values (?, ?, ?, ?, ?, ?)",
                snapshotItemId++,
                currentSnapshotId,
                "자격확인일시",
                receptionRef.dateTime.toString(),
                "VALID",
                2
            );
            execute(
                "insert into RCPT_QUALIFICATION_ITEM (" +
                    "SNAPSHOT_ITEM_ID, SNAPSHOT_ID, ITEM_NAME, ITEM_VALUE, ITEM_STATUS_CD, DISPLAY_ORDER" +
                ") values (?, ?, ?, ?, ?, ?)",
                snapshotItemId++,
                currentSnapshotId,
                "판정결과",
                i % 5 == 0 ? "추가확인 필요" : "정상",
                i % 5 == 0 ? "REVIEW" : "VALID",
                3
            );

            long currentQueueId = queueId++;
            execute(
                "insert into RCPT_WAITING_QUEUE (" +
                    "QUEUE_ID, RECEPTION_ID, QUEUE_NO, QUEUE_STATUS_CD, QUEUE_ORDER_NO, DEPT_ID, DOCTOR_ID, ESTIMATED_WAIT_MIN, ACTIVE_YN, CREATED_AT, UPDATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, 'Y', ?, ?)",
                currentQueueId,
                receptionRef.id,
                String.format("Q-%03d", i + 1),
                receptionRef.status.equals("IN_PROGRESS") ? "CALLED" : "DONE",
                i + 1,
                receptionRef.departmentId,
                receptionRef.doctorId,
                10 + (i % 25),
                sqlTimestamp(receptionRef.dateTime.plusMinutes(2)),
                sqlTimestamp(receptionRef.dateTime.plusMinutes(20))
            );

            execute(
                "insert into RCPT_CALL_HISTORY (" +
                    "CALL_HISTORY_ID, QUEUE_ID, CALL_DATETIME, CALL_USER_ID, CALL_COUNT, CALL_RESULT_CD, REMARK" +
                ") values (?, ?, ?, ?, ?, ?, ?)",
                callHistoryId++,
                currentQueueId,
                sqlTimestamp(receptionRef.dateTime.plusMinutes(18)),
                nurse.id,
                1,
                receptionRef.status.equals("IN_PROGRESS") ? "CALLED" : "RESPONDED",
                "진료실 호출"
            );

            if (i < 80) {
                long currentClosureId = closureId++;
                String closureStatus = closureStatuses.get(i % closureStatuses.size());
                execute(
                    "insert into RCPT_VISIT_CLOSURE (" +
                        "CLOSURE_ID, RECEPTION_ID, CLOSURE_STATUS_CD, CLOSURE_DATETIME, CLOSURE_USER_ID, CLOSURE_REASON_CD, REMARK, ACTIVE_YN" +
                    ") values (?, ?, ?, ?, ?, ?, ?, 'Y')",
                    currentClosureId,
                    receptionRef.id,
                    closureStatus,
                    sqlTimestamp(receptionRef.dateTime.plusHours(1)),
                    reception.id,
                    closureReasonCodes.get(i % closureReasonCodes.size()),
                    "외래 종료 처리"
                );

                execute(
                    "insert into RCPT_VISIT_CLOSURE_HIS (" +
                        "CLOSURE_HISTORY_ID, CLOSURE_ID, BEFORE_STATUS_CD, AFTER_STATUS_CD, CHANGED_BY, CHANGED_AT, CHANGE_REASON" +
                    ") values (?, ?, ?, ?, ?, ?, ?)",
                    closureHistoryId++,
                    currentClosureId,
                    "IN_PROGRESS",
                    closureStatus,
                    reception.id,
                    sqlTimestamp(receptionRef.dateTime.plusHours(1)),
                    "방문 종료 기록"
                );
            }
        }
    }

    private List<ClinicalVisitRef> buildClinicalVisits(List<ReceptionRef> receptions) {
        List<ClinicalVisitRef> visits = new ArrayList<>();
        for (int i = 0; i < receptions.size(); i++) {
            ReceptionRef receptionRef = receptions.get(i);
            ClinicalVisitRef visit = new ClinicalVisitRef();
            visit.id = SAMPLE_CLINICAL_VISIT_BASE + i;
            visit.receptionId = receptionRef.id;
            visit.patient = receptionRef.patient;
            visit.doctorId = receptionRef.doctorId;
            visit.startTime = receptionRef.dateTime.plusMinutes(25);
            visit.endTime = receptionRef.dateTime.plusMinutes(55);
            visit.status = receptionRef.status.equals("IN_PROGRESS") ? "IN_PROGRESS" : "DONE";
            visits.add(visit);
        }
        return visits;
    }

    private void insertClinicalVisits(List<ClinicalVisitRef> clinicalVisits) throws SQLException {
        for (ClinicalVisitRef visit : clinicalVisits) {
            execute(
                "insert into CLINICAL_VISIT (" +
                    "VISIT_ID, PATIENT_ID, DOCTOR_ID, RECEPTION_ID, VISIT_STATUS, START_TIME, END_TIME, CREATED_AT, UPDATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                visit.id,
                visit.patient.id,
                visit.doctorId,
                visit.receptionId,
                visit.status,
                sqlTimestamp(visit.startTime),
                sqlTimestamp(visit.endTime),
                sqlTimestamp(visit.startTime.minusMinutes(5)),
                sqlTimestamp(visit.endTime)
            );
        }
    }

    private void seedClinicalSideTables(List<ClinicalVisitRef> clinicalVisits) throws SQLException {
        long queueId = SAMPLE_CLINICAL_QUEUE_BASE;
        long statusHistoryId = SAMPLE_CLINICAL_VISIT_STATUS_BASE;
        long noteId = SAMPLE_NOTE_BASE;
        long attachmentId = SAMPLE_NOTE_ATTACHMENT_BASE;
        long noteHistoryId = SAMPLE_NOTE_HISTORY_BASE;
        long soapId = SAMPLE_SOAP_BASE;
        long diagnosisId = SAMPLE_DIAGNOSIS_BASE;
        long clinicalNoteId = SAMPLE_CLINICAL_NOTE_BASE;
        long clinicalNoteHistoryId = SAMPLE_CLINICAL_NOTE_HISTORY_BASE;
        long clinicalOrderId = SAMPLE_CLINICAL_ORDER_BASE;
        long orderItemId = SAMPLE_CLINICAL_ORDER_ITEM_BASE;
        long orderResultId = SAMPLE_CLINICAL_ORDER_RESULT_BASE;
        long medicalOrderId = SAMPLE_MEDICAL_ORDER_BASE;
        long legacyOrderResultId = SAMPLE_ORDER_RESULT_BASE;
        long procedureResultId = SAMPLE_PROCEDURE_RESULT_BASE;

        for (int i = 0; i < clinicalVisits.size(); i++) {
            ClinicalVisitRef visit = clinicalVisits.get(i);
            String assessment = assessmentSamples.get(i % assessmentSamples.size());
            String plan = planSamples.get(i % planSamples.size());
            String diagnosisCode = diagnosisCodes.get(i % diagnosisCodes.size());
            String diagnosisName = diagnosisNames.get(i % diagnosisNames.size());
            String chiefComplaint = symptomSamples.get(i % symptomSamples.size());
            long currentNoteId = noteId++;
            long currentClinicalNoteId = clinicalNoteId++;
            long currentClinicalOrderId = clinicalOrderId++;

            execute(
                "insert into CLINICAL_VISIT_QUEUE (QUEUE_ID, VISIT_ID, QUEUE_ORDER, ROOM_ID, CREATED_AT) values (?, ?, ?, ?, ?)",
                queueId++,
                visit.id,
                i + 1,
                300 + (i % 15),
                sqlTimestamp(visit.startTime.minusMinutes(2))
            );

            execute(
                "insert into CLINICAL_VISIT_STATUS_HISTORY (HISTORY_ID, VISIT_ID, STATUS, CHANGED_AT) values (?, ?, ?, ?)",
                statusHistoryId++,
                visit.id,
                "WAITING",
                sqlTimestamp(visit.startTime.minusMinutes(10))
            );
            execute(
                "insert into CLINICAL_VISIT_STATUS_HISTORY (HISTORY_ID, VISIT_ID, STATUS, CHANGED_AT) values (?, ?, ?, ?)",
                statusHistoryId++,
                visit.id,
                visit.status,
                sqlTimestamp(visit.endTime)
            );

            execute(
                "insert into NOTE (" +
                    "NOTE_ID, VISIT_ID, CHIEF_COMPLAINT, PRESENT_ILLNESS, ASSESSMENT, PLAN, MEMO, STATUS, CREATED_AT, UPDATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                currentNoteId,
                visit.id,
                chiefComplaint,
                chiefComplaint + " 증상이 2일 전부터 지속되었다고 진술함.",
                assessment,
                plan,
                "기본 진료 노트 샘플",
                "FINAL",
                sqlTimestamp(visit.startTime),
                sqlTimestamp(visit.endTime)
            );

            execute(
                "insert into NOTE_ATTACHMENT (ATTACHMENT_ID, NOTE_ID, FILE_NAME, FILE_PATH, FILE_TYPE, CREATED_AT) values (?, ?, ?, ?, ?, ?)",
                attachmentId++,
                currentNoteId,
                "visit_note_" + String.format("%03d", i + 1) + ".pdf",
                "/sample/notes/visit_note_" + String.format("%03d", i + 1) + ".pdf",
                "application/pdf",
                sqlTimestamp(visit.endTime)
            );

            execute(
                "insert into NOTE_HISTORY (HISTORY_ID, NOTE_ID, CHANGE_TYPE, CHANGED_AT, CHANGED_BY) values (?, ?, ?, ?, ?)",
                noteHistoryId++,
                currentNoteId,
                "UPDATE",
                sqlTimestamp(visit.endTime),
                doctor.id
            );

            insertSoapSection(soapId++, currentNoteId, "S", chiefComplaint + " 호소");
            insertSoapSection(soapId++, currentNoteId, "O", "체온 36.8도, 혈압 안정적");
            insertSoapSection(soapId++, currentNoteId, "A", assessment);
            insertSoapSection(soapId++, currentNoteId, "P", plan);

            execute(
                "insert into CLINICAL_DIAGNOSIS (DIAGNOSIS_ID, NOTE_ID, PATIENT_CODE, DIAGNOSIS_CODE, DESCRIPTION, CREATED_AT) values (?, ?, ?, ?, ?, ?)",
                diagnosisId++,
                currentNoteId,
                visit.patient.patientNo,
                diagnosisCode,
                diagnosisName,
                sqlTimestamp(visit.endTime)
            );

            execute(
                "insert into CLINICAL_NOTE (" +
                    "NOTE_ID, VISIT_ID, CHIEF_COMPLAINT, PRESENT_ILLNESS, ASSESSMENT, PLAN, MEMO, STATUS, CREATED_AT, UPDATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                currentClinicalNoteId,
                visit.id,
                chiefComplaint,
                chiefComplaint + "에 대한 추가 임상 메모",
                assessment,
                plan,
                "임상 노트 샘플",
                "SIGNED",
                sqlTimestamp(visit.startTime.plusMinutes(5)),
                sqlTimestamp(visit.endTime)
            );

            execute(
                "insert into CLINICAL_NOTE_HISTORY (HISTORY_ID, CHANGE_TYPE, CHANGED_AT, CHANGED_BY, NOTE_ID) values (?, ?, ?, ?, ?)",
                clinicalNoteHistoryId++,
                "SIGN",
                sqlTimestamp(visit.endTime),
                doctor.id,
                currentClinicalNoteId
            );

            execute(
                "insert into CLINICAL_ORDER (" +
                    "ORDER_ID, VISIT_ID, ORDER_TYPE, ORDER_STATUS, DOCTOR_ID, ORDER_DATE, CREATED_AT, UPDATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?)",
                currentClinicalOrderId,
                visit.id,
                i % 2 == 0 ? "LAB" : "IMAGING",
                "COMPLETED",
                doctor.id,
                sqlTimestamp(visit.startTime.plusMinutes(10)),
                sqlTimestamp(visit.startTime.plusMinutes(10)),
                sqlTimestamp(visit.endTime)
            );

            for (int j = 0; j < 2; j++) {
                long currentOrderItemId = orderItemId++;
                String itemName = orderItemNames.get((i + j) % orderItemNames.size());

                execute(
                    "insert into CLINICAL_ORDER_ITEM (" +
                        "ORDER_ITEM_ID, ORDER_ID, ITEM_CODE, DOSE, FREQUENCY, DURATION, CREATED_AT" +
                    ") values (?, ?, ?, ?, ?, ?, ?)",
                    currentOrderItemId,
                    currentClinicalOrderId,
                    itemName,
                    1 + (j % 2),
                    j == 0 ? "1회" : "2회",
                    j == 0 ? "당일" : "3일",
                    sqlTimestamp(visit.startTime.plusMinutes(10))
                );

                execute(
                    "insert into CLINICAL_ORDER_RESULT (" +
                        "RESULT_ID, ORDER_ITEM_ID, RESULT_VALUE, RESULT_STATUS, RESULT_DATE, CREATED_AT" +
                    ") values (?, ?, ?, ?, ?, ?)",
                    orderResultId++,
                    currentOrderItemId,
                    itemName.contains("X-ray") || itemName.contains("초음파") ? "특이 소견 없음" : "정상 범위",
                    "VERIFIED",
                    sqlTimestamp(visit.endTime),
                    sqlTimestamp(visit.endTime)
                );

                if (j == 0) {
                    execute(
                        "insert into ORDER_RESULT (" +
                            "RESULT_ID, CREATED_AT, ORDER_ITEM_ID, RESULT_DATE, RESULT_STATUS, RESULT_VALUE" +
                        ") values (?, ?, ?, ?, ?, ?)",
                        legacyOrderResultId++,
                        sqlTimestamp(visit.endTime),
                        currentOrderItemId,
                        sqlTimestamp(visit.endTime),
                        "COMPLETED",
                        "정상"
                    );
                } else {
                    execute(
                        "insert into PROCEDURE_RESULT (" +
                            "PROCEDURE_RESULT_ID, ORDER_ITEM_ID, STATUS, PERFORMED_AT, PERFORMER_ID, CONTENT" +
                        ") values (?, ?, ?, ?, ?, ?)",
                        procedureResultId++,
                        currentOrderItemId,
                        "DONE",
                        sqlTimestamp(visit.endTime.minusMinutes(5)),
                        nurse.id,
                        itemName + " 시행 후 환자 상태 안정적"
                    );
                }

                execute(
                    "insert into SUPPORT_TEST_EXECUTION (" +
                        "TEST_EXECUTION_ID, ORDER_ITEM_ID, PROGRESS_STATUS, STARTED_AT, COMPLETED_AT, EXECUTION_TYPE, " +
                        "PERFORMER_ID, PERFORMER_DEPT_ID, EXECUTION_LOCATION_ID, CREATED_AT, UPDATED_AT" +
                    ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    "SMP-TEX-" + String.format("%04d", i * 2 + j + 1),
                    String.valueOf(currentOrderItemId),
                    "COMPLETED",
                    sqlTimestamp(visit.startTime.plusMinutes(15)),
                    sqlTimestamp(visit.endTime.minusMinutes(3)),
                    j == 0 ? "LAB" : "PROCEDURE",
                    j == 0 ? nurse.username : doctor.username,
                    String.valueOf(visit.patient.id % 5 + 8),
                    j == 0 ? "LAB-01" : "PROC-01",
                    sqlTimestamp(visit.startTime.plusMinutes(15)),
                    sqlTimestamp(visit.endTime.minusMinutes(3))
                );
            }

            execute(
                "insert into MEDICAL_ORDER (" +
                    "ORDER_ID, CREATED_AT, DOCTOR_ID, ORDER_DATE, ORDER_STATUS, ORDER_TYPE, UPDATED_AT, VISIT_ID" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?)",
                medicalOrderId++,
                sqlTimestamp(visit.startTime.plusMinutes(12)),
                doctor.id,
                sqlTimestamp(visit.startTime.plusMinutes(12)),
                "COMPLETED",
                i % 2 == 0 ? "MEDICATION" : "FOLLOWUP",
                sqlTimestamp(visit.endTime),
                visit.id
            );
        }
    }

    private void insertSoapSection(long sectionId, long noteId, String sectionType, String content) throws SQLException {
        execute(
            "insert into SOAP_SECTION (SECTION_ID, CONTENT, CREATED_AT, NOTE_ID, SECTION_TYPE) values (?, ?, ?, ?, ?)",
            sectionId,
            content,
            sqlTimestamp(LocalDateTime.of(2026, 3, 15, 9, 0)),
            noteId,
            sectionType
        );
    }

    private List<AdmissionRef> buildAdmissions(List<ReceptionRef> receptions) {
        List<AdmissionRef> admissions = new ArrayList<>();
        int startIndex = receptions.size() - EMERGENCY_RECEPTION_COUNT;

        for (int i = 0; i < ADMISSION_COUNT; i++) {
            ReceptionRef receptionRef = receptions.get(startIndex + i);
            AdmissionRef admission = new AdmissionRef();
            admission.id = SAMPLE_ADMISSION_BASE + i;
            admission.reception = receptionRef;
            admission.patient = receptionRef.patient;
            admission.admissionDateTime = receptionRef.dateTime.plusHours(2);
            admissions.add(admission);
        }

        return admissions;
    }

    private void insertAdmissions(List<AdmissionRef> admissions) throws SQLException {
        for (int i = 0; i < admissions.size(); i++) {
            AdmissionRef admission = admissions.get(i);
            execute(
                "insert into ADM_INPATIENT_ADMISSION (" +
                    "ADMISSION_ID, RECEPTION_ID, PATIENT_ID, ADMISSION_STATUS_CD, ADMISSION_TYPE_CD, ADMISSION_DATETIME, " +
                    "ADMISSION_REASON, DEPT_ID, ATTENDING_DOCTOR_ID, ACTIVE_YN, CREATED_AT, UPDATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Y', ?, ?)",
                admission.id,
                admission.reception.id,
                admission.patient.id,
                "ADMITTED",
                i % 3 == 0 ? "EMERGENCY" : "OBSERVATION",
                sqlTimestamp(admission.admissionDateTime),
                "응급 처치 후 입원 필요",
                4L,
                doctor.id,
                sqlTimestamp(admission.admissionDateTime),
                sqlTimestamp(admission.admissionDateTime.plusMinutes(30))
            );
        }
    }

    private void seedAdmissionSideTables(List<AdmissionRef> admissions) throws SQLException {
        for (int i = 0; i < admissions.size(); i++) {
            AdmissionRef admission = admissions.get(i);
            long decisionId = SAMPLE_ADMISSION_DECISION_BASE + i;
            long bedAssignmentId = SAMPLE_BED_ASSIGNMENT_BASE + i;

            execute(
                "insert into ADM_ADMISSION_DECISION (" +
                    "DECISION_ID, ADMISSION_ID, DECISION_DATETIME, DECISION_DOCTOR_ID, DECISION_REASON, EXPECTED_DAYS, DECISION_NOTE" +
                ") values (?, ?, ?, ?, ?, ?, ?)",
                decisionId,
                admission.id,
                sqlTimestamp(admission.admissionDateTime.minusMinutes(20)),
                doctor.id,
                "지속적 관찰 필요",
                3 + (i % 5),
                "입원 경과 관찰 계획"
            );

            execute(
                "insert into ADM_BED_ASSIGNMENT (" +
                    "BED_ASSIGNMENT_ID, ADMISSION_ID, WARD_ID, ROOM_ID, BED_ID, ASSIGNMENT_DATETIME, ASSIGNMENT_STATUS_CD, ASSIGNED_BY, REMARK" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                bedAssignmentId,
                admission.id,
                10 + (i % 4),
                200 + (i % 10),
                300 + (i % 20),
                sqlTimestamp(admission.admissionDateTime.plusMinutes(30)),
                "ASSIGNED",
                nurse.id,
                "병상 배정 완료"
            );

            execute(
                "insert into ADM_BED_ASSIGNMENT_HIS (" +
                    "BED_ASSIGNMENT_HIS_ID, BED_ASSIGNMENT_ID, BEFORE_BED_ID, AFTER_BED_ID, CHANGE_REASON, CHANGED_BY, CHANGED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?)",
                SAMPLE_BED_ASSIGNMENT_HIS_BASE + i,
                bedAssignmentId,
                null,
                300 + (i % 20),
                "최초 병상 배정",
                nurse.id,
                sqlTimestamp(admission.admissionDateTime.plusMinutes(30))
            );

            execute(
                "insert into ADM_ADMISSION_AUDIT (" +
                    "AUDIT_ID, ADMISSION_ID, CHANGE_FIELD_NM, BEFORE_VALUE, AFTER_VALUE, CHANGE_REASON, CHANGED_BY, CHANGED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?)",
                SAMPLE_ADMISSION_AUDIT_BASE + i,
                admission.id,
                "ADMISSION_STATUS_CD",
                "REQUESTED",
                "ADMITTED",
                "입원 확정",
                admin.id,
                sqlTimestamp(admission.admissionDateTime)
            );
        }
    }

    private List<LegacyVisitRef> buildLegacyVisits(List<ReceptionRef> receptions, List<AdmissionRef> admissions, List<AppointmentRef> appointments) {
        List<LegacyVisitRef> legacyVisits = new ArrayList<>();

        for (int i = 0; i < LEGACY_VISIT_GROUP_COUNT; i++) {
            ReceptionRef receptionRef = receptions.get(i);
            LegacyVisitRef visit = new LegacyVisitRef();
            visit.id = SAMPLE_VISIT_BASE + i;
            visit.patient = receptionRef.patient;
            visit.receptionId = receptionRef.id;
            visit.doctorId = receptionRef.doctorId;
            visit.startTime = receptionRef.dateTime.plusMinutes(10);
            visit.endTime = receptionRef.dateTime.plusMinutes(40);
            visit.status = "COMPLETED";
            visit.groupType = "RESERVATION";
            visit.reservationId = "RSV-" + String.format("%04d", i + 1);
            legacyVisits.add(visit);
        }

        for (int i = 0; i < LEGACY_VISIT_GROUP_COUNT; i++) {
            ReceptionRef receptionRef = receptions.get(RECEPTION_COUNT - EMERGENCY_RECEPTION_COUNT + i);
            LegacyVisitRef visit = new LegacyVisitRef();
            visit.id = SAMPLE_VISIT_BASE + LEGACY_VISIT_GROUP_COUNT + i;
            visit.patient = receptionRef.patient;
            visit.receptionId = receptionRef.id;
            visit.doctorId = receptionRef.doctorId;
            visit.startTime = receptionRef.dateTime.plusMinutes(5);
            visit.endTime = receptionRef.dateTime.plusMinutes(50);
            visit.status = "EMERGENCY";
            visit.groupType = "EMERGENCY";
            legacyVisits.add(visit);
        }

        for (int i = 0; i < LEGACY_VISIT_GROUP_COUNT; i++) {
            AdmissionRef admission = admissions.get(i);
            LegacyVisitRef visit = new LegacyVisitRef();
            visit.id = SAMPLE_VISIT_BASE + LEGACY_VISIT_GROUP_COUNT * 2L + i;
            visit.patient = admission.patient;
            visit.receptionId = admission.reception.id;
            visit.doctorId = doctor.id;
            visit.startTime = admission.admissionDateTime;
            visit.endTime = admission.admissionDateTime.plusDays(2);
            visit.status = "INPATIENT";
            visit.groupType = "INPATIENT";
            legacyVisits.add(visit);
        }

        return legacyVisits;
    }

    private void insertLegacyVisits(List<LegacyVisitRef> legacyVisits) throws SQLException {
        for (LegacyVisitRef visit : legacyVisits) {
            execute(
                "insert into VISIT (" +
                    "VISIT_ID, CREATED_AT, DOCTOR_ID, END_TIME, PATIENT_ID, RECEPTION_ID, START_TIME, UPDATED_AT, VISIT_STATUS" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                visit.id,
                sqlTimestamp(visit.startTime),
                visit.doctorId,
                sqlTimestamp(visit.endTime),
                visit.patient.id,
                visit.receptionId,
                sqlTimestamp(visit.startTime),
                sqlTimestamp(visit.endTime),
                visit.status
            );
        }
    }

    private void seedLegacyVisitSideTables(List<LegacyVisitRef> legacyVisits) throws SQLException {
        for (LegacyVisitRef visit : legacyVisits) {
            if ("RESERVATION".equals(visit.groupType)) {
                execute(
                    "insert into VISIT_RESERVATION (VISIT_ID, RESERVATION_ID, SCHEDULED_AT, ARRIVAL_AT, NOTE, UPDATED_AT) values (?, ?, ?, ?, ?, ?)",
                    visit.id,
                    visit.reservationId,
                    sqlDate(visit.startTime.toLocalDate()),
                    sqlDate(visit.startTime.toLocalDate()),
                    "예약 방문 샘플",
                    sqlDate(visit.endTime.toLocalDate())
                );
            } else if ("EMERGENCY".equals(visit.groupType)) {
                execute(
                    "insert into VISIT_EMERGENCY (VISIT_ID, TRIAGE_LEVEL, AMBULANCE_YN, TRAUMA_YN, NOTE, UPDATED_AT) values (?, ?, ?, ?, ?, ?)",
                    visit.id,
                    "LEVEL_" + ((int) (visit.id % 5) + 1),
                    visit.id % 2 == 0 ? 1 : 0,
                    visit.id % 3 == 0 ? 1 : 0,
                    "응급 방문 샘플",
                    sqlDate(visit.endTime.toLocalDate())
                );
            } else {
                execute(
                    "insert into VISIT_INPATIENT (VISIT_ID, WARD_CODE, ROOM_NO, BED_NO, ADMISSION_AT, NOTE, UPDATED_AT) values (?, ?, ?, ?, ?, ?, ?)",
                    visit.id,
                    "WARD-" + ((visit.id % 4) + 1),
                    "R-" + ((visit.id % 10) + 1),
                    "B-" + ((visit.id % 20) + 1),
                    sqlDate(visit.startTime.toLocalDate()),
                    "입원 방문 샘플",
                    sqlDate(visit.endTime.toLocalDate())
                );
            }
        }
    }

    private void seedEncounterSideTables() throws SQLException {
        for (int i = 0; i < 50; i++) {
            EncounterRef encounter = encounters.get(i % encounters.size());
            String diagnosisCode = diagnosisCodes.get(i % diagnosisCodes.size());
            String diagnosisName = diagnosisNames.get(i % diagnosisNames.size());

            execute(
                "insert into ENCOUNTER_SUMMARY (SUMMARY_ID, CREATED_AT, SUMMARY_TEXT, VISIT_ID) values (?, ?, ?, ?)",
                SAMPLE_ENCOUNTER_SUMMARY_BASE + i,
                sqlTimestamp(LocalDateTime.of(2026, 3, 15, 11, 0).minusDays(i % 7)),
                assessmentSamples.get(i % assessmentSamples.size()) + " / " + planSamples.get(i % planSamples.size()),
                encounter.visitId
            );

            execute(
                "insert into MEDICAL_ENCOUNTER_DIAGNOSIS (" +
                    "ID, ENCOUNTER_ID, DIAGNOSIS_CODE, DIAGNOSIS_NAME, IS_PRIMARY, SORT_ORDER, CREATED_BY, CREATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?)",
                SAMPLE_MEDICAL_ENCOUNTER_DIAG_BASE + i,
                encounter.id,
                diagnosisCode,
                diagnosisName,
                i % 2 == 0 ? "Y" : "N",
                (i % 3) + 1,
                doctor.username,
                sqlDate(daysAgo(i % 14))
            );

            execute(
                "insert into MEDICAL_ENCOUNTER_ASSET (" +
                    "ID, ENCOUNTER_ID, PATIENT_ID, ASSET_TYPE, TEMPLATE_CODE, OBJECT_KEY, CREATED_BY, CREATED_AT" +
                ") values (?, ?, ?, ?, ?, ?, ?, ?)",
                SAMPLE_MEDICAL_ENCOUNTER_ASSET_BASE + i,
                encounter.id,
                encounter.patientId,
                i % 2 == 0 ? "PDF" : "IMAGE",
                i % 2 == 0 ? "CLINIC_NOTE" : "SCAN_IMAGE",
                "/sample/encounter/" + encounter.id + "/asset_" + String.format("%03d", i + 1),
                doctor.username,
                sqlDate(daysAgo(i % 10))
            );
        }
    }

    private void seedBillingSideTables(List<ReceptionRef> receptions, List<String> paymentMethodCodes, List<String> billTypeCodes) throws SQLException {
        List<BillRef> bills = new ArrayList<>();
        for (int i = 0; i < BILL_COUNT; i++) {
            ReceptionRef receptionRef = receptions.get(i % receptions.size());
            long totalAmount = 40000 + (i % 10) * 15000L;
            String status;
            long paidAmount;
            long remainingAmount;

            if (i < 30) {
                status = "PAID";
                paidAmount = totalAmount;
                remainingAmount = 0;
            } else if (i < 60) {
                status = "PARTIAL";
                paidAmount = (long) (totalAmount * 0.6);
                remainingAmount = totalAmount - paidAmount;
            } else {
                status = "CONFIRMED";
                paidAmount = 0;
                remainingAmount = totalAmount;
            }

            BillRef bill = new BillRef();
            bill.id = SAMPLE_BILL_BASE + i;
            bill.patientId = receptionRef.patient.id;
            bill.totalAmount = totalAmount;
            bill.paidAmount = paidAmount;
            bill.remainingAmount = remainingAmount;
            bill.status = status;
            bill.treatmentDate = receptionRef.dateTime.toLocalDate();
            bills.add(bill);

            execute(
                "insert into BILL (BILL_ID, PATIENT_ID, TOTAL_AMOUNT, STATUS, CREATED_AT, TREATMENT_DATE, PAID_AMOUNT, REMAINING_AMOUNT) values (?, ?, ?, ?, ?, ?, ?, ?)",
                bill.id,
                bill.patientId,
                bill.totalAmount,
                bill.status,
                sqlDate(bill.treatmentDate),
                sqlDate(bill.treatmentDate),
                bill.paidAmount,
                bill.remainingAmount
            );
        }

        long billItemId = SAMPLE_BILL_ITEM_BASE;
        for (int i = 0; i < bills.size(); i++) {
            BillRef bill = bills.get(i);
            long first = bill.totalAmount / 2;
            long second = bill.totalAmount / 3;
            long third = bill.totalAmount - first - second;
            long[] amounts = { first, second, third };

            for (int j = 0; j < 3; j++) {
                String itemName = sampleBillTypeNames.get((i + j) % sampleBillTypeNames.size());
                execute(
                    "insert into BILL_ITEM (BILL_ITEM_ID, BILL_ID, ITEM_NAME, ITEM_AMOUNT) values (?, ?, ?, ?)",
                    billItemId++,
                    bill.id,
                    itemName,
                    amounts[j]
                );
            }
        }

        long billHistoryId = SAMPLE_BILL_HISTORY_BASE;
        for (BillRef bill : bills) {
            execute(
                "insert into BILL_HISTORY (BILL_HISTORY_ID, BILL_ID, OLD_STATUS, NEW_STATUS, CHANGED_AT, CHANGED_BY, CHANGE_REASON) values (?, ?, ?, ?, ?, ?, ?)",
                billHistoryId++,
                bill.id,
                "DRAFT",
                "CONFIRMED",
                sqlDate(bill.treatmentDate),
                reception.username,
                "진료비 확정"
            );

            execute(
                "insert into BILL_HISTORY (BILL_HISTORY_ID, BILL_ID, OLD_STATUS, NEW_STATUS, CHANGED_AT, CHANGED_BY, CHANGE_REASON) values (?, ?, ?, ?, ?, ?, ?)",
                billHistoryId++,
                bill.id,
                "CONFIRMED",
                bill.status,
                sqlDate(bill.treatmentDate.plusDays(1)),
                reception.username,
                "수납 상태 반영"
            );
        }

        long paymentId = SAMPLE_PAYMENT_BASE;
        for (int i = 0; i < 30; i++) {
            BillRef bill = bills.get(i);
            execute(
                "insert into PAYMENT (PAYMENT_ID, BILL_ID, PAYMENT_AMOUNT, PAYMENT_METHOD, PAYMENT_STATUS, PAID_AT) values (?, ?, ?, ?, ?, ?)",
                paymentId++,
                bill.id,
                bill.totalAmount,
                paymentMethodCodes.get(i % 6),
                "COMPLETED",
                sqlDate(bill.treatmentDate)
            );
        }

        for (int i = 30; i < 60; i++) {
            BillRef bill = bills.get(i);
            execute(
                "insert into PAYMENT (PAYMENT_ID, BILL_ID, PAYMENT_AMOUNT, PAYMENT_METHOD, PAYMENT_STATUS, PAID_AT) values (?, ?, ?, ?, ?, ?)",
                paymentId++,
                bill.id,
                bill.paidAmount,
                paymentMethodCodes.get(i % 6),
                "COMPLETED",
                sqlDate(bill.treatmentDate)
            );
        }

        long paymentCancelId = SAMPLE_PAYMENT_CANCEL_BASE;
        for (int i = 0; i < 50; i++) {
            BillRef bill = bills.get(i % bills.size());
            long canceledPaymentId = paymentId++;
            long cancelAmount = 5000 + (i % 6) * 3000L;

            execute(
                "insert into PAYMENT (PAYMENT_ID, BILL_ID, PAYMENT_AMOUNT, PAYMENT_METHOD, PAYMENT_STATUS, PAID_AT) values (?, ?, ?, ?, ?, ?)",
                canceledPaymentId,
                bill.id,
                cancelAmount,
                paymentMethodCodes.get((i + 8) % paymentMethodCodes.size()),
                "CANCELED",
                sqlDate(bill.treatmentDate.plusDays(1))
            );

            execute(
                "insert into PAYMENT_CANCEL (PAYMENT_CANCEL_ID, PAYMENT_ID, CANCEL_AMOUNT, CANCEL_REASON, CANCEL_STATUS, CANCELED_AT) values (?, ?, ?, ?, ?, ?)",
                paymentCancelId++,
                canceledPaymentId,
                cancelAmount,
                "환자 요청 또는 중복 결제 취소",
                "COMPLETED",
                sqlDate(bill.treatmentDate.plusDays(1))
            );
        }

        for (int i = 30; i < bills.size(); i++) {
            BillRef bill = bills.get(i);
            execute(
                "insert into UNPAID (UNPAID_ID, BILL_ID, UNPAID_AMOUNT, STATUS, CREATED_AT) values (?, ?, ?, ?, ?)",
                SAMPLE_UNPAID_BASE + (i - 30),
                bill.id,
                bill.remainingAmount,
                bill.remainingAmount > 0 ? "OPEN" : "CLOSED",
                sqlDate(bill.treatmentDate.plusDays(2))
            );
        }

        long statId = SAMPLE_PAYMENT_METHOD_STAT_BASE;
        for (int day = 0; day < 10; day++) {
            for (int method = 0; method < 6; method++) {
                execute(
                    "insert into PAYMENT_METHOD_STAT (STAT_ID, STAT_DATE, PAYMENT_METHOD, TOTAL_AMOUNT, CREATED_AT) values (?, ?, ?, ?, ?)",
                    statId++,
                    sqlDate(LocalDate.of(2026, 3, 1).plusDays(day)),
                    paymentMethodCodes.get(method),
                    100000L + day * 5000L + method * 7000L,
                    sqlDate(LocalDate.of(2026, 3, 1).plusDays(day))
                );
            }
        }
    }

    private void printSummary() throws SQLException {
        String[] tables = {
            "APPT_BOOKING_RULE", "APPT_DOCTOR_SCHEDULE", "APPT_TIME_SLOT", "APPT_RESERVATION", "APPT_RESERVATION_STATUS_HIS",
            "APPT_TO_RCPT_CONVERSION_HIS", "RCPT_RECEPTION", "RCPT_RECEPTION_AUDIT", "RCPT_STATUS_HISTORY", "RCPT_OUTPATIENT_DETAIL",
            "RCPT_QUALIFICATION_SNAPSHOT", "RCPT_QUALIFICATION_ITEM", "RCPT_WAITING_QUEUE", "RCPT_CALL_HISTORY", "RCPT_CLOSURE_REASON",
            "RCPT_VISIT_CLOSURE", "RCPT_VISIT_CLOSURE_HIS", "RECEPTION_NO_SEQUENCE", "EMR_TRIAGE", "EMR_EMERGENCY_DETAIL",
            "CLINICAL_VISIT", "CLINICAL_VISIT_QUEUE", "CLINICAL_VISIT_STATUS_HISTORY", "NOTE", "NOTE_ATTACHMENT",
            "NOTE_HISTORY", "SOAP_SECTION", "CLINICAL_DIAGNOSIS", "CLINICAL_NOTE", "CLINICAL_NOTE_HISTORY",
            "CLINICAL_ORDER", "CLINICAL_ORDER_ITEM", "CLINICAL_ORDER_RESULT", "MEDICAL_ORDER", "ORDER_RESULT",
            "PROCEDURE_RESULT", "SUPPORT_TEST_EXECUTION", "ADM_INPATIENT_ADMISSION", "ADM_ADMISSION_DECISION", "ADM_BED_ASSIGNMENT",
            "ADM_BED_ASSIGNMENT_HIS", "ADM_ADMISSION_AUDIT", "VISIT", "VISIT_RESERVATION", "VISIT_EMERGENCY",
            "VISIT_INPATIENT", "ENCOUNTER_STATUS", "ENCOUNTER_SUMMARY", "MEDICAL_ENCOUNTER_DIAGNOSIS", "MEDICAL_ENCOUNTER_ASSET",
            "BILL_TYPE", "PAYMENT_METHOD", "BILL_HISTORY", "BILL_ITEM", "PAYMENT_CANCEL",
            "PAYMENT_METHOD_STAT", "UNPAID", "STAFF_CHANGE_REQUEST", "STAFF_COMMON_DOC_LINE", "STAFF_HISTORY"
        };

        for (String table : tables) {
            try (PreparedStatement ps = connection.prepareStatement("select count(*) from " + table);
                 ResultSet rs = ps.executeQuery()) {
                rs.next();
                System.out.println(table + "=" + rs.getLong(1));
            }
        }
    }

    private StaffRef selectStaff(int index) {
        int mod = index % 4;
        if (mod == 0) {
            return admin;
        }
        if (mod == 1) {
            return doctor;
        }
        if (mod == 2) {
            return nurse;
        }
        return reception;
    }

    private void deleteByRange(String table, String column, long startValue) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("delete from " + table + " where " + column + " >= ?")) {
            ps.setLong(1, startValue);
            ps.executeUpdate();
        }
    }

    private void deleteByPrefix(String table, String column, String likePattern) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("delete from " + table + " where " + column + " like ?")) {
            ps.setString(1, likePattern);
            ps.executeUpdate();
        }
    }

    private void execute(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                setParameter(ps, i + 1, params[i]);
            }
            ps.executeUpdate();
        }
    }

    private void setParameter(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) {
            ps.setObject(index, null);
        } else if (value instanceof String) {
            ps.setString(index, (String) value);
        } else if (value instanceof Long) {
            ps.setLong(index, (Long) value);
        } else if (value instanceof Integer) {
            ps.setInt(index, (Integer) value);
        } else if (value instanceof Double) {
            ps.setDouble(index, (Double) value);
        } else if (value instanceof Date) {
            ps.setDate(index, (Date) value);
        } else if (value instanceof Timestamp) {
            ps.setTimestamp(index, (Timestamp) value);
        } else {
            ps.setObject(index, value);
        }
    }

    private Date sqlDate(LocalDate date) {
        return Date.valueOf(date);
    }

    private Date sqlDate(LocalDateTime dateTime) {
        return Date.valueOf(dateTime.toLocalDate());
    }

    private Timestamp sqlTimestamp(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }

    private LocalDate daysAgo(int days) {
        return LocalDate.of(2026, 3, 16).minusDays(days);
    }

    private static class PatientRef {
        long id;
        String patientNo;
        String name;
    }

    private static class StaffRef {
        long id;
        String username;
        String fullName;
        String role;
        long deptId;
    }

    private static class DepartmentRef {
        long id;
        String name;
    }

    private static class EncounterRef {
        long id;
        long visitId;
        long patientId;
    }

    private static class DoctorScheduleRef {
        long id;
        DepartmentRef department;
        long doctorId;
        LocalDate date;
        int startHour;
        int endHour;
        int maxCapacity;
    }

    private static class AppointmentRef {
        long id;
        String apptNo;
        PatientRef patient;
        long departmentId;
        long doctorId;
        long scheduleId;
        long slotId;
        LocalDateTime dateTime;
        String status;
        String reason;
    }

    private static class ReceptionRef {
        long id;
        String receptionNo;
        PatientRef patient;
        long departmentId;
        long doctorId;
        Long sourceApptId;
        LocalDateTime dateTime;
        String receptionType;
        String status;
        String symptomSummary;
    }

    private static class ClinicalVisitRef {
        long id;
        long receptionId;
        PatientRef patient;
        long doctorId;
        LocalDateTime startTime;
        LocalDateTime endTime;
        String status;
    }

    private static class AdmissionRef {
        long id;
        ReceptionRef reception;
        PatientRef patient;
        LocalDateTime admissionDateTime;
    }

    private static class LegacyVisitRef {
        long id;
        PatientRef patient;
        long receptionId;
        long doctorId;
        LocalDateTime startTime;
        LocalDateTime endTime;
        String status;
        String groupType;
        String reservationId;
    }

    private static class BillRef {
        long id;
        long patientId;
        long totalAmount;
        long paidAmount;
        long remainingAmount;
        String status;
        LocalDate treatmentDate;
    }
}
