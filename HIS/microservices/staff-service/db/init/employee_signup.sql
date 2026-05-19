/*------------------------------------------------------------------------------
-- EMPLOYEE : 직원 공통 허브
------------------------------------------------------------------------------*/
CREATE TABLE JCH.EMPLOYEE (
                              STAFF_ID          VARCHAR2(30 BYTE)    NOT NULL,
                              DEPT_ID           VARCHAR2(30 BYTE)    NOT NULL,
                              NAME         VARCHAR2(100 BYTE)   NOT NULL,
                              PHONE             VARCHAR2(50 BYTE)    NULL,
                              EMAIL             VARCHAR2(100 BYTE)   NULL,
                              BIRTH_DATE        VARCHAR2(6 BYTE)     NULL,
                              GENDER_CODE       VARCHAR2(1 BYTE)     NULL,
                              ZIP_CODE          VARCHAR2(20 BYTE)    NULL,
                              ADDRESS1          VARCHAR2(255 BYTE)   NULL,
                              ADDRESS2          VARCHAR2(255 BYTE)   NULL,
                              STATUS            VARCHAR2(20 BYTE)    DEFAULT 'ACTIVE' NOT NULL,
                              CREATED_AT        DATE                 DEFAULT SYSDATE NOT NULL,
                              UPDATED_AT        DATE                 DEFAULT SYSDATE NOT NULL,

                              CONSTRAINT PK_EMPLOYEE PRIMARY KEY (STAFF_ID),

                              CONSTRAINT CK_EMPLOYEE_BIRTH_DATE
                                  CHECK (
                                      BIRTH_DATE IS NULL
                                          OR REGEXP_LIKE(BIRTH_DATE, '^[0-9]{6}$')
                                      ),

                              CONSTRAINT CK_EMPLOYEE_GENDER_CODE
                                  CHECK (
                                      GENDER_CODE IS NULL
                                          OR GENDER_CODE IN ('1', '2', '3', '4')
                                      ),

                              CONSTRAINT CK_EMPLOYEE_STATUS
                                  CHECK (STATUS IN ('ACTIVE', 'INACTIVE', 'LEAVE', 'RETIRE')),

                              CONSTRAINT FK_EMPLOYEE_DEPARTMENT
                                  FOREIGN KEY (DEPT_ID)
                                      REFERENCES JCH.DEPARTMENT (DEPT_ID)
);

/*------------------------------------------------------------------------------
-- EMPLOYEE_PRIVATE : 직원 민감정보
-- 주민번호 전체가 정말 필요한 경우만 관리
------------------------------------------------------------------------------*/
CREATE TABLE JCH.EMPLOYEE_PRIVATE (
                                      STAFF_ID          VARCHAR2(30 BYTE)    NOT NULL,
                                      RRN_FRONT         VARCHAR2(6 BYTE)     NULL,
                                      RRN_BACK          VARCHAR2(7 BYTE)     NULL,


                                      CONSTRAINT PK_EMPLOYEE_PRIVATE PRIMARY KEY (STAFF_ID),

                                      CONSTRAINT CK_EMP_PRIVATE_RRN_FRONT
                                          CHECK (
                                              RRN_FRONT IS NULL
                                                  OR REGEXP_LIKE(RRN_FRONT, '^[0-9]{6}$')
                                              ),

                                      CONSTRAINT CK_EMP_PRIVATE_RRN_BACK
                                          CHECK (
                                              RRN_BACK IS NULL
                                                  OR REGEXP_LIKE(RRN_BACK, '^[0-9]{7}$')
                                              ),

                                      CONSTRAINT FK_EMP_PRIVATE_EMPLOYEE
                                          FOREIGN KEY (STAFF_ID)
                                              REFERENCES JCH.EMPLOYEE (STAFF_ID)
);