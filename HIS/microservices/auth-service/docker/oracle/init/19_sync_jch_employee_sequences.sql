-- Ensure JCH employee sequences are ahead of clean STAFF_ID patterns.
DECLARE
  FUNCTION seq_exists(p_owner IN VARCHAR2, p_seq IN VARCHAR2) RETURN NUMBER IS
    v_cnt NUMBER;
  BEGIN
    SELECT COUNT(*)
      INTO v_cnt
      FROM ALL_SEQUENCES
     WHERE SEQUENCE_OWNER = UPPER(p_owner)
       AND SEQUENCE_NAME = UPPER(p_seq);
    RETURN v_cnt;
  END;

  PROCEDURE sync_sequence(p_owner IN VARCHAR2, p_sequence IN VARCHAR2, p_target IN NUMBER) IS
    v_next NUMBER;
    v_inc  NUMBER;
  BEGIN
    EXECUTE IMMEDIATE 'SELECT ' || p_owner || '.' || p_sequence || '.NEXTVAL FROM DUAL' INTO v_next;

    IF v_next < p_target THEN
      v_inc := p_target - v_next;
      EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || p_owner || '.' || p_sequence || ' INCREMENT BY ' || v_inc;
      EXECUTE IMMEDIATE 'SELECT ' || p_owner || '.' || p_sequence || '.NEXTVAL FROM DUAL' INTO v_next;
      EXECUTE IMMEDIATE 'ALTER SEQUENCE ' || p_owner || '.' || p_sequence || ' INCREMENT BY 1';
    END IF;
  END;

  v_doctor_target NUMBER := 1;
  v_nurse_target  NUMBER := 1;
BEGIN
  SELECT NVL(MAX(TO_NUMBER(REGEXP_SUBSTR(STAFF_ID, '[0-9]{3}$'))), 0) + 1
    INTO v_doctor_target
    FROM (
      SELECT STAFF_ID FROM JCH.EMPLOYEE WHERE REGEXP_LIKE(STAFF_ID, '^DOC_[0-9]{3}$')
      UNION ALL
      SELECT STAFF_ID FROM JCH.EMPLOYEE_DOCTOR WHERE REGEXP_LIKE(STAFF_ID, '^DOC_[0-9]{3}$')
    );

  SELECT NVL(MAX(TO_NUMBER(REGEXP_SUBSTR(STAFF_ID, '[0-9]{3}$'))), 0) + 1
    INTO v_nurse_target
    FROM (
      SELECT STAFF_ID FROM JCH.EMPLOYEE WHERE REGEXP_LIKE(STAFF_ID, '^NUR_[0-9]{3}$')
      UNION ALL
      SELECT STAFF_ID FROM JCH.EMPLOYEE WHERE REGEXP_LIKE(STAFF_ID, '^NURSE_[0-9]{3}$')
      UNION ALL
      SELECT STAFF_ID FROM JCH.EMPLOYEE_NURSE WHERE REGEXP_LIKE(STAFF_ID, '^NUR_[0-9]{3}$')
      UNION ALL
      SELECT STAFF_ID FROM JCH.EMPLOYEE_NURSE WHERE REGEXP_LIKE(STAFF_ID, '^NURSE_[0-9]{3}$')
    );

  IF seq_exists('JCH', 'EMPLOYEE_DOCTOR_ID') = 1 THEN
    sync_sequence('JCH', 'EMPLOYEE_DOCTOR_ID', v_doctor_target);
  END IF;

  IF seq_exists('JCH', 'EMPLOYEE_NURSE_ID') = 1 THEN
    sync_sequence('JCH', 'EMPLOYEE_NURSE_ID', v_nurse_target);
  END IF;
END;
/
