/*------------------------------------------------------------------------------
-- 동의서 철회 이력 - ORA-02289 해결
-- Hibernate가 CONSENT_WITHDRAW_HISTORY_SEQ 시퀀스를 사용합니다.
------------------------------------------------------------------------------*/

CREATE SEQUENCE CONSENT_WITHDRAW_HISTORY_SEQ
  START WITH 1
  INCREMENT BY 1
  NOCACHE;

/*
CREATE TABLE PATIENT_CONSENT_HISTORY (
  HISTORY_ID    NUMBER          NOT NULL,
  CONSENT_ID    NUMBER          NOT NULL,
  PATIENT_ID    NUMBER          NOT NULL,
  CONSENT_TYPE  VARCHAR2(30)    NOT NULL,
  WITHDRAWN_AT  TIMESTAMP       NOT NULL,
  CHANGED_BY    VARCHAR2(50)    NULL,
  CREATED_AT    TIMESTAMP       DEFAULT SYSTIMESTAMP NULL,
  CONSTRAINT PK_PATIENT_CONSENT_HISTORY PRIMARY KEY (HISTORY_ID)
);
*/
