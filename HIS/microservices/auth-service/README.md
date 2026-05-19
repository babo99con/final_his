# AuthService Backend

이 브랜치에는 `portfolio-react/backend` 모듈에서 개발한 백엔드 소스가 들어 있습니다.

## Primary Entry Points

다음 REST 엔드포인트들은 인증, 사용자/권한 관리, 메뉴 구성을 처리합니다:

| Path | Method | Purpose |
| --- | --- | --- |
| /api/auth/login | POST | Credential login that returns JWT + refresh tokens. |
| /api/auth/me | GET | Returns the authenticated profile with resolved operational role. |
| /api/auth/menu | GET | Returns the hierarchical menu tree granted to the caller. |
| /api/auth/permission/users | GET/POST | Query or update user menu permissions. |
| /api/auth/permission/roles | GET/POST | Query or update role menu permissions. |
| /api/auth/register | POST | Submit a registration request (email verification + approval). |
| /api/auth/session/refresh | POST | Refresh the current JWT session. |
| /api/auth/verification/email/send | POST | Send a registration email code. |
| /api/auth/verification/email/confirm | POST | Confirm the email code and issue a verification token. |
| /oauth2/authorize (Naver/Google) | GET | Redirect to upstream OAuth provider. |

`MenuAccessFilter`와 `JwtAuthenticationFilter`가 `/api/**` 경로를 모두 감싸며, 메뉴 기반 허용 목록을 검증합니다.

## Local Verification Notes

1. 서비스를 (`./gradlew bootRun` 또는 `docker compose up`) 실행하고 로그인 후 `/api/auth/menu`가 기대한 메뉴 트리를 반환하는지 확인하세요.

2. Postman 또는 curl로 `/api/auth/login` → `/api/auth/me`를 불러 토큰과 역할/메뉴 해석이 정상적으로 동작하는지 검증하세요.

3. 시드 데이터를 갱신하거나 메뉴 권한 맵핑을 수정할 때는 `docker/oracle/init` 아래 SQL 스크립트를 실행하세요.

4. 가벼운 “E2E” 검증을 위해 로그인 → 메뉴 조회 → 권한 확인 흐름(간단한 Jest 테스트나 curl 스크립트)을 작성해 로컬에서 실행해보세요.

내용을 확인했으면 이 README를 `develop`에 푸시하시면 됩니다.
