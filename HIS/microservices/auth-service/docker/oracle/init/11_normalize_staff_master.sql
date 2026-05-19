MERGE INTO CMH.STAFF_STATUS_CODES t
USING (
    SELECT 'ACTIVE' AS code, '재직' AS label, 1 AS sort_order, 'Y' AS is_active FROM dual
    UNION ALL SELECT 'INACTIVE', '비활성', 2, 'Y' FROM dual
    UNION ALL SELECT 'ON_LEAVE', '휴직', 3, 'Y' FROM dual
    UNION ALL SELECT 'RESIGNED', '퇴사', 4, 'Y' FROM dual
    UNION ALL SELECT 'SUSPENDED', '정지', 5, 'Y' FROM dual
    UNION ALL SELECT 'PENDING_APPROVAL', '승인대기', 90, 'N' FROM dual
    UNION ALL SELECT 'REJECTED_SIGNUP', '가입반려', 91, 'N' FROM dual
) s
ON (t.CODE = s.code)
WHEN MATCHED THEN
  UPDATE SET
    t.LABEL = s.label,
    t.SORT_ORDER = s.sort_order,
    t.IS_ACTIVE = s.is_active,
    t.UPDATED_AT = SYSDATE
WHEN NOT MATCHED THEN
  INSERT (CODE, LABEL, SORT_ORDER, IS_ACTIVE, CREATED_AT, UPDATED_AT)
  VALUES (s.code, s.label, s.sort_order, s.is_active, SYSDATE, SYSDATE);

MERGE INTO CMH.DEPARTMENTS t
USING (
    SELECT 1 AS id, '원무팀' AS name, '환자 접수 및 수납' AS description, '본관 1층' AS location, 'RECEPTION' AS dept_code, 'Y' AS is_active, 1 AS sort_order, '1' AS floor_no, '101' AS room_no, '7001' AS extension FROM dual
    UNION ALL SELECT 2, '진료부', '외래 진료 공통 부서', '본관 2층', 'MEDICAL', 'Y', 2, '2', '201', '7100' FROM dual
    UNION ALL SELECT 3, '간호부', '병동 및 진료지원 간호', '본관 3층', 'NURSING', 'Y', 3, '3', '301', '7200' FROM dual
    UNION ALL SELECT 11, '내과', '내과 외래 진료', '본관 2층', 'INTERNAL_MEDICINE', 'Y', 11, '2', '211', '7111' FROM dual
    UNION ALL SELECT 12, '외과', '외과 외래 진료', '본관 2층', 'SURGERY', 'Y', 12, '2', '212', '7112' FROM dual
    UNION ALL SELECT 13, '정형외과', '정형외과 외래 진료', '본관 2층', 'ORTHOPEDICS', 'Y', 13, '2', '213', '7113' FROM dual
    UNION ALL SELECT 14, '응급의학과', '응급 및 병리 지원 연계', '응급동 1층', 'EMERGENCY', 'Y', 14, '1', 'E101', '7114' FROM dual
    UNION ALL SELECT 15, '진료지원간호', '처치 및 투약 지원 간호', '본관 3층', 'SUPPORT_NURSING', 'Y', 15, '3', '315', '7215' FROM dual
    UNION ALL SELECT 16, '행정팀', '관리 및 인증 운영 지원', '본관 4층', 'ADMIN_SUPPORT', 'Y', 16, '4', '401', '7301' FROM dual
    UNION ALL SELECT 17, '시설관리팀', '시설 및 장비 운영 지원', '별관 1층', 'FACILITY_SUPPORT', 'Y', 17, '1', 'B101', '7302' FROM dual
    UNION ALL SELECT 18, '영상의학과', '영상 검사 및 판독 지원', '진단동 1층', 'RADIOLOGY', 'Y', 18, '1', 'R101', '7401' FROM dual
    UNION ALL SELECT 19, '검사실', '임상병리 및 검체 검사', '진단동 2층', 'CLINICAL_LAB', 'Y', 19, '2', 'L201', '7402' FROM dual
    UNION ALL SELECT 20, '약제팀', '투약 및 약품 관리', '본관 1층', 'PHARMACY', 'Y', 20, '1', 'P101', '7403' FROM dual
    UNION ALL SELECT 21, '내과-부설', '특수 검진 및 부설 클리닉', '별관 2층', 'INTERNAL_MEDICINE_ANNEX', 'Y', 21, '2', 'A201', '7115' FROM dual
    UNION ALL SELECT 22, '병리지원실', '병리 슬라이드 및 판독 지원', '진단동 2층', 'PATHOLOGY_SUPPORT', 'Y', 22, '2', 'L202', '7404' FROM dual
    UNION ALL SELECT 23, '내시경실', '내시경 검사 및 준비 지원', '진단동 3층', 'ENDOSCOPY_CENTER', 'Y', 23, '3', 'E301', '7405' FROM dual
    UNION ALL SELECT 24, '생리기능검사실', '심전도 및 생리기능 검사', '진단동 3층', 'PHYSIOLOGY_TEST_CENTER', 'Y', 24, '3', 'P301', '7406' FROM dual
) s
ON (t.ID = s.id)
WHEN MATCHED THEN
  UPDATE SET
    t.NAME = s.name,
    t.DESCRIPTION = s.description,
    t.LOCATION = s.location,
    t.DEPT_CODE = s.dept_code,
    t.IS_ACTIVE = s.is_active,
    t.SORT_ORDER = s.sort_order,
    t.FLOOR_NO = s.floor_no,
    t.ROOM_NO = s.room_no,
    t.EXTENSION = s.extension,
    t.UPDATED_AT = SYSDATE
