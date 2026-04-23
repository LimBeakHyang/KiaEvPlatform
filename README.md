# Kia EV Platform

Kia EV Platform은 기아 전기차 정보를 제공하고, 상담 신청 및 차량 관련 서비스를 이용할 수 있는 전기차 딜러 플랫폼 팀 프로젝트입니다.

## 프로젝트 소개

전기차 구매를 고려하는 사용자가 차량 정보, 프로모션, 충전소 정보 등을 확인하고 상담을 신청할 수 있도록 구현한 웹 서비스입니다.  
사용자, 관리자, 딜러 역할을 기준으로 기능을 분리하여 설계했으며, 저는 게시판, 프로모션, 충전소, 챗봇 기능을 담당했습니다.

## 담당 구현 기능

### 게시판
- 사용자 게시글 목록 조회
- 게시글 상세 조회
- 게시글 등록, 수정, 삭제
- 게시판 카테고리 기반 화면 구성

### 프로모션
- 전기차 관련 프로모션 목록 조회
- 프로모션 상세 정보 확인
- 사용자 화면에서 프로모션 정보를 확인할 수 있도록 UI 구성

### 충전소
- 충전소 정보 조회 기능 구현
- 충전소 관련 데이터를 사용자 화면에 표시
- 전기차 이용자에게 필요한 충전 인프라 정보 제공

### 챗봇
- AI API 연동이 아닌 선택형/FAQ 기반 챗봇 구현
- 사용자가 버튼 또는 선택지를 클릭하면 미리 정의된 답변을 확인하는 방식
- 차량 정보, 상담, 전기차 관련 문의에 대한 기본 응답 흐름 구성

## 기술 스택

### Backend
- Java
- JDK 21
- Spring Boot
- Spring Data JPA
- Hibernate ORM
- Embedded Tomcat

### Frontend
- HTML
- CSS

### Database
- MySQL

### Collaboration
- Git
- GitHub
- Google Drive

## 프로젝트 구조

```text
src/main/java/com/kiaev
├── client
│   ├── board
│   ├── charging
│   ├── promotion
│   └── recommend
├── cbclient
└── common
```
## 구현하며 배운 점
Spring Boot 기반 MVC 구조 설계
Controller, Service, Repository 계층 분리
JPA를 활용한 데이터 조회 및 관리
사용자 화면과 서버 로직 연결 흐름
AI 연동 없이도 FAQ/선택형 구조로 챗봇 사용자 경험을 구성하는 방법

## 향후 개선 사항
실제 AI API를 연동한 챗봇 응답 기능 추가
충전소 위치 기반 검색 기능 개선
게시판 파일 첨부 및 검색 기능 고도화
프로모션 관리 기능 개선
반응형 UI 보완
