CREATE TABLE JCH.DEPARTMENT_INFO (
                                        DEPT_ID         VARCHAR2(30 CHAR)   NOT NULL,
                                        BUILDING_NAME   VARCHAR2(100 CHAR),
                                        FLOOR_NO        VARCHAR2(20 CHAR),
                                        ROOM_NO         VARCHAR2(20 CHAR),
                                        DAY_PHONE       VARCHAR2(30 CHAR),
                                        NIGHT_PHONE     VARCHAR2(30 CHAR),
                                        MAIN_PHONE      VARCHAR2(30 CHAR),
                                        LOCATION_DESC   VARCHAR2(300 CHAR),

                                        CONSTRAINT PK_DEPARTMENT_INFO PRIMARY KEY (DEPT_ID),
                                        CONSTRAINT FK_DEPARTMENT_INFO
                                            FOREIGN KEY (DEPT_ID) REFERENCES JCH.DEPARTMENT(DEPT_ID)
);