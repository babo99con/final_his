/*------------------------------------------------------------------------------
-- DROP
------------------------------------------------------------------------------*/
DROP TABLE JCH.EMPLOYEE_PRIVATE CASCADE CONSTRAINTS;
DROP TABLE JCH.EMPLOYEE CASCADE CONSTRAINTS;
DROP TABLE JCH.DEPARTMENT CASCADE CONSTRAINTS;

/*------------------------------------------------------------------------------
-- DEPARTMENT : 부서 마스터
------------------------------------------------------------------------------*/
CREATE TABLE JCH.DEPARTMENT (
                                DEPT_ID         VARCHAR2(30 CHAR)   NOT NULL,
                                DEPT_CODE       VARCHAR2(30 CHAR)   NOT NULL,
                                DEPT_NAME       VARCHAR2(100 CHAR)  NOT NULL,
                                PARENT_DEPT_ID  VARCHAR2(30 CHAR),
                                DEPT_TYPE_CD    VARCHAR2(30 CHAR),
                                STATUS          VARCHAR2(20 CHAR)   DEFAULT 'ACTIVE' NOT NULL,
                                CREATED_AT      DATE                DEFAULT SYSDATE NOT NULL,
                                UPDATED_AT      DATE                DEFAULT SYSDATE NOT NULL,

                                CONSTRAINT PK_DEPARTMENT PRIMARY KEY (DEPT_ID),
                                CONSTRAINT UK_DEPARTMENT_CODE UNIQUE (DEPT_CODE),
                                CONSTRAINT FK_DEPARTMENT_PARENT
                                    FOREIGN KEY (PARENT_DEPT_ID) REFERENCES JCH.DEPARTMENT(DEPT_ID),
                                CONSTRAINT CK_DEPARTMENT_STATUS
                                    CHECK (STATUS IN ('ACTIVE', 'INACTIVE'))
);