> **프로젝트 한 줄 소개:** 나만의 전자 잉크 키링 서비

![Java](https://img.shields.io/badge/Java-17-007396?style=flat&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-6DB33F?style=flat&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white)

<br>

## Swagger 주소
http://3.37.213.174:8080/swagger-ui/index.html#

## 1. 🚩 프로젝트 개요
- **개발 기간:** 2025.12.12 ~ 2026.02.19
- **개발 인원:** 4명
- **프로젝트 목적:**

<br>

## 2. 📚 사용 기술 (Tech Stack)

| 구분 | 기술 |
| :-: | - |
| **Language** | Java 17 |
| **Framework** | Spring Boot 3.5.9, Spring Security, Spring Data JPA |
| **Database** | MySQL, Redis |
| **Infra** | AWS EC2, RDS, Docker |
| **Collaboration** | Git, Notion, Discord |

<br>

## 3.🤝 Team Convention (협업 규칙)

### 3-1. Branch Strategy - Git Flow 전략

* **main**: 배포 가능한 상태의 코드가 모이는 브랜치
* **develop**: 다음 버전을 위한 개발이 진행되는 주 브랜치
* **feature**: 기능을 개발하는 브랜치 (develop에서 분기)
    * 브랜치 명: `feature/이슈번호-기능요약`
    * 예시: `feature/12-login-api`
* **hotfix**: 배포된 버전에서 발생한 버그를 긴급 수정 (main에서 분기)


### 3-2. Commit Message Convention
- 제목은 50글자 이내로 작성한다.
- 첫글자는 대문자로 작성한다.
- 마침표 및 특수기호는 사용하지 않는다.
- 영문으로 작성하는 경우 동사(원형)을 가장 앞에 명령어로 작성한다.
- 과거시제는 사용하지 않는다.
- 간결하고 요점적으로 즉, 개조식 구문으로 작성한다.
### 구조 및 작성예시
```text
Feat: 회원가입 시 이메일 중복 검사 로직 추가

기존에 아이디만 중복 검사하던 로직에 이메일 검사 추가함.
DB 쿼리 최적화 포함.

Resolves: #123

```

## 4. 🏗️ 아키텍처 및 설계 (Architecture & Design)
### 4-1. 시스템 아키텍처
<img width="2816" height="1536" alt="digitaltok 아키텍처구조" src="https://github.com/user-attachments/assets/f65c9d9c-9503-405d-bc6b-97a074a7efa3" />

### 4-2. ERD (Entity Relationship Diagram)
<img width="1553" height="958" alt="image" src="https://github.com/user-attachments/assets/c9bbada4-d5ea-4f07-b06b-35df1140d490" />



<br>

## 5. ✨ 주요 기능 (Key Features)
* **회원가입/로그인:** JWT 기반 인증, OAuth2
* **이미지 생성:** 템플릿 이미지 생성 및 S3를 이용한 이미지 업로드
* **이미지 변환:** 전자잉크 디스플레이 전송을 위한 이미지to바이너리 데이터 변환
* **기기 연결:** 전자잉크 디스플레이 연동

<br>


## 6. 📂 디렉토리 구조 (Directory Structure)
```bash
src
├── main
│   ├── java
│   │   └── com.example.project
│   │       ├── global          # 공통 처리 및 전역 설정
│   │          ├── apiPayload   # 공통 처리 (GeneralExceptione, BaseCode)
│   │          ├── config       # 설정 파일
│   │       ├── controller      # 컨트롤러
│   │       ├── convertor       # Entity, DTO 간의 변환
│   │       ├── domain          # 엔티티
│   │       ├── dto             # DTO 클래스
│   │          ├── request      # request DTO
│   │          ├── response     # response DTO
│   │       ├── repository      # 리포지토리
│   │       └── service         # 비즈니스 로직
│   └── resources
└── test

