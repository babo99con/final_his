/*------------------------------------------------------------------------------
-- PATIENT_FAMILY: 환자 가족관계 테이블
------------------------------------------------------------------------------*/

CREATE SEQUENCE PATIENT_FAMILY_SEQ
  START WITH 1
  INCREMENT BY 1
  NOCACHE;

CREATE TABLE PATIENT_FAMILY (
  FAMILY_ID         NUMBER                NOT NULL,
  PATIENT_ID        NUMBER                NOT NULL,
  RELATION          VARCHAR2(30 BYTE)     NOT NULL,
  FAMILY_NAME       VARCHAR2(50 BYTE)     NOT NULL,
  FAMILY_PHONE      VARCHAR2(20 BYTE)         NULL,
  BIRTH_DATE        DATE                       NULL,
  IS_PRIMARY        NUMBER(1)             DEFAULT 0              NOT NULL,
  SORT_ORDER        NUMBER                DEFAULT 1              NOT NULL,
  CREATED_AT        DATE                  DEFAULT SYSDATE        NOT NULL,
  UPDATED_AT        DATE                  DEFAULT SYSDATE        NOT NULL,
  CONSTRAINT PK_PATIENT_FAMILY PRIMARY KEY (FAMILY_ID),
  CONSTRAINT FK_PATIENT_FAMILY_PATIENT FOREIGN KEY (PATIENT_ID)
    REFERENCES PATIENT (PATIENT_ID) ON DELETE CASCADE
);

CREATE INDEX IDX_PATIENT_FAMILY_PATIENT ON PATIENT_FAMILY (PATIENT_ID);
