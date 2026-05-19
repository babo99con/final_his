# ENTITY SPLIT NOTE (KO)

이번 수정은 "엔터티도 공통 도메인 기준으로 분리할 필요가 있는가"에 대한 1차 반영이다.

## 반영 내용
- `module/staff/entity/StaffEntity` 추가
- `module/staff/repository/StaffRepository` 추가
- `StaffCommonService`, `StaffCommonServiceImpl` 주석 보강

## 왜 doctor / nurse에 바로 연관관계를 붙이지 않았나?
현재 프로젝트의 실사용 SQL/엔터티를 보면:
- `EMPLOYEE_SIGNUP` = 공통 직원 정보
- `EMPLOYEE_DOCTOR`, `EMPLOYEE_NURSE` = 역할 상세

이 철학은 맞다.
하지만 현재 active 스키마/엔터티 기준으로는 doctor / nurse 쪽 `STAFF_ID` FK가 아직 확정적으로 연결되지 않은 상태다.
그래서 이번 단계에서는 다음처럼 보수적으로 정리했다.

1. 공통 staff 엔터티를 별도 모듈로 분리한다.
2. 공통 staff 리포지토리도 따로 둔다.
3. 기존 doctor / nurse 저장 로직은 깨지지 않도록 유지한다.
4. 실제 FK 연관관계는 DB 스키마가 고정되면 다음 단계에서 붙인다.

## 지금 구조 해석
- `module/staff` = 공통 직원 도메인
- `module/doctor` = 의사 역할 상세 도메인
- `module/nurse` = 간호사 역할 상세 도메인

즉 이제 엔터티 관점에서도 `공통 staff -> 역할 상세` 철학을 시작한 상태다.
