# HIS Local Setup / Handoff Guide

이 문서는 새 PC에 아무것도 없다고 가정하고, 현재 전달 파일만으로 HIS 백엔드, 프론트엔드, Oracle 덤프, Kafka를 실행하는 절차를 정리한 문서입니다.

## 1. 전달 폴더 구조

전달받은 폴더는 아래 구조를 유지하는 것을 권장합니다.

```text
handoff-root/
  HIS/
  frontend/
  HOSPITAL_EXPORT/
    hospital_xe_20260521_212722.dmp
    hospital_xe_20260521_212722.exp.log
  HIS_LOCAL_SETUP_README.md
```

중요한 덤프 파일은 `HOSPITAL_EXPORT/hospital_xe_20260521_212722.dmp`입니다.

## 2. 설치해야 하는 것

Windows 기준입니다.

- Docker Desktop
- JDK 17
- Node.js 20.9 이상
  - 현재 개발 환경은 Node `24.14.0`, npm `11.9.0`에서 빌드 확인됨
- PowerShell

PowerShell에서 `npm`이 실행 정책 때문에 막히면 `npm.cmd`를 사용합니다.

```powershell
npm.cmd --version
npm.cmd ci
```

## 3. 사용하는 포트

아래 포트가 비어 있어야 합니다.

```text
Oracle XE        1521
Kafka            9092
frontend         3001
auth-service     8586
patient-service  8182
reception        8283
staff-service    8023
clinical         8090
clinic-support   8189
billing          8081
```

## 4. Oracle XE 실행

최초 실행:

```powershell
docker run -d `
  --name hospital-oracle `
  -p 1521:1521 `
  -e ORACLE_PASSWORD=1111 `
  gvenzl/oracle-xe:21-slim
```

Oracle 첫 부팅은 몇 분 걸릴 수 있습니다. 아래 로그에서 `DATABASE IS READY TO USE!`가 나올 때까지 기다립니다.

```powershell
docker logs -f hospital-oracle
```

중요:

- 백엔드는 Oracle service name을 `XE`로 봅니다.
- `XEPDB1`에 import하면 앱이 데이터를 못 봅니다.
- JDBC URL은 `jdbc:oracle:thin:@//localhost:1521/XE`입니다.

## 5. HOSPITAL DB 복원

`handoff-root`에서 실행한다고 가정합니다.

```powershell
docker exec hospital-oracle bash -lc "mkdir -p /tmp/datapump"
docker cp .\HOSPITAL_EXPORT\hospital_xe_20260521_212722.dmp hospital-oracle:/tmp/datapump/hospital_xe_20260521_212722.dmp
```

`HOSPITAL` 계정과 Data Pump directory를 생성합니다.

```powershell
@'
alter session set "_ORACLE_SCRIPT"=true;
create user HOSPITAL identified by 1111 default tablespace USERS temporary tablespace TEMP quota unlimited on USERS;
grant create session, resource, connect to HOSPITAL;
grant create view, create sequence, create trigger, create procedure, create table to HOSPITAL;
create or replace directory RESTORE_DIR as '/tmp/datapump';
grant read, write on directory RESTORE_DIR to HOSPITAL;
exit;
'@ | docker exec -i hospital-oracle sqlplus -s system/1111@//localhost:1521/XE
```

덤프를 import합니다.

```powershell
docker exec hospital-oracle bash -lc "impdp system/1111@//localhost:1521/XE directory=RESTORE_DIR dumpfile=hospital_xe_20260521_212722.dmp logfile=hospital_restore.imp.log schemas=HOSPITAL table_exists_action=replace"
```

복원 확인:

```powershell
@'
set pages 200 lines 200
select count(*) as tables from dba_tables where owner='HOSPITAL';
select count(*) as sequences from dba_sequences where sequence_owner='HOSPITAL';
select count(*) as invalid_objects from dba_objects where owner='HOSPITAL' and status <> 'VALID';
select 'AUTH_USER' table_name, count(*) cnt from HOSPITAL.AUTH_USER union all
select 'CMH_MENU', count(*) from HOSPITAL.CMH_MENU union all
select 'PATIENT', count(*) from HOSPITAL.PATIENT union all
select 'RECEPTION', count(*) from HOSPITAL.RECEPTION union all
select 'BILL', count(*) from HOSPITAL.BILL;
exit;
'@ | docker exec -i hospital-oracle sqlplus -s system/1111@//localhost:1521/XE
```