WHEN NOT MATCHED THEN
  INSERT (ID, NAME, DESCRIPTION, LOCATION, HEAD_STAFF_ID, DEPT_CODE, IS_ACTIVE, SORT_ORDER, CREATED_AT, UPDATED_AT, FLOOR_NO, ROOM_NO, EXTENSION)
  VALUES (s.id, s.name, s.description, s.location, NULL, s.dept_code, s.is_active, s.sort_order, SYSDATE, SYSDATE, s.floor_no, s.room_no, s.extension);

UPDATE CMH.DEPARTMENTS
SET IS_ACTIVE = 'N',
    UPDATED_AT = SYSDATE
WHERE ID IN (4, 5, 6, 7, 8, 9, 10);

MERGE INTO CMH.POSITIONS t
USING (
    SELECT 1 AS id, 'ADMIN' AS domain, '관리자' AS title, '시스템 및 조직 운영 관리자' AS description, 'ADMIN_MANAGER' AS position_code, 'Y' AS is_active, 1 AS sort_order FROM dual
    UNION ALL SELECT 2, 'DOCTOR', '의사', '외래 및 입원 진료 의사', 'GENERAL_DOCTOR', 'Y', 2 FROM dual
    UNION ALL SELECT 3, 'NURSE', '간호사', '병동 및 진료지원 간호사', 'SUPPORT_NURSE', 'Y', 3 FROM dual
    UNION ALL SELECT 4, 'RECEPTION', '원무', '접수 및 수납 담당', 'RECEPTION_DESK', 'Y', 4 FROM dual
    UNION ALL SELECT 5, 'RADIOLOGY_TECH', '방사선사', '영상 검사 및 촬영 담당', 'RADIOLOGY_TECH', 'Y', 5 FROM dual
    UNION ALL SELECT 6, 'CLINICAL_LAB_TECH', '임상병리사', '임상병리 검사 담당', 'CLINICAL_LAB_TECH', 'Y', 6 FROM dual
    UNION ALL SELECT 7, 'PATHOLOGY_COORDINATOR', '병리 담당자', '병리 지원 및 검체 흐름 담당', 'PATHOLOGY_COORDINATOR', 'Y', 7 FROM dual
    UNION ALL SELECT 8, 'ENDOSCOPY_COORDINATOR', '내시경 담당자', '내시경실 운영 및 준비 담당', 'ENDOSCOPY_COORDINATOR', 'Y', 8 FROM dual
    UNION ALL SELECT 9, 'PHYSIOLOGY_TEST_COORDINATOR', '생리기능검사 담당자', '생리기능검사 운영 담당', 'PHYSIOLOGY_TEST_COORDINATOR', 'Y', 9 FROM dual
    UNION ALL SELECT 10, 'DOCTOR', '응급의학 전문의', '응급의학 진료 담당 의사', 'EMERGENCY_DOCTOR', 'Y', 10 FROM dual
    UNION ALL SELECT 11, 'DOCTOR', '응급의학 계약의', '응급의학 계약직 의사', 'CONTRACT_DOCTOR', 'Y', 11 FROM dual
) s
ON (t.ID = s.id)
WHEN MATCHED THEN
  UPDATE SET
    t.DOMAIN = s.domain,
    t.TITLE = s.title,
    t.DESCRIPTION = s.description,
    t.POSITION_CODE = s.position_code,
    t.IS_ACTIVE = s.is_active,
    t.SORT_ORDER = s.sort_order,
    t.UPDATED_AT = SYSDATE
