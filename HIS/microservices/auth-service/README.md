# AuthService Backend

??釉뚮옖移섏뿉??`portfolio-react/backend` 紐⑤뱢?먯꽌 媛쒕컻??諛깆뿏???뚯뒪媛 ?ㅼ뼱 ?덉뒿?덈떎.

## Primary Entry Points

?ㅼ쓬 REST ?붾뱶?ъ씤?몃뱾? ?몄쬆, ?ъ슜??沅뚰븳 愿由? 硫붾돱 援ъ꽦??泥섎━?⑸땲??

| Path | Method | Purpose |
| --- | --- | --- |
| /api/auth/login | POST | Credential login that creates a server-side HTTP session. |
| /api/auth/me | GET | Returns the authenticated profile with resolved operational role. |
| /api/auth/menu | GET | Returns the hierarchical menu tree granted to the caller. |
| /api/auth/permission/users | GET/POST | Query or update user menu permissions. |
| /api/auth/permission/roles | GET/POST | Query or update role menu permissions. |
| /api/auth/register | POST | Submit a registration request for approval. |
| /api/auth/session/validate | GET | Validates the current server-side session. |

`MenuAccessFilter`媛 `/api/**` 寃쎈줈瑜?紐⑤몢 媛먯떥硫? 硫붾돱 湲곕컲 ?덉슜 紐⑸줉??寃利앺빀?덈떎.

## Local Notes

1. ?쒕퉬?ㅻ? (`./gradlew bootRun` ?먮뒗 `docker compose up`) ?ㅽ뻾?섍퀬 濡쒓렇????`/api/auth/menu`媛 湲곕???硫붾돱 ?몃━瑜?諛섑솚?섎뒗吏 ?뺤씤?섏꽭??

2. Postman ?먮뒗 curl濡?`/api/auth/login` ??`/api/auth/me`瑜?遺덈윭 ?좏겙怨???븷/硫붾돱 ?댁꽍???뺤긽?곸쑝濡??숈옉?섎뒗吏 寃利앺븯?몄슂.

3. ?쒕뱶 ?곗씠?곕? 媛깆떊?섍굅??硫붾돱 沅뚰븳 留듯븨???섏젙???뚮뒗 `docker/oracle/init` ?꾨옒 SQL ?ㅽ겕由쏀듃瑜??ㅽ뻾?섏꽭??

4. 媛踰쇱슫 ?쏣2E??寃利앹쓣 ?꾪빐 濡쒓렇????硫붾돱 議고쉶 ??沅뚰븳 ?뺤씤 ?먮쫫(媛꾨떒??Jest ?뚯뒪?몃굹 curl ?ㅽ겕由쏀듃)???묒꽦??濡쒖뺄?먯꽌 ?ㅽ뻾?대낫?몄슂.

?댁슜???뺤씤?덉쑝硫???README瑜?`develop`???몄떆?섏떆硫??⑸땲??