정상 기준:

```text
HOSPITAL tables      102
HOSPITAL sequences   76
invalid objects       0
AUTH_USER             5
CMH_MENU             33
PATIENT               2
RECEPTION             1
BILL                  1
```

로그인 테스트 계정:

```text
ID: 26-6001
PW: 1111
ROLE: ADMIN
```

## 6. Kafka 실행

로컬 PC에서 백엔드를 직접 실행하는 기준입니다. Kafka의 advertised listener를 `localhost:9092`로 열어야 합니다.

```powershell
docker run -d `
  --name hospital-kafka `
  -p 9092:9092 `
  -e KAFKA_NODE_ID=1 `
  -e KAFKA_PROCESS_ROLES=broker,controller `
  -e KAFKA_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 `
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 `
  -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER `
  -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT `
  -e KAFKA_CONTROLLER_QUORUM_VOTERS=1@localhost:9093 `
  -e KAFKA_INTER_BROKER_LISTENER_NAME=PLAINTEXT `
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 `
  apache/kafka:latest
```

Kafka 확인:

```powershell
docker exec hospital-kafka /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list
```

정상 실행 후 consumer group은 아래처럼 보입니다.

```text
clinical-billing
clinical-support
support-billing
support-clinical
```

## 7. 환경변수

백엔드 토스 결제를 쓰려면 `TOSS_SECRET_KEY`가 필요합니다. 키는 코드나 GitHub에 올리지 말고 로컬 환경변수로 설정합니다.

```powershell
$env:TOSS_SECRET_KEY="발급받은_토스_SECRET_KEY"
```

PowerShell 창을 새로 열어도 유지하려면:

```powershell
[Environment]::SetEnvironmentVariable("TOSS_SECRET_KEY", "발급받은_토스_SECRET_KEY", "User")
```

공공 API 키는 받는 사람이 직접 발급해서 설정합니다. 현재 화면 검색은 로컬 `HOSPITAL.CODE_DETAIL` 기반이라, 공공 API 키만 넣는다고 자동으로 데이터가 채워지지는 않습니다.

선택 환경변수:

```text
HIRA_API_KEY
DRUG_API_KEY
DISEASE_API_KEY
```

## 8. 백엔드 빌드

서비스가 이미 실행 중이면 먼저 종료하고 빌드합니다. 실행 중인 Java 프로세스가 공통 `util` JAR를 잡고 있으면 빌드가 실패할 수 있습니다.

`handoff-root/HIS`에서 실행:

```powershell
cd .\HIS
.\microservices\billing-service\gradlew.bat -p . clean buildAll -x test --no-daemon
```

개별 서비스만 빌드할 수도 있습니다.

```powershell
cd .\HIS\microservices\auth-service
.\gradlew.bat clean assemble -x test --no-daemon
```

## 9. 백엔드 실행

`handoff-root`에서 실행한다고 가정합니다.

아래 명령은 각 서비스를 background process로 실행하고 로그를 `HIS/runtime-logs`에 남깁니다.

