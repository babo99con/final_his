# 간호기록 접수 목록 연동 API

간호기록 화면의 `접수 환자 목록`은 **진료 지원(medical_support)이 접수 MSA의 당일 목록 API를 호출한 뒤** 상태로 걸러 돌려준다. 접수 MSA 코드는 변경하지 않는다.

## 1) 외래 접수 대기열 (간호기록 왼쪽 목록 권장)

- Method: `GET`
- Path: `/api/receptions/queue`
- Query params
  - `date` (optional): `yyyy-MM-dd` — **생략 시 서버 기준 오늘**
  - `departmentId` (optional): 문자열 코드 (예: `DEPT-003`)
  - `doctorId` (optional): 문자열 (예: `DOC-2026-0010`)

**동작:** 진료 지원 서버가 접수 `GET /api/receptions?dateFrom=…&dateTo=…`(같은 날)로 당일 접수를 받은 뒤, **`visitType=OUTPATIENT`** 이고 **`status`가 `WAITING`, `CALLED`, `IN_PROGRESS`** 인 행만 반환한다. (대기·진료중 등이 큐 API만으로 빠지는 경우를 피하기 위함.)

### 요청 예시

```http
GET /api/receptions/queue
GET /api/receptions/queue?date=2026-04-15
GET /api/receptions/queue?date=2026-04-15&departmentId=DEPT-003
```

### 응답

`ApiResponse<List<OutpatientReceptionDTO>>` 형식 (`success`, `message`, `result`).

---

## 2) 접수 목록(조건) 조회

당일·과·의사 단위로 목록을 쓰고 **상태·진료유형을 직접 지정**할 때 사용한다.

- Method: `GET`
- Path: `/api/receptions`
- Query
  - `visitDate` (required): `yyyy-MM-dd` — 접수 MSA에는 `dateFrom`/`dateTo` 동일 값으로 전달된다.
  - `visitType` (optional, default `OUTPATIENT`)
  - `statuses` (optional, default `WAITING,CALLED,IN_PROGRESS`) — CSV, 대문자로 정규화 후 필터
  - `departmentId` (optional): 문자열 코드 (예: `DEPT-003`)
  - `doctorId` (optional): 문자열 (예: `DOC-2026-0010`)

### 요청 예시

```http
GET /api/receptions?visitDate=2026-04-15
GET /api/receptions?visitDate=2026-04-15&statuses=WAITING,IN_PROGRESS
GET /api/receptions?visitDate=2026-04-15&departmentId=DEPT-003&doctorId=DOC-2026-0010
```

### 응답

`ApiResponse<List<OutpatientReceptionDTO>>` 형식.

---

## 3) 접수 상세 조회

- Method: `GET`
- Path: `/api/receptions/{id}`

---

## 프론트 적용 가이드

- 간호기록 왼쪽 목록은 **`GET /api/receptions/queue`** 또는 **`GET /api/receptions?visitDate=…`** 중 하나를 쓰면 된다. 대기·진료중을 함께 보려면 **`statuses`를 생략**(목록 API 기본값)하거나 `WAITING,IN_PROGRESS` 등으로 지정한다.
- 베이스 URL은 medical_support 서버 (예: `http://localhost:8181`).
