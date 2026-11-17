# 병원정보시스템(HIS) 로컬 통합 프로젝트

학원에서 함께 진행한 병원정보시스템을 바탕으로 서비스와 프론트엔드를 로컬에서 함께 실행하도록 정리한 프로젝트입니다. 이후 개인적으로 실행 환경과 서비스 연결을 정리하는 과정에서 AI 코딩 도구 Codex를 활용했습니다.

인증, 환자, 접수, 직원, 진료, 진료지원, 수납 서비스를 Next.js 화면과 연결합니다. Oracle XE, Redis, Kafka, MinIO를 Docker Compose로 함께 실행합니다.

![관리자 계정으로 로그인한 병원 업무 화면](docs/images/home.png)

## 구성

```text
브라우저 → Next.js
              ├─ 인증 · 환자 · 접수 · 직원
              └─ 진료 · 진료지원 · 수납
                          │
              Oracle · Redis · Kafka · MinIO
```

| 서비스 | 포트 | 역할 |
| --- | --- | --- |
| 프론트엔드 | 3001 | 병원 업무 화면 |
| auth-service | 8586 | 로그인 및 인증 |
| patient-service | 8182 | 환자 정보 |
| reception-service | 8283 | 외래 접수 |
| staff-service | 8023 | 직원 정보 |
| clinical-service | 8090 | 진료 |
| clinic-support-service | 8189 | 진료지원 |
| billing-service | 8081 | 청구와 수납 |
| Oracle XE / Kafka / Redis | 1521 / 9092 / 6379 | DB, 이벤트, 캐시 |
| MinIO | 9000 / 9001 | 파일 저장 / 관리 콘솔 |

## 개인적으로 정리한 작업

- 여러 서비스의 Dockerfile, 환경 변수, 네트워크 주소를 연결하고 Compose 실행 환경을 정리했습니다. [통합 커밋](https://github.com/babo99con/final_his/commit/7bb71dd915a9405a8bad0d839c2954a90efc159f)
- Oracle이 준비된 다음 데이터 덤프를 복원하고, 복원 성공 후 서비스를 시작하도록 구성했습니다. 기존 데이터가 재시작 때 덮어써지지 않도록 복원 상태를 확인합니다.
- MinIO 서버와 클라이언트는 공개 소스의 고정 버전으로 빌드합니다. 버킷 초기화는 별도 컨테이너에서 처리합니다.
- 진료 서비스의 Kafka 소비자가 컨테이너 내부에서 `kafka:9092`로 접속하도록 설정했습니다.
- 서버 세션 인증 전환을 별도 브랜치에서 실험했습니다. 이 변경은 [PR #1](https://github.com/babo99con/final_his/pull/1)에 있으며 현재 `main`에 병합되지 않았습니다. PR에는 Java 컴파일, 프론트엔드 린트와 빌드 결과가 기록되어 있습니다.

## 실행 방법

Git과 Docker Desktop이 필요합니다. Windows CMD에서 실행합니다.

```cmd
git clone https://github.com/babo99con/final_his.git
cd final_his
copy .env.example .env
docker compose up --build -d
```

처음에는 이미지 빌드와 Oracle 데이터 복원에 시간이 걸립니다. 상태를 확인합니다.

```cmd
docker compose ps -a
docker compose logs --tail 30 oracle-restore
```

`oracle-restore`가 종료 코드 0으로 완료되면 [http://localhost:3001](http://localhost:3001)에 접속합니다.

- 데모 로그인 ID: `26-6001`
- 데모 로그인 비밀번호: `1111`
- 프론트엔드: `http://localhost:3001`
- 인증 API: `http://localhost:8586`

기본 포트가 사용 중이면 `.env`에서 호스트 포트를 변경합니다. Toss 결제 승인을 시험할 때는 테스트용 `TOSS_SECRET_KEY`가 필요합니다. 약품·상병·처치 검색에 사용되는 `HOSPITAL.CODE_DETAIL`은 제공된 덤프에서 비어 있습니다.

## 데이터 유지 및 초기화

`docker compose down`은 컨테이너를 내리고 데이터 볼륨은 유지합니다. 다시 실행할 때 복원 완료 표시가 있으면 기존 Oracle 데이터를 사용합니다.

과거 버전으로 생성한 볼륨에 HOSPITAL 스키마가 있다면 자동 복원 없이 기존 데이터를 사용합니다. 최초 복원 도중 오류가 발생했다면 로그와 스키마 상태를 확인해야 합니다.

`docker compose down -v`는 이 프로젝트의 데이터 볼륨을 삭제합니다. 처음부터 재생성이 필요할 때 사용하세요.

## 실행 확인

- `docker compose up --build -d`로 서비스 이미지를 빌드하고 전체 컨테이너를 시작했습니다.
- 빈 Oracle 볼륨에서 스키마 복원, 재실행 시 복원 생략과 기존 스키마 보존을 확인했습니다.
- 데모 계정의 화면 로그인과 환자·접수·직원·진료·수납 API 응답을 확인했습니다. 진료지원 서비스의 Swagger UI도 응답했습니다.
- MinIO의 세 버킷 생성과 파일 쓰기·읽기, Kafka의 네 소비자 그룹 연결을 확인했습니다.

## 이미지로 배포

기본 Compose 파일은 소스에서 이미지를 빌드합니다. 앱 이미지를 레지스트리에 게시하려면 `.env`의 `HIS_IMAGE_PREFIX`를 Docker Hub 사용자명으로, `HIS_IMAGE_TAG`를 배포 버전으로 설정합니다.

```cmd
docker login
docker compose build
docker compose push
```

다른 컴퓨터에서는 같은 `docker-compose.yml`과 이미지 이름을 지정한 `.env`를 두고 실행할 수 있습니다.

```cmd
docker compose pull
docker compose up -d --no-build
```

Oracle 복원 이미지에 DB 덤프가 포함되므로 이미지를 공개하기 전에 덤프의 내용을 확인하세요. MinIO 소스는 AGPL 라이선스가 적용됩니다. 이 구성의 계정과 비밀번호는 로컬 데모용입니다.

## 파일 구조

- `HIS/microservices/`: 병원 업무 백엔드 서비스
- `frontend/`: Next.js 화면
- `HOSPITAL_EXPORT/`: Oracle XE 덤프
- `infra/minio/`: MinIO 서버와 클라이언트 이미지 빌드
- `infra/oracle/`: Oracle 스키마 및 덤프 복원 이미지
- `docker-compose.yml`: 전체 실행 구성
- [LOCAL_SETUP_README.md](LOCAL_SETUP_README.md): 개별 서비스 수동 설치와 문제 해결 안내
