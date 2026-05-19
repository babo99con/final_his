DROP TABLE JCH.EMPLOYEE_DOCTOR CASCADE CONSTRAINTS;

CREATE TABLE JCH.EMPLOYEE_DOCTOR (
                                     STAFF_ID          VARCHAR2(30 BYTE)     NOT NULL,
                                     LICENSE_NO        VARCHAR2(50 BYTE)     NOT NULL,
                                     DOCTOR_TYPE       VARCHAR2(30 BYTE)     NOT NULL,
                                     SPECIALTY_ID      VARCHAR2(30 BYTE)     NULL,
                                     PROFILE_SUMMARY   VARCHAR2(200 BYTE)    NULL,
                                     DOCTOR_FILE_URL   VARCHAR2(1000 BYTE)   NULL,
                                     EDUCATION         VARCHAR2(1000 BYTE)   NULL,
                                     CAREER_DETAIL     VARCHAR2(1000 BYTE)   NULL,
                                     CREATED_AT        DATE                 DEFAULT SYSDATE NOT NULL,
                                     UPDATED_AT        DATE                 DEFAULT SYSDATE NOT NULL,

                                     CONSTRAINT PK_EMPLOYEE_DOCTOR PRIMARY KEY (STAFF_ID),

                                     CONSTRAINT UK_EMPLOYEE_DOCTOR_LICENSE UNIQUE (LICENSE_NO),

                                     CONSTRAINT FK_EMPLOYEE_DOCTOR_STAFF
                                         FOREIGN KEY (STAFF_ID)
                                             REFERENCES JCH.EMPLOYEE (STAFF_ID)

                                         ON DELETE CASCADE
    -- specialty 테이블 있으면 여기에 추가
    -- CONSTRAINT FK_EMPLOYEE_DOCTOR_SPECIALTY
    --     FOREIGN KEY (SPECIALTY_ID)
    --     REFERENCES JCH.SPECIALTY (SPECIALTY_ID)


);