```powershell
$env:TOSS_SECRET_KEY="발급받은_토스_SECRET_KEY"

$logDir = ".\HIS\runtime-logs"
New-Item -ItemType Directory -Force -Path $logDir | Out-Null

Start-Process java -ArgumentList '-jar ".\HIS\microservices\auth-service\build\libs\hospital-0.0.1-SNAPSHOT.war" --server.port=8586 --spring.datasource.url=jdbc:log4jdbc:oracle:thin:@//localhost:1521/XE --spring.datasource.username=HOSPITAL --spring.datasource.password=1111' -RedirectStandardOutput "$logDir\auth.out.log" -RedirectStandardError "$logDir\auth.err.log"

Start-Process java -ArgumentList '-jar ".\HIS\microservices\patient-service\build\libs\hospital_patients-0.0.1-SNAPSHOT.jar" --server.port=8182 --spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XE --spring.datasource.username=HOSPITAL --spring.datasource.password=1111' -RedirectStandardOutput "$logDir\patient.out.log" -RedirectStandardError "$logDir\patient.err.log"

Start-Process java -ArgumentList '-jar ".\HIS\microservices\reception-service\build\libs\reception-0.0.1-SNAPSHOT.jar" --server.port=8283 --spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XE --spring.datasource.username=HOSPITAL --spring.datasource.password=1111' -RedirectStandardOutput "$logDir\reception.out.log" -RedirectStandardError "$logDir\reception.err.log"

Start-Process java -ArgumentList '-jar ".\HIS\microservices\staff-service\build\libs\Employee-0.0.1-SNAPSHOT.jar" --server.port=8023 --spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XE --spring.datasource.username=HOSPITAL --spring.datasource.password=1111' -RedirectStandardOutput "$logDir\staff.out.log" -RedirectStandardError "$logDir\staff.err.log"

Start-Process java -ArgumentList '-jar ".\HIS\microservices\clinical-service\build\libs\hospital_clinical-0.0.1-SNAPSHOT.jar" --server.port=8090 --spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XE --spring.datasource.username=HOSPITAL --spring.datasource.password=1111 --reception.api.base-url=http://localhost:8283 --clinical-support.api.base-url=http://localhost:8189 --billing.api.base-url=http://localhost:8081' -RedirectStandardOutput "$logDir\clinical.out.log" -RedirectStandardError "$logDir\clinical.err.log"

Start-Process java -ArgumentList '-jar ".\HIS\microservices\clinic-support-service\build\libs\medical_support-0.0.1-SNAPSHOT.war" --server.port=8189 --spring.datasource.url=jdbc:log4jdbc:oracle:thin:@//localhost:1521/XE --spring.datasource.username=HOSPITAL --spring.datasource.password=1111 --integration.reception.base-url=http://localhost:8283 --integration.clinical.base-url=http://localhost:8090' -RedirectStandardOutput "$logDir\clinic-support.out.log" -RedirectStandardError "$logDir\clinic-support.err.log"

Start-Process java -ArgumentList '-jar ".\HIS\microservices\billing-service\build\libs\hospital-billing-backend-0.0.1-SNAPSHOT.jar" --server.port=8081 --spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XE --spring.datasource.username=HOSPITAL --spring.datasource.password=1111 --patient.api.base-url=http://localhost:8182' -RedirectStandardOutput "$logDir\billing.out.log" -RedirectStandardError "$logDir\billing.err.log"
```

포트 확인:

```powershell
8586,8182,8283,8023,8090,8189,8081 | ForEach-Object {
  $p=$_
  $c=Get-NetTCPConnection -State Listen -ErrorAction SilentlyContinue | Where-Object LocalPort -eq $p
  [pscustomobject]@{Port=$p; Listening=!!$c; PID=($c.OwningProcess -join ',')}
} | Format-Table -AutoSize
```

## 10. 프론트엔드 설정 및 실행

`handoff-root/frontend`에서 실행합니다.

```powershell
cd .\frontend
Copy-Item .env.example .env.local
npm.cmd ci
npm.cmd run build
npm.cmd run dev
```

접속:

```text
http://localhost:3001
```

프론트 주요 환경변수 기본값:

```text
NEXT_PUBLIC_AUTH_API_BASE_URL=http://localhost:8586
NEXT_PUBLIC_MENU_API_BASE_URL=http://localhost:8586
NEXT_PUBLIC_BILLING_API_BASE_URL=http://localhost:8081
NEXT_PUBLIC_STAFF_API_BASE_URL=http://localhost:8023
NEXT_PUBLIC_PATIENTS_API_BASE_URL=http://localhost:8182
NEXT_PUBLIC_RECEPTION_API_BASE_URL=http://localhost:8283
NEXT_PUBLIC_CLINICAL_API_BASE_URL=http://localhost:8090
NEXT_PUBLIC_NURSING_API_BASE_URL=http://localhost:8189
NEXT_PUBLIC_TOSS_CLIENT_KEY=토스_CLIENT_KEY
```

