CREATE TABLE JCH.EMPLOYEE_RECEPTION (
                                        STAFF_ID             VARCHAR2(30 CHAR)    NOT NULL,
                                        JOB_TYPE_CD          VARCHAR2(30 CHAR),
                                        DESK_NO              VARCHAR2(20 CHAR),
                                        SHIFT_TYPE           VARCHAR2(20 CHAR),
                                        START_DATE           DATE,
                                        WINDOW_AREA          VARCHAR2(50 CHAR),
                                        MULTI_TASK           VARCHAR2(500 CHAR),
                                        RMK                  VARCHAR2(1000 CHAR),

                                        CONSTRAINT PK_EMPLOYEE_RECEPTION PRIMARY KEY (STAFF_ID),

                                        CONSTRAINT FK_EMP_RECEPTION_STAFF
                                            FOREIGN KEY (STAFF_ID)
                                                REFERENCES JCH.EMPLOYEE (STAFF_ID),

                                        CONSTRAINT CK_EMP_RECEPTION_SHIFT
                                            CHECK (SHIFT_TYPE IN ('DAY', 'NIGHT', 'ROTATION'))
);