WHEN NOT MATCHED THEN
  INSERT (ID, DOMAIN, TITLE, DESCRIPTION, POSITION_CODE, IS_ACTIVE, SORT_ORDER, CREATED_AT, UPDATED_AT)
  VALUES (s.id, s.domain, s.title, s.description, s.position_code, s.is_active, s.sort_order, SYSDATE, SYSDATE);

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'ADMIN',
    DEPT_ID = 16,
    POSITION_ID = 1,
    OFFICE_LOCATION = '본관 4층 401호',
    PHONE = '010-7000-0001',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'admin';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'DOCTOR',
    DEPT_ID = 11,
    POSITION_ID = 2,
    OFFICE_LOCATION = '본관 2층 211호',
    PHONE = '010-7000-0002',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'doctor';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'NURSE',
    DEPT_ID = 15,
    POSITION_ID = 3,
    OFFICE_LOCATION = '본관 3층 315호',
    PHONE = '010-7000-0003',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'nurse';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'RECEPTION',
    DEPT_ID = 1,
    POSITION_ID = 4,
    OFFICE_LOCATION = '본관 1층 접수창구',
    PHONE = '010-7000-0004',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'reception';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'NURSE',
    DEPT_ID = 15,
    POSITION_ID = 3,
    OFFICE_LOCATION = '본관 3층 315호',
    PHONE = '010-7000-0005',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'dark';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'RADIOLOGY_TECH',
    DEPT_ID = 18,
    POSITION_ID = 5,
    OFFICE_LOCATION = '진단동 1층 R101',
    PHONE = '010-7000-0006',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'blaster';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'CLINICAL_LAB_TECH',
    DEPT_ID = 19,
    POSITION_ID = 6,
    OFFICE_LOCATION = '진단동 2층 L201',
    PHONE = '010-7000-0007',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'zero';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'PATHOLOGY_COORDINATOR',
    DEPT_ID = 22,
    POSITION_ID = 7,
    OFFICE_LOCATION = '진단동 2층 L202',
    PHONE = '010-7000-0008',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'demonavenger';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'ENDOSCOPY_COORDINATOR',
    DEPT_ID = 23,
    POSITION_ID = 8,
    OFFICE_LOCATION = '진단동 3층 E301',
    PHONE = '010-7000-0009',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'len';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'PHYSIOLOGY_TEST_COORDINATOR',
    DEPT_ID = 24,
    POSITION_ID = 9,
    OFFICE_LOCATION = '진단동 3층 P301',
    PHONE = '010-7000-0010',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'adel';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'DOCTOR',
    DEPT_ID = 12,
    POSITION_ID = 2,
    OFFICE_LOCATION = '본관 2층 212호',
    PHONE = '010-7000-0011',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'soulmaster';

UPDATE CMH.STAFF
SET STATUS = 'ACTIVE',
    STATUS_CODE = 'ACTIVE',
    DOMAIN_ROLE = 'ADMIN',
    DEPT_ID = 16,
    POSITION_ID = 1,
    OFFICE_LOCATION = '본관 4층 402호',
    PHONE = '010-7000-0012',
    UPDATED_AT = SYSDATE
WHERE USERNAME = 'kaiser';

