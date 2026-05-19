# SupportService

병원 **진료지원 영역**을 담당하는 Spring Boot 기반 REST API 서비스입니다.  
간호 문진(Nursing Assessment) 관리 기능을 중심으로,  
진료 전 단계에서 필요한 보조 정보를 관리합니다.

본 서비스는 애자일(Scrum) 방식으로 개발되며,  
Sprint 1에서는 **간호 문진 CRUD 흐름 구현**을 목표로 합니다.

---

## Features (Sprint 1)

- 간호 문진 신규 등록
- 간호 문진 목록 조회
- 간호 문진 상세 조회
- 간호 문진 수정
- 간호 문진 비활성화 (삭제 대체)

※ 활력징후(Vital Sign) 관리는 다음 Sprint에서 확장 예정

---

## Tech Stack

- **Java 17**
- **Spring Boot 3.3.x**
- **Gradle**

### Backend
- Spring Web (REST API)
- Spring Validation
- Spring JDBC
- MyBatis (CRUD 및 조회)
- Lombok

### Database
- Oracle Database
- Oracle JDBC Driver (ojdbc11)

### API Documentation
- SpringDoc OpenAPI (Swagger UI)

---

## Architecture

- 진료지원(MSA) 서비스 중 **간호 문진 관리 책임**을 담당
- 문진 데이터는 방문(visit) 단위로 관리
- 다른 서비스(Patient, Visit)와는 느슨한 식별자 기반 연계

---

## Requirements

- JDK 17
- Oracle Database (로컬 또는 원격)
- Gradle

---


#### 권장 환경변수

SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