## 11. Smoke Test

로그인 확인:

```powershell
$body='{ "username":"26-6001", "password":"1111" }'
Invoke-WebRequest -Uri "http://localhost:8586/api/auth/login" -Method Post -ContentType "application/json" -Body $body -UseBasicParsing
```

주요 API 확인:

```powershell
$session = New-Object Microsoft.PowerShell.Commands.WebRequestSession
$body='{ "username":"26-6001", "password":"1111" }'
Invoke-WebRequest -Uri "http://localhost:8586/api/auth/login" -Method Post -ContentType "application/json" -Body $body -WebSession $session -UseBasicParsing

Invoke-WebRequest -Uri "http://localhost:8182/api/patients" -WebSession $session -UseBasicParsing
Invoke-WebRequest -Uri "http://localhost:8283/api/receptions" -WebSession $session -UseBasicParsing
Invoke-WebRequest -Uri "http://localhost:8090/api/clinical/reception-queue" -WebSession $session -UseBasicParsing
Invoke-WebRequest -Uri "http://localhost:8081/api/billing/bills" -WebSession $session -UseBasicParsing
Invoke-WebRequest -Uri "http://localhost:8189/swagger-ui/index.html" -UseBasicParsing
```

Kafka group 확인:

```powershell
docker exec hospital-kafka /opt/kafka/bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list
```

## 12. 자주 나는 문제

### Oracle import는 됐는데 앱에 데이터가 안 보임

대부분 `XEPDB1`에 import한 경우입니다. 이 앱은 `/XE`를 봅니다.

확인:

```powershell
@'
select count(*) from HOSPITAL.AUTH_USER;
exit;
'@ | docker exec -i hospital-oracle sqlplus -s system/1111@//localhost:1521/XE
```

### Kafka는 켰는데 MSA가 붙지 않음

로컬 Java 실행 기준으로 Kafka advertised listener가 `localhost:9092`여야 합니다.

MSA까지 Docker 안에서 실행할 경우 `localhost`는 각 컨테이너 자신을 의미하므로, `KAFKA_BROKERS`를 Docker service name으로 바꿔야 합니다.

### 빌드 중 `Unable to delete util-1.0.0-SNAPSHOT.jar`

실행 중인 Java 서비스가 공통 JAR를 잡고 있는 상태입니다. 백엔드를 모두 종료한 뒤 다시 빌드합니다.

### PowerShell에서 npm이 막힘

`npm` 대신 `npm.cmd`를 사용합니다.

```powershell
npm.cmd ci
npm.cmd run build
```

### 토스 결제가 실패함

`TOSS_SECRET_KEY`가 백엔드 실행 환경변수에 들어가 있어야 합니다. 프론트에는 `NEXT_PUBLIC_TOSS_CLIENT_KEY`가 필요합니다.

### 약/상병/처치 검색 결과가 비어 있음

현재 화면 검색은 공공 API 직접 호출이 아니라 로컬 `HOSPITAL.CODE_DETAIL` 기준입니다. 현재 덤프의 `CODE_DETAIL`, `CODE_GROUP`은 비어 있습니다. 공공 API 키만 넣는다고 자동으로 데이터가 채워지지는 않습니다.

## 13. GitHub 업로드 시 제외할 것

올려도 되는 것:

```text
HIS 소스
frontend 소스
HOSPITAL_EXPORT/hospital_xe_20260521_212722.dmp
HIS_LOCAL_SETUP_README.md
```

제외할 것:

```text
frontend/node_modules/
frontend/.next/
frontend/.env.local
HIS/**/build/
HIS/.gradle/
HIS/runtime-logs/
HIS/run-logs/
HIS/.codex-runtime-logs/
*.log
토스 secret key
개인 PC 절대경로가 들어간 임시 파일
```
