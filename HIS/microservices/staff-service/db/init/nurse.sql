DROP TABLE JCH.EMPLOYEE_NURSE CASCADE CONSTRAINTS;

CREATE TABLE JCH.EMPLOYEE_NURSE (
                                    STAFF_ID            VARCHAR2(30 BYTE)     NOT NULL,
                                    LICENSE_NO          VARCHAR2(50 BYTE)     NOT NULL,
                                    NURSE_TYPE          VARCHAR2(30 BYTE)     NOT NULL,
                                    SHIFT_TYPE          VARCHAR2(30 BYTE)     NULL,

                                    NURSE_FILE_URL      VARCHAR2(1000 BYTE)   NULL,
                                    EDUCATION           VARCHAR2(1000 BYTE)   NULL,
                                    CAREER_DETAIL       VARCHAR2(1000 BYTE)   NULL,
                                    CREATED_AT        DATE                 DEFAULT SYSDATE NOT NULL,
                                    UPDATED_AT        DATE                 DEFAULT SYSDATE NOT NULL,


                                    CONSTRAINT PK_EMPLOYEE_NURSE PRIMARY KEY (STAFF_ID),

                                    CONSTRAINT UK_EMPLOYEE_NURSE_LICENSE UNIQUE (LICENSE_NO),

                                    CONSTRAINT FK_EMPLOYEE_NURSE_STAFF
                                        FOREIGN KEY (STAFF_ID)
                                            REFERENCES JCH.EMPLOYEE (STAFF_ID)

                                                 ON DELETE CASCADE
);