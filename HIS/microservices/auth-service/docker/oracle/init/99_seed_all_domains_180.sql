-- 180 sample rows across requested domains
-- notice/schedule/event: STAFF_BOARD_POST
-- docs/meetings: STAFF_COMMON_DOC + STAFF_COMMON_DOC_LINE
-- leave: LEAVE_REQUEST + LEAVE_APPROVAL_LINE
-- on-duty roster: SHIFT_ASSIGNMENT
-- training: TRAINING_SESSION + TRAINING_ENROLLMENT
-- handover note options: HANDOVER_OPTION

DECLARE
  v_req_id NUMBER;
  v_doc_id NUMBER;
  v_session_id NUMBER;
  v_shift_date VARCHAR2(10);
  v_staff_id VARCHAR2(100);
  v_staff_name VARCHAR2(100);
  v_dept VARCHAR2(100);
  v_type VARCHAR2(20);
BEGIN
  -- 1) 공지사항 20
  FOR i IN 1..20 LOOP
    INSERT INTO CMH.STAFF_BOARD_POST (
      ID, CATEGORY, POST_TYPE, TITLE, CONTENT, EVENT_DATE, LOCATION, SUBJECT_NAME,
      DEPARTMENT_NAME, AUTHOR_ID, AUTHOR_NAME, DELETE_PIN, IS_DELETED, CREATED_AT, UPDATED_AT
    ) VALUES (
      CMH.STAFF_BOARD_POST_SEQ.NEXTVAL,
      'NOTICE',
      CASE WHEN MOD(i,3)=0 THEN '필독' WHEN MOD(i,3)=1 THEN '공지' ELSE '일반' END,
      '[공지] 병원 운영 안내 ' || i,
      '공지사항 샘플 내용 ' || i,
      NULL,
      NULL,
      '공지 안내 ' || i,
      '행정부',
      'admin',
      '시스템 관리자',
      '1234',
      'N',
      SYSDATE,
      SYSDATE
    );
  END LOOP;

  -- 2) 주요일정 20
  FOR i IN 1..20 LOOP
    INSERT INTO CMH.STAFF_BOARD_POST (
      ID, CATEGORY, POST_TYPE, TITLE, CONTENT, EVENT_DATE, LOCATION, SUBJECT_NAME,
      DEPARTMENT_NAME, AUTHOR_ID, AUTHOR_NAME, DELETE_PIN, IS_DELETED, CREATED_AT, UPDATED_AT
    ) VALUES (
      CMH.STAFF_BOARD_POST_SEQ.NEXTVAL,
      'SCHEDULE',
      '공지',
      '[일정] 주요일정 안내 ' || i,
      '주요일정 샘플 내용 ' || i,
      TO_CHAR(TRUNC(SYSDATE) + i, 'YYYY-MM-DD'),
      '본관 회의실 ' || MOD(i, 5) + 1,
      '주요일정 ' || i,
      '운영팀',
      'admin',
      '시스템 관리자',
      '1234',
      'N',
      SYSDATE,
      SYSDATE
    );
  END LOOP;

  -- 3) 경조사 20
  FOR i IN 1..20 LOOP
    INSERT INTO CMH.STAFF_BOARD_POST (
      ID, CATEGORY, POST_TYPE, TITLE, CONTENT, EVENT_DATE, LOCATION, SUBJECT_NAME,
      DEPARTMENT_NAME, AUTHOR_ID, AUTHOR_NAME, DELETE_PIN, IS_DELETED, CREATED_AT, UPDATED_AT
    ) VALUES (
      CMH.STAFF_BOARD_POST_SEQ.NEXTVAL,
      'EVENT',
      CASE WHEN MOD(i,2)=0 THEN '일반' ELSE '공지' END,
      CASE WHEN MOD(i,2)=0 THEN '[경사] 직원 경사 안내 ' || i ELSE '[부고] 직원 조사 안내 ' || i END,
      CASE WHEN MOD(i,2)=0 THEN 'TYPE:경사\n\n행사 안내 ' || i ELSE 'TYPE:조사\n\n장례 안내 ' || i END,
      TO_CHAR(TRUNC(SYSDATE) + i, 'YYYY-MM-DD'),
      '외부 행사장 ' || MOD(i,4)+1,
      '경조사 ' || i,
      '총무팀',
      'admin',
      '시스템 관리자',
      '1234',
      'N',
      SYSDATE,
      SYSDATE
    );
  END LOOP;

  -- 4) 문서함 20 (카테고리 순환)
  FOR i IN 1..20 LOOP
    v_doc_id := CMH.STAFF_COMMON_DOC_SEQ.NEXTVAL;
    INSERT INTO CMH.STAFF_COMMON_DOC (
      ID, CATEGORY, TITLE, CONTENT, VERSION_LABEL, OWNER_NAME,
      SENDER_DEPT_ID, SENDER_DEPT_NAME, RECEIVER_DEPT_ID, RECEIVER_DEPT_NAME,
      APPROVER_ID, APPROVER_NAME, APPROVAL_STATUS,
      AUTHOR_ID, AUTHOR_NAME, IS_DELETED, CREATED_AT, UPDATED_AT
    ) VALUES (
      v_doc_id,
      CASE MOD(i,5)
        WHEN 1 THEN '규정'
        WHEN 2 THEN '매뉴얼'
        WHEN 3 THEN '양식'
        WHEN 4 THEN '교육자료'
        ELSE '공문'
      END,
      '문서함 샘플 ' || i,
      '문서함 샘플 내용 ' || i,
      'v1.' || i,
      '시스템 관리자',
      1,
      '행정부',
      2,
      '원무부',
      'doctor',
      '진료의 김담당',
      CASE WHEN MOD(i,3)=0 THEN 'APPROVED' ELSE 'PENDING' END,
      'admin',
      '시스템 관리자',
      'N',
      SYSDATE,
      SYSDATE
    );

    INSERT INTO CMH.STAFF_COMMON_DOC_LINE (
      ID, DOC_ID, LINE_ORDER, LINE_TYPE, APPROVER_ID, APPROVER_NAME, ACTION_STATUS, ACTION_COMMENT, ACTED_AT, CREATED_AT
    ) VALUES (
      CMH.STAFF_COMMON_DOC_LINE_SEQ.NEXTVAL,
      v_doc_id,
      1,
      'APPROVAL',
      'doctor',
      '진료의 김담당',
      CASE WHEN MOD(i,3)=0 THEN 'APPROVED' ELSE 'PENDING' END,
      NULL,
      CASE WHEN MOD(i,3)=0 THEN SYSDATE ELSE NULL END,
      SYSDATE
    );
  END LOOP;

  -- 5) 휴가근태 20
  FOR i IN 1..20 LOOP
    v_req_id := CMH.LEAVE_REQUEST_SEQ.NEXTVAL;
    v_staff_id := CASE MOD(i,4) WHEN 0 THEN 'admin' WHEN 1 THEN 'doctor' WHEN 2 THEN 'nurse' ELSE 'reception' END;
    v_staff_name := CASE MOD(i,4) WHEN 0 THEN '시스템 관리자' WHEN 1 THEN '진료의 김담당' WHEN 2 THEN '간호사 이담당' ELSE '원무 박담당' END;
    v_dept := CASE MOD(i,4) WHEN 0 THEN '행정부' WHEN 1 THEN '진료부' WHEN 2 THEN '간호부' ELSE '원무부' END;

    INSERT INTO CMH.LEAVE_REQUEST (
      ID, REQUESTER_ID, REQUESTER_NAME, DEPARTMENT_NAME, LEAVE_TYPE, FROM_DATE, TO_DATE, REASON, FINAL_STATUS, CREATED_AT, UPDATED_AT
    ) VALUES (
      v_req_id,
      v_staff_id,
      v_staff_name,
      v_dept,
      CASE MOD(i,3) WHEN 0 THEN '연차' WHEN 1 THEN '반차' ELSE '병가' END,
      TO_CHAR(TRUNC(SYSDATE) + i, 'YYYY-MM-DD'),
      TO_CHAR(TRUNC(SYSDATE) + i + MOD(i,2), 'YYYY-MM-DD'),
      '휴가 사유 샘플 ' || i,
      CASE WHEN MOD(i,4)=0 THEN 'APPROVED' WHEN MOD(i,4)=1 THEN 'REJECTED' ELSE 'PENDING' END,
      SYSDATE,
      SYSDATE
    );

    INSERT INTO CMH.LEAVE_APPROVAL_LINE (
      ID, REQUEST_ID, LINE_TYPE, APPROVER_ID, APPROVER_NAME, LINE_ORDER, ACTION_STATUS, ACTED_AT, CREATED_AT, UPDATED_AT
    ) VALUES (
      CMH.LEAVE_APPROVAL_LINE_SEQ.NEXTVAL,
      v_req_id,
      'APPROVAL',
      'admin',
      '시스템 관리자',
      1,
      CASE WHEN MOD(i,4)=0 THEN 'APPROVED' WHEN MOD(i,4)=1 THEN 'REJECTED' ELSE 'PENDING' END,
      CASE WHEN MOD(i,4) IN (0,1) THEN TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI') ELSE NULL END,
      SYSDATE,
      SYSDATE
    );

    INSERT INTO CMH.LEAVE_APPROVAL_LINE (
      ID, REQUEST_ID, LINE_TYPE, APPROVER_ID, APPROVER_NAME, LINE_ORDER, ACTION_STATUS, ACTED_AT, CREATED_AT, UPDATED_AT
    ) VALUES (
      CMH.LEAVE_APPROVAL_LINE_SEQ.NEXTVAL,
      v_req_id,
      'CC',
      'nurse',
      '간호사 이담당',
      2,
      'PENDING',
      NULL,
      SYSDATE,
      SYSDATE
    );
  END LOOP;

  -- 6) 당직교대표(근무표) 20
  FOR i IN 1..20 LOOP
    v_shift_date := TO_CHAR(TRUNC(SYSDATE) + i, 'YYYY-MM-DD');
    v_staff_id := CASE MOD(i,4) WHEN 0 THEN 'admin' WHEN 1 THEN 'doctor' WHEN 2 THEN 'nurse' ELSE 'reception' END;
    v_staff_name := CASE MOD(i,4) WHEN 0 THEN '시스템 관리자' WHEN 1 THEN '진료의 김담당' WHEN 2 THEN '간호사 이담당' ELSE '원무 박담당' END;
    v_dept := CASE MOD(i,4) WHEN 0 THEN '행정부' WHEN 1 THEN '진료부' WHEN 2 THEN '간호부' ELSE '원무부' END;
    v_type := CASE WHEN MOD(i,2)=0 THEN 'DAY' ELSE 'NIGHT' END;

    BEGIN
      INSERT INTO CMH.SHIFT_ASSIGNMENT (
        ID, SHIFT_DATE, STAFF_ID, STAFF_NAME, DEPARTMENT_NAME, SHIFT_TYPE, CREATED_BY, CREATED_AT, UPDATED_AT
      ) VALUES (
        CMH.SHIFT_ASSIGNMENT_SEQ.NEXTVAL,
        v_shift_date,
        v_staff_id,
        v_staff_name,
        v_dept,
        v_type,
        'admin',
        SYSDATE,
        SYSDATE
      );
    EXCEPTION
      WHEN DUP_VAL_ON_INDEX THEN NULL;
    END;
  END LOOP;

  -- 7) 교육이수 20 세션 + 등록 데이터
  FOR i IN 1..20 LOOP
    v_session_id := CMH.TRAINING_SESSION_SEQ.NEXTVAL;

    INSERT INTO CMH.TRAINING_SESSION (
      ID, TRAINING_DATE, TITLE, CATEGORY, DEPARTMENT_NAME, CAPACITY, IS_ACTIVE, CREATED_AT, UPDATED_AT
    ) VALUES (
      v_session_id,
      TRUNC(SYSDATE) + i,
      '교육이수 샘플 과정 ' || i,
      CASE WHEN MOD(i,2)=0 THEN '필수' ELSE '선택' END,
      CASE MOD(i,3) WHEN 0 THEN '전체' WHEN 1 THEN '간호부' ELSE '원무/행정' END,
      20 + MOD(i, 20),
      1,
      SYSTIMESTAMP,
      SYSTIMESTAMP
    );

    INSERT INTO CMH.TRAINING_ENROLLMENT (
      ID, SESSION_ID, STAFF_ID, STATUS, CREATED_AT, UPDATED_AT
    ) VALUES (
      CMH.TRAINING_ENROLLMENT_SEQ.NEXTVAL,
      v_session_id,
      'admin',
      CASE WHEN MOD(i,3)=0 THEN 'COMPLETED' ELSE 'APPLIED' END,
      SYSTIMESTAMP,
      SYSTIMESTAMP
    );
  END LOOP;

  -- 8) 인계노트 옵션 20(완료), 20(todo)
  FOR i IN 1..20 LOOP
    MERGE INTO CMH.HANDOVER_OPTION t
    USING (SELECT 'COMPLETED' AS option_type, '완료 항목 ' || i AS label, i AS sort_order FROM dual) s
    ON (t.OPTION_TYPE = s.option_type AND t.LABEL = s.label)
    WHEN MATCHED THEN UPDATE SET t.SORT_ORDER = s.sort_order, t.IS_ACTIVE = 1
    WHEN NOT MATCHED THEN
      INSERT (ID, OPTION_TYPE, LABEL, SORT_ORDER, IS_ACTIVE)
      VALUES (CMH.HANDOVER_OPTION_SEQ.NEXTVAL, s.option_type, s.label, s.sort_order, 1);

    MERGE INTO CMH.HANDOVER_OPTION t
    USING (SELECT 'TODO' AS option_type, '할일 항목 ' || i AS label, i AS sort_order FROM dual) s
    ON (t.OPTION_TYPE = s.option_type AND t.LABEL = s.label)
    WHEN MATCHED THEN UPDATE SET t.SORT_ORDER = s.sort_order, t.IS_ACTIVE = 1
    WHEN NOT MATCHED THEN
      INSERT (ID, OPTION_TYPE, LABEL, SORT_ORDER, IS_ACTIVE)
      VALUES (CMH.HANDOVER_OPTION_SEQ.NEXTVAL, s.option_type, s.label, s.sort_order, 1);
  END LOOP;

  -- 9) 회의위원회 20 (문서함의 회의록 카테고리)
  FOR i IN 1..20 LOOP
    v_doc_id := CMH.STAFF_COMMON_DOC_SEQ.NEXTVAL;
    INSERT INTO CMH.STAFF_COMMON_DOC (
      ID, CATEGORY, TITLE, CONTENT, VERSION_LABEL, OWNER_NAME,
      SENDER_DEPT_ID, SENDER_DEPT_NAME, RECEIVER_DEPT_ID, RECEIVER_DEPT_NAME,
      APPROVER_ID, APPROVER_NAME, APPROVAL_STATUS,
      AUTHOR_ID, AUTHOR_NAME, IS_DELETED, CREATED_AT, UPDATED_AT
    ) VALUES (
      v_doc_id,
      '회의록',
      i || '차 회의록 - 회의위원회 샘플 ' || i,
      '회의위원회 샘플 내용 ' || i,
      'v1.' || i,
      '시스템 관리자',
      1,
      '행정부',
      3,
      '간호부',
      'admin',
      '시스템 관리자',
      CASE WHEN MOD(i,2)=0 THEN 'APPROVED' ELSE 'PENDING' END,
      'admin',
      '시스템 관리자',
      'N',
      SYSDATE,
      SYSDATE
    );

    INSERT INTO CMH.STAFF_COMMON_DOC_LINE (
      ID, DOC_ID, LINE_ORDER, LINE_TYPE, APPROVER_ID, APPROVER_NAME, ACTION_STATUS, ACTION_COMMENT, ACTED_AT, CREATED_AT
    ) VALUES (
      CMH.STAFF_COMMON_DOC_LINE_SEQ.NEXTVAL,
      v_doc_id,
      1,
      'APPROVAL',
      'admin',
      '시스템 관리자',
      CASE WHEN MOD(i,2)=0 THEN 'APPROVED' ELSE 'PENDING' END,
      NULL,
      CASE WHEN MOD(i,2)=0 THEN SYSDATE ELSE NULL END,
      SYSDATE
    );
  END LOOP;

  COMMIT;
END;
/