MERGE INTO CMH.STAFF t
USING (
    SELECT 30 AS id, 'admin_ops1' AS username, '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c' AS password_hash, 'ACTIVE' AS status, 'ADMIN' AS domain_role, '관리자 민지원' AS full_name, '본관 4층 401호' AS office_location, '010-7100-0030' AS phone, 16 AS dept_id, 1 AS position_id FROM dual
    UNION ALL SELECT 31, 'admin_ops2', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'ADMIN', '관리자 한도윤', '본관 4층 402호', '010-7100-0031', 16, 1 FROM dual
    UNION ALL SELECT 32, 'internaldoc1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'DOCTOR', '내과의 김서준', '본관 2층 211호', '010-7100-0032', 11, 2 FROM dual
    UNION ALL SELECT 33, 'surgerydoc1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'DOCTOR', '외과의 박지후', '본관 2층 212호', '010-7100-0033', 12, 2 FROM dual
    UNION ALL SELECT 34, 'orthodoc1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'DOCTOR', '정형외과 최연우', '본관 2층 213호', '010-7100-0034', 13, 2 FROM dual
    UNION ALL SELECT 35, 'erdoc1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'DOCTOR', '응급의 윤서하', '응급동 1층 E101', '010-7100-0035', 14, 10 FROM dual
    UNION ALL SELECT 36, 'nurseward1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'NURSE', '간호사 오병동', '본관 3층 315호', '010-7100-0036', 15, 3 FROM dual
    UNION ALL SELECT 37, 'nurseward2', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'NURSE', '간호사 정지원', '본관 3층 316호', '010-7100-0037', 15, 3 FROM dual
    UNION ALL SELECT 38, 'receptiondesk1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'RECEPTION', '원무 한창구', '본관 1층 접수창구', '010-7100-0038', 1, 4 FROM dual
    UNION ALL SELECT 39, 'receptiondesk2', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'RECEPTION', '원무 장창구', '본관 1층 수납창구', '010-7100-0039', 1, 4 FROM dual
    UNION ALL SELECT 40, 'radtech1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'RADIOLOGY_TECH', '방사선사 이영상', '진단동 1층 R101', '010-7100-0040', 18, 5 FROM dual
    UNION ALL SELECT 41, 'radtech2', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'RADIOLOGY_TECH', '방사선사 송촬영', '진단동 1층 R102', '010-7100-0041', 18, 5 FROM dual
    UNION ALL SELECT 42, 'labtech1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'CLINICAL_LAB_TECH', '임상병리사 남검사', '진단동 2층 L201', '010-7100-0042', 19, 6 FROM dual
    UNION ALL SELECT 43, 'labtech2', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'CLINICAL_LAB_TECH', '임상병리사 류채혈', '진단동 2층 L202', '010-7100-0043', 19, 6 FROM dual
    UNION ALL SELECT 44, 'pathcoord1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'PATHOLOGY_COORDINATOR', '병리담당 곽슬라이드', '진단동 2층 P221', '010-7100-0044', 22, 7 FROM dual
    UNION ALL SELECT 45, 'endocoord1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'ENDOSCOPY_COORDINATOR', '내시경담당 백준비', '진단동 3층 E301', '010-7100-0045', 23, 8 FROM dual
    UNION ALL SELECT 46, 'physiocoord1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'PHYSIOLOGY_TEST_COORDINATOR', '생리검사 신파형', '진단동 3층 P301', '010-7100-0046', 24, 9 FROM dual
    UNION ALL SELECT 47, 'contractdoc1', '0ffe1abd1a08215353c233d6e009613e95eec4253832a761af28ff37ac5a150c', 'ACTIVE', 'DOCTOR', '계약의 노응급', '응급동 1층 E102', '010-7100-0047', 14, 11 FROM dual
) s
ON (LOWER(t.USERNAME) = LOWER(s.username))
WHEN MATCHED THEN
  UPDATE SET
    t.PASSWORD_HASH = s.password_hash,
    t.STATUS = s.status,
    t.STATUS_CODE = s.status,
    t.DOMAIN_ROLE = s.domain_role,
    t.FULL_NAME = s.full_name,
    t.OFFICE_LOCATION = s.office_location,
    t.PHONE = s.phone,
    t.DEPT_ID = s.dept_id,
    t.POSITION_ID = s.position_id,
    t.UPDATED_AT = SYSDATE
WHEN NOT MATCHED THEN
  INSERT (ID, USERNAME, PASSWORD_HASH, STATUS, STATUS_CODE, DOMAIN_ROLE, FULL_NAME, OFFICE_LOCATION, PHONE, DEPT_ID, POSITION_ID, CREATED_AT, UPDATED_AT)
  VALUES (s.id, s.username, s.password_hash, s.status, s.status, s.domain_role, s.full_name, s.office_location, s.phone, s.dept_id, s.position_id, SYSDATE, SYSDATE);

COMMIT;
