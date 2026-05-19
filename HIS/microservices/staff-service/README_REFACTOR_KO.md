# Employee CBD 대규모 현업형 리팩토링본

이 압축본은 업로드된 `Employee.zip` 실제 소스를 기준으로 다음 원칙을 반영해 재구성한 버전이다.

- CBD 기반 상위 컴포넌트 정리
- `staff_management` 컴포넌트 도입
- `doctor`, `nurse`를 `staff_management` 하위 서브도메인으로 이동
- `common` 공통 계층 추가 (`response`, `config`, `mapper`, `exception`)
- `storage/minio` 공통 인프라 유지
- DTO는 Lombok 기반 유지
- 에픽 진입점 성격의 `facade` 추가
- 상위 기능 서비스 `StaffManagementService` 추가

## 주의
- 이 환경에서는 외부 의존성 다운로드가 막혀 있어 최종 Gradle 컴파일 검증은 하지 못했다.
- 대신 패키지 구조, import, XML namespace, 클래스명 정리까지는 실제 소스 기준으로 반영했다.
