package com.example.hospitalClinical.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Profile("db-seed")
@Component
public class DbSeedRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public DbSeedRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM HOSPITAL.CLINICAL_VISIT WHERE VISIT_ID = 1",
                Integer.class);
        if (count != null && count > 0) {
            System.out.println("[DbSeed] VISIT_ID=1 ?대? 議댁옱. ?섑뵆 ?쎌엯 ?ㅽ궢.");
            return;
        }
        System.out.println("[DbSeed] VISIT_ID=1 ?놁쓬. ?섑뵆 ?곗씠???쎌엯 ?쒖옉.");

        jdbcTemplate.update(
                "INSERT INTO HOSPITAL.CLINICAL_VISIT (VISIT_ID, PATIENT_ID, DOCTOR_ID, RECEPTION_ID, VISIT_STATUS, START_TIME, CREATED_AT, UPDATED_AT) " +
                        "VALUES (1, 1, 1, 1, 'WAITING', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP)");
        jdbcTemplate.update(
                "INSERT INTO HOSPITAL.CLINICAL_VISIT (VISIT_ID, PATIENT_ID, DOCTOR_ID, RECEPTION_ID, VISIT_STATUS, START_TIME, CREATED_AT, UPDATED_AT) " +
                        "VALUES (2, 2, 1, 1, 'IN_PROGRESS', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP)");
        jdbcTemplate.update(
                "INSERT INTO HOSPITAL.CLINICAL_VISIT (VISIT_ID, PATIENT_ID, DOCTOR_ID, RECEPTION_ID, VISIT_STATUS, START_TIME, END_TIME, CREATED_AT, UPDATED_AT) " +
                        "VALUES (3, 1, 1, 1, 'COMPLETED', SYSTIMESTAMP - 1, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP)");

        jdbcTemplate.update(
                "INSERT INTO HOSPITAL.NOTE (NOTE_ID, VISIT_ID, CHIEF_COMPLAINT, STATUS, CREATED_AT, UPDATED_AT) " +
                        "SELECT CL_NOTE_SEQ.NEXTVAL, 1, '?먰넻', 'DRAFT', SYSTIMESTAMP, SYSTIMESTAMP FROM DUAL " +
                        "WHERE NOT EXISTS (SELECT 1 FROM HOSPITAL.NOTE WHERE VISIT_ID = 1)");

        jdbcTemplate.update(
                "INSERT INTO HOSPITAL.CLINICAL_ORDER (ORDER_ID, VISIT_ID, ORDER_TYPE, ORDER_STATUS, DOCTOR_ID, ORDER_DATE, CREATED_AT, UPDATED_AT) " +
                        "SELECT CL_ORDER_SEQ.NEXTVAL, 1, 'LAB', 'REQUESTED', 1, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP FROM DUAL");

        System.out.println("[DbSeed] ?섑뵆 ?곗씠???쎌엯 ?꾨즺. VISIT 1,2,3 / NOTE(visitId=1) / ORDER(visitId=1)");
        System.exit(0);
    }
}
