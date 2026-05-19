# 4차 정리 메모

이번 4차 정리는 3차의 `유스케이스 + 공통도메인 + 역할도메인` 구조를 유지하면서,
실제 현업에서 오래 버티도록 책임을 한 단계 더 세분화한 버전이다.

## 이번에 한 일

### 1. validator 분리
- `module/staff/validator/StaffCommonValidator`
- `module/doctor/validator/DoctorValidator`
- `module/nurse/validator/NurseValidator`

기존에 facade / service / staffCommonService 안에 흩어져 있던
대상 ID 검증, 필수값 검증, 업로드 파일 검증을 validator로 이동했다.

### 2. 파일 업로드 책임 분리
- `DoctorProfileImageService`
- `DoctorProfileImageServiceImpl`
- `NurseProfileImageService`
- `NurseProfileImageServiceImpl`

기존에는 doctor/nurse service 구현체가 MinIO 업로드와 파일 URL 저장까지 직접 처리했다.
4차에서는 프로필 이미지 업로드 책임을 전용 서비스로 분리해서,
도메인 서비스가 CRUD 중심 책임에 더 가깝게 유지되도록 정리했다.

### 3. facade는 흐름에 더 집중
- onboarding facade: validator + 공통 normalize + 모듈 서비스 호출
- profile facade: validator + command 변환 + 모듈 서비스 호출
- assignment facade: 공통 validator 기반 검증

### 4. staff common service 역할 축소
기존 `StaffCommonService`에서 단순 ID/파일 검증 책임을 제거하고,
문자열 정규화/역할 기본값 부여 같은 공통 정책 중심으로 정리했다.

## 현재 구조 해석

```text
Controller
  ↓
Facade (업무 유스케이스)
  ↓
Validator / StaffCommonService (검증 + 공통 정책)
  ↓
DoctorService / NurseService (역할 도메인 처리)
  ↓
ProfileImageService (파일처리 세부책임)
  ↓
Mapper / Repository / Storage
```

## 이번 단계의 의미
- 3차: 설계 관점 전환
- 4차: 실제 운영 가능한 방향으로 고도화 시작

즉 4차부터는 "폴더를 더 나눈다"기보다
"검증/파일/정책/흐름 책임을 명확히 분리한다"에 가깝다.

