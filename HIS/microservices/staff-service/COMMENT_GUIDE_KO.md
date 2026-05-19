# 주석 추가 안내

이번 수정에서는 `staff_management` 중심으로 주석을 보강했습니다.

## 주석을 많이 단 위치
- onboarding/profile/assignment facade
- doctor/nurse controller
- doctor/nurse service 구현체
- staff 공통 service / validator
- doctor/nurse validator
- profile image service
- command(record) 클래스

## 주석 기준
- 클래스 상단: 이 클래스의 역할과 책임
- 메서드 상단: 이 메서드가 하는 일과 흐름
- 필요한 부분: 왜 facade를 거치는지, 왜 command를 쓰는지, 왜 파일 업로드를 분리했는지

## 구조 해석 핵심
- Controller: HTTP 요청/응답
- Facade: 업무 유스케이스 흐름
- StaffCommonService / Validator: 공통 정책, 공통 검증
- DoctorService / NurseService: 역할 모듈 처리
- ProfileImageService: 파일 업로드 전용 처리

