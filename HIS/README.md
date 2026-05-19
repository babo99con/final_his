# HMS 통합본 모노레포 (Composite Gradle)

이 폴더는 각 서비스를 **한 워크스페이스/한 루트**에서 다루기 위한 모노레포 루트입니다.
현재 단계에서는 각 서비스의 기존 Gradle 구성을 유지한 채, 루트에서 호출할 수 있도록 **Composite Build (`includeBuild`)** 로 묶었습니다.

## 포함된 서비스

- `auth_service`
- `staff_service`
- `patient_service`
- `reception_service`
- `clinical_service`
- `clinicSupport_service`
- `billing_service`

## 빠른 사용법

현재 루트에는 Gradle Wrapper(`gradlew`)를 아직 두지 않았습니다.
대신 **아무 서비스의 `gradlew.bat`로 루트를 실행**할 수 있습니다(권장: `billing_service`).

루트(`c:\dev\HMS\통합본`) 작업 예시:

- 전체 빌드:

```bash
.\billing_service\gradlew.bat -p . buildAll
```

- 특정 서비스만 빌드:

```bash
.\billing_service\gradlew.bat -p . billingBuild
```

- 전체 테스트:

```bash
.\billing_service\gradlew.bat -p . testAll
```

## 다음 단계(예정)

- 공통 코드(`ApiResponse`, 공통 예외/이벤트 모델 등)를 `libs/*`로 분리해 중복 제거
- 로컬 통합 기동을 위한 `docker-compose.yml`을 루트로 승격(서비스별 compose를 정리/통합)
- Kubernetes 배포 표준(Helm/Kustomize) 디렉터리 도입

