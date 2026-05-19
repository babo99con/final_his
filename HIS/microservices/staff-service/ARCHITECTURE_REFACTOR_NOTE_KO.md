
ARCHITECTURE_REFACTOR_NOTE_KO.md

현업 스타일 정리 요약

1. Controller
- DoctorController / NurseController 하나만 유지
- HTTP 진입점 통일

2. Facade
- onboarding / profile / assignment 유스케이스 계층 유지
- Controller -> Facade -> Domain Service 흐름

3. Domain Module (employee)
- doctor
    - command service
    - query service
- nurse
- staff (공통 정책)

4. 이유
- Controller는 HTTP adapter
- Facade는 UseCase orchestration
- Service는 Domain 책임

구조 흐름

Controller
  ↓
Facade (UseCase)
  ↓
employee.staff (공통 정책)
  ↓
employee.doctor / employee.nurse (Domain)
  ↓
Repository / Mapper
