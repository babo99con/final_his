# module 폴더 재정리

이번 수정에서는 `staff_management` 내부를 다음 두 축으로 분리했습니다.

- `facade/*` : 업무(유스케이스) 축
  - `onboarding`
  - `profile`
  - `assignment`
- `module/*` : 역할/도메인 축
  - `doctor`
  - `nurse`

## 최종 방향

```text
com.staff.staff_management
 ├─ facade 성격의 업무 모듈
 │   ├─ onboarding
 │   ├─ profile
 │   └─ assignment
 │
 └─ module
     ├─ doctor
     └─ nurse
```

## 의미

- 퍼사드는 등록/프로필/배정 같은 업무 흐름을 담당합니다.
- module 아래 도메인 모듈은 역할별 엔티티/서비스/리포지토리를 담당합니다.
- 흐름은 `Controller -> Facade -> Domain Service -> Repository` 입니다.
