CREATE OR REPLACE PROCEDURE HOSPITAL.SP_NEXT_NO (
  P_SEQ_TYPE IN  VARCHAR2,
  P_NO       OUT VARCHAR2
) AS
  V_SEQ_DATE  DATE := TRUNC(SYSDATE);
  V_LAST_NO   NUMBER;
  V_PREFIX    VARCHAR2(10);
BEGIN
  -- prefix 결정
  V_PREFIX :=
    CASE UPPER(P_SEQ_TYPE)
      WHEN 'PATIENT_NO'                    THEN 'P'
      WHEN 'VISIT_NO'                      THEN 'V'
      WHEN 'CLINIC_NO'                     THEN 'C'
      WHEN 'RECEIPT_NO'                    THEN 'R'
      WHEN 'BILLING_NO'                    THEN 'B'

      WHEN 'RECORD_ID'                     THEN 'REC'
      WHEN 'MEDICATION_RECORD_ID'          THEN 'MED'
      WHEN 'TREATMENT_RESULT_ID'           THEN 'TRT'
      WHEN 'TEST_EXECUTION_ID'             THEN 'TEX'

      WHEN 'IMAGING_EXAM_ID'               THEN 'IMG'
      WHEN 'IMAGING_RESULT_ID'             THEN 'IMG_R'

      WHEN 'ENDOSCOPY_EXAM_ID'             THEN 'END'
      WHEN 'ENDOSCOPY_RESULT_ID'           THEN 'END_R'

      WHEN 'PATHOLOGY_EXAM_ID'             THEN 'PTH'
      WHEN 'PATHOLOGY_EXAM_RESULT_ID'      THEN 'PTH_R'

      WHEN 'PHYSIOLOGICAL_EXAM_ID'         THEN 'PHY'
      WHEN 'PHYSIOLOGICAL_EXAM_RESULT_ID'  THEN 'PHY_R'

      WHEN 'SPECIMEN_EXAM_ID'              THEN 'SPC'
      WHEN 'SPECIMEN_EXAM_RESULT_ID'       THEN 'SPC_R'

      ELSE 'X'
    END;

  LOOP
    BEGIN
      SELECT LAST_NO
        INTO V_LAST_NO
        FROM NO_SEQUENCE
       WHERE SEQ_DATE = V_SEQ_DATE
         AND SEQ_TYPE = P_SEQ_TYPE
       FOR UPDATE;

      V_LAST_NO := V_LAST_NO + 1;

      UPDATE NO_SEQUENCE
         SET LAST_NO = V_LAST_NO
       WHERE SEQ_DATE = V_SEQ_DATE
         AND SEQ_TYPE = P_SEQ_TYPE;

      EXIT;

    EXCEPTION
      WHEN NO_DATA_FOUND THEN
        BEGIN
          INSERT INTO NO_SEQUENCE (SEQ_DATE, SEQ_TYPE, LAST_NO)
          VALUES (V_SEQ_DATE, P_SEQ_TYPE, 1);
          V_LAST_NO := 1;
          EXIT;
        EXCEPTION
          WHEN DUP_VAL_ON_INDEX THEN
            NULL;
        END;
    END;
  END LOOP;

  P_NO := V_PREFIX || TO_CHAR(V_SEQ_DATE, 'YYYYMMDD') || '-' || LPAD(V_LAST_NO, 6, '0');
END;
/
