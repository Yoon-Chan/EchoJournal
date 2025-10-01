# EchoJournal

해당 개발은 PL-Coding 강의 The Android Architecture Masterclass를 이용한 음성 녹음 앱입니다.

Figma : https://www.figma.com/design/Mrdck7icWtTM1fysRgAmao/EchoJournal?node-id=96-10621&t=WqB0xiHR0PX5NdQS-1

<img width="1920" height="1080" alt="thumbnail" src="https://github.com/user-attachments/assets/3c5fd55b-e2b6-4eff-9bd7-31f699a2147e" />


##  프로젝트를 통해 얻은 경험 및 블로그 기록들
+  해시태그 엔티티와 기록에 대한 정보인 Echo 엔티티를 분리 및 멀티테이블로 다대다 연결 작업을 통해 Topic의 정보를 가져올 때 Echo의 모든 해시태그 정보를 조회하지 않고 전체 해시태그를 가져올 수 있도록 구현
+  MediaRecorder를 이용한 음성녹음 기능 구현을 경험하였으며, Coroutine을 이용한 비동기 작업 처리 진행 경험
+ Modifier.pointerInput을 활용한 드래그 기능을 통해 Quick Record 기능 구현
+ MVI 아키텍처를 이용한 단방향 흐름(State -> Event -> Effect) 구조를 학습을 통해 UI의 흐름을 직관적으로 판단하여 문제점을 쉽게 발견할 수 있는 장점
+ Clean Architecture 준수하여 패키지 분리 및 Mapper를 통한 데이터 변환 기능 경험
+  Bundle의 음성 녹음 크기 제한의 문제점과 음성 녹음의 길이를 통일하기 위함 길이 Normalizer기능을 통해 길이에 맞게 녹음 길이 재설정 및 진폭 조절 기능 경험
+ 위젯 구현을 통해 앱을 실행하지 않아도 위젯 클릭으로 빠른 녹음 기능 구현
+ 음성 녹음 파일을 cache에 저장 후 실제 일기 작성 완료 시 file에 저장 후 cacheDir 초기화 기능 경험
+  다양한 유틸리티 작성 방법 경험(stringResource 관리하기 위한 UiText 구현, 날짜 정보 변환 기능, foreground 상태 여부 판단 기능

### 블로그
+ [Room 다중 관계 적용기](https://velog.io/@vsvx13/AndroidRoom-Room-Entity-%EB%8B%A4%EC%A4%91-%EA%B4%80%EA%B3%84-%EC%82%AC%EC%9A%A9%ED%95%B4%EB%B3%B4%EA%B8%B0)
+ [음성녹음 구현 기록 블로그](https://velog.io/@vsvx13/Android-MediaRecorder%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%B4-%EC%9D%8C%EC%84%B1%EB%85%B9%EC%9D%8C%EC%9D%84-%ED%95%B4%EB%B3%B4%EC%9E%90) 
+ [음성 녹음 길이 조절 방법](https://velog.io/@vsvx13/Android-%EC%9D%8C%EC%84%B1-%EB%85%B9%EC%9D%8C-%EA%B8%B8%EC%9D%B4%EB%A5%BC-%EC%A1%B0%EC%A0%88%ED%95%B4%EB%B3%B4%EC%9E%90)
+ [Foreground 감지하는 법](https://velog.io/@vsvx13/Android-produceState%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%B4-Foreground-%ED%99%94%EB%A9%B4-%EC%97%AC%EB%B6%80-%ED%99%95%EC%9D%B8%ED%95%98%EA%B8%B0)

---

## 기술 스택
| 기술 스택             |                                                              |
| --------------------- | ------------------------------------------------------------ |
| 안드로이드 라이브러리 | Compose UI<br />Koin (version 4.0.0)<br />Room (2.7.0)<br />Ksp (version 2.1.20-2.0.0)<br />serialization (version 1.8.1)<br />Timber (version 5.0.1)<br />glance (version 1.1.1)<br />DataStroe |
| 아키텍처              | MVI 아키텍처 |

---

## 실행 영상

| 음성 녹음 | 퀵 음성 녹음 | 위젯 녹음 |
|:--:|:--:|:--:|
|![일반녹음](https://github.com/user-attachments/assets/baebdb6f-54e9-48bf-b95e-af741133fee3)|![퀵음성녹음](https://github.com/user-attachments/assets/04be95aa-2b99-40c1-983c-72c0cbbdec17)| ![위젯 녹음](https://github.com/user-attachments/assets/bfe2521c-92ef-481d-9137-d8ebe2bf0c08)|

| 글작성 | 분류 - 감정 | 분류 - Topic |
|:--:|:--:|:--:|
|![글작성](https://github.com/user-attachments/assets/d26ceaa6-885c-4f26-9147-f0ac45c3b788)|![감정카테고리분류](https://github.com/user-attachments/assets/b229d49b-e601-4199-9e6e-f8f498d49a2f)|![해시태그분류](https://github.com/user-attachments/assets/264da62e-4b60-4677-bdc0-fce068f86601)|


---
