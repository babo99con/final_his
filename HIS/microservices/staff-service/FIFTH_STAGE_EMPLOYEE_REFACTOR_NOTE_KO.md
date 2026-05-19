# Employee 100% 스타일 정리 노트

이번 정리는 `module` 이라는 애매한 이름을 `employee` 로 변경하고,
역할 도메인 내부도 **조회(Query) / 변경(Command)** 기준으로 한 단계 더 분리한 버전입니다.

## 핵심 변경점

### 1. 패키지/폴더명 변경
- `staff_management/module/*`
- → `staff_management/employee/*`

즉 이제 의미가 더 분명합니다.
- `employee/staff` : 공통 직원 정책/엔터티
- `employee/doctor` : 의사 역할 도메인
- `employee/nurse` : 간호사 역할 도메인

### 2. 퍼사드 책임은 유지
- `onboarding/facade` : 신규 등록 유스케이스
- `profile/facade` : 조회/수정/삭제/프로필 이미지 유스케이스
- `assignment/facade` : 배정 유스케이스

퍼사드는 여전히 **업무 흐름 조합**만 담당합니다.

### 3. 도메인 서비스 분리
기존에는 `DoctorService`, `NurseService` 하나에 조회/변경이 같이 들어 있었는데,
이번엔 현업식으로 아래처럼 분리했습니다.

- `DoctorQueryService` / `DoctorCommandService`
- `NurseQueryService` / `NurseCommandService`

#### Query
- 목록 조회
- 상세 조회

#### Command
- 생성
- 수정
- 삭제
- 프로필 이미지 업로드

### 4. 퍼사드 의존성도 역할별로 분리
#### onboarding
- `DoctorCommandService`
- `NurseCommandService`

#### profile
- `DoctorQueryService`
- `DoctorCommandService`
- `NurseQueryService`
- `NurseCommandService`

즉 이제 퍼사드도 조회/변경을 구분해서 사용합니다.

### 5. MyBatis XML 패키지 경로 수정
- `module.doctor` → `employee.doctor`
- `module.nurse` → `employee.nurse`

## 현재 해석 구조

```text
Controller
  ↓
Facade (업무 유스케이스)
  ↓
Employee Staff Common Policy
  ↓
Doctor/Nurse Query Service
Doctor/Nurse Command Service
  ↓
Mapper / Repository / Storage
```

## 참고
이 버전은 구조를 현업식으로 더 선명하게 다듬은 것이고,
실제 DB 스키마를 전면 변경하는 리팩토링은 아닙니다.
그래서 `StaffEntity ↔ DoctorEntity/NurseEntity` FK/JPA 연관은 보수적으로 유지했습니다.
