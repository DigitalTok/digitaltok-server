> **프로젝트 한 줄 소개:** 나만의 전자 잉크 키링 서비

[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=YOUR_REPO_URL)](https://hits.seeyoufarm.com)
![Java](https://img.shields.io/badge/Java-17-007396?style=flat&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-6DB33F?style=flat&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white)

<br>

## 1. 🚩 프로젝트 개요
- **개발 기간:** 2025.12.12 ~ 2026.02.19
- **개발 인원:** Backend 3
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

## 3. 🏗️ 아키텍처 및 설계 (Architecture & Design)
### 3-1. 시스템 아키텍처
<img width="2816" height="1536" alt="digitaltok 아키텍처구조" src="https://github.com/user-attachments/assets/f65c9d9c-9503-405d-bc6b-97a074a7efa3" />

### 3-2. ERD (Entity Relationship Diagram)
*(여기에 ERD 이미지를 넣으세요)*
`![ERD](./images/erd.png)`

<br>

## 4. ✨ 주요 기능 (Key Features)
* **회원가입/로그인:** JWT 기반 인증, OAuth2 (Kakao, Google)
* **이미지 생성:** S3를 이용한 이미지 업로드

<br>


## 5. 📂 디렉토리 구조 (Directory Structure)
```bash
src
├── main
│   ├── java
│   │   └── com.example.project
│   │       ├── common       # 공통 처리 (Exception, DTO)
│   │       ├── config       # 설정 파일
│   │       ├── controller   # 컨트롤러
│   │       ├── domain       # 엔티티
│   │       ├── repository   # 리포지토리
│   │       └── service      # 비즈니스 로직
│   └── resources
└── test

