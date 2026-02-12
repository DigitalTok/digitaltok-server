# 🏷️ 나만의 전자 잉크 키링 서비스, DiRing (디링)

![Java](https://img.shields.io/badge/Java-17-007396?style=flat&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.9-6DB33F?style=flat&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-EC2%20%2F%20S3%20%2F%20RDS-232F3E?style=flat&logo=amazon-aws&logoColor=white)

> **"당신의 일상을 키링에 담다."** <br>
> 사용자가 원하는 템플릿이나 이미지를 선택하면 전자 잉크(E-ink) 디스플레이에 맞는 이미지로 변환하여 전자 잉크(E-ink)키링 디바이스로 전송해주는 서비스입니다.

<br>

## 🌐 Swagger API Docs
- **URL:** [[http://3.37.213.174:8080/swagger-ui/index.html](http://3.37.213.174:8080/swagger-ui/index.html)](https://www.diring.site/swagger-ui/index.html#/)

<br>

## 1. 🚩 프로젝트 개요
- **개발 기간:** 2025.12.12 ~ 2026.02.19
- **개발 인원:** 4명 (Backend Team)

### 👨‍💻 팀원 및 담당 역할 (R&R)

| 이름 | 포지션 | 담당 도메인 및 핵심 기여 |
| :---: | :---: | :--- |
| [**이정헌**](https://github.com/LeeJeongHeon02) | **Team Leader** | • **Template & CI/CD** <br>• 지하철 템플릿 이미지 생성 로직 구현 <br>• GitHub Actions 파이프라인 구축 및 서버 관리 |
| [**송정은**](https://github.com/jeun66) | **Sub Leader** | • **Image Processing** <br>• E-ink 전용 4-Color Quantization(양자화) 알고리즘 구현 <br>• 이미지 ↔ 바이너리 데이터 인코딩/디코딩 로직 개발 |
| [**이승주**](https://github.com/dudulic) | **Member** | • **Auth & Security** <br>• JWT 기반 인증/인가 시스템 및 Refresh Token 로테이션 <br>• SMTP 기반 이메일 인증 기능 구현 |
| [**조성하**](https://github.com/cdth12345) | **Member** | • **Device & User** <br>• 사용자 프로필 관리 및 디바이스 연동 API 개발 |

<br>

## 2. 📚 사용 기술 (Tech Stack)

| 구분 | 기술 | 상세 내용 |
| :-: | :-: | - |
| **Language** | Java 17 |  |
| **Framework** | Spring Boot 3.5.9 | Spring Security, Spring Data JPA |
| **Database** | MySQL 8.0 | AWS RDS (Prod), H2 (Test) |
| **Infra** | AWS | EC2, S3 (Image Storage), RDS |
| **CI/CD** | GitHub Actions | Gradle Build -> SCP Transfer -> Deploy Script |
| **Utils** | ImageIO, Java AWT | 이미지 생성 및 픽셀 처리 |

<br>

## 3. ✨ 주요 기능 (Key Features)

### 🔐 1. 인증 및 보안 (Auth & Security)
- **JWT 기반 로그인:** Access/Refresh Token을 활용한 무상태(Stateless) 인증.
- **이메일 인증:** Google SMTP를 연동하여 비밀번호 찾기 시 새로운 비밀번호 자동 발송.

### 🖼️ 2. 템플릿 이미지 생성 (Template Engine)
- **동적 이미지 드로잉:** Java AWT/Graphics2D를 활용해 실시간으로 이미지를 그려냅니다.
- **커스텀 폰트 적용:** `Pretendard` 폰트를 서버 리소스에 내장하여 일관된 타이포그래피 제공.

### 🎨 3. 전자 잉크 이미지 처리 (E-ink Processing)
- **4-Color Quantization:** 일반 이미지를 E-ink 디스플레이가 표현 가능한 4색(Black, White, Red, Yellow)으로 최적화하여 변환합니다.
- **Binary Encoding:** 변환된 픽셀 데이터를 하드웨어(키링)로 전송하기 위해 경량화된 바이너리 포맷으로 인코딩합니다.

### ☁️ 4. 스토리지 및 데이터 관리
- **AWS S3 연동:** 생성된 템플릿 이미지와 사용자 업로드 이미지를 S3 버킷에 안전하게 저장합니다.
- **트랜잭션 관리:** 이미지 업로드 실패 시 S3 파일 삭제 등 롤백 처리 이벤트 핸들링 로직 구현.

<br>

## 4. 🏗️ 아키텍처 및 설계 (Architecture)

### 4-1. 시스템 아키텍처
<img width="80%" alt="최종 시스템 아키텍처" src="https://github.com/user-attachments/assets/4c868be5-8523-4106-b7e1-e899430b431d" />

### 4-2. ERD (Entity Relationship Diagram)
<img width="80%" alt="ERD" src="https://github.com/user-attachments/assets/c9bbada4-d5ea-4f07-b06b-35df1140d490" />

<br>

## 5. 📂 디렉토리 구조 (Directory Structure)
도메인형 디렉토리 구조(Domain-driven)를 채택하여 응집도를 높였습니다.

```bash
digitaltok-server/
├── .github/                # GitHub 관련 설정 (CI/CD 워크플로우, 이슈/PR 템플릿)
├── gradle/                 # Gradle Wrapper 관련 설정
├── src/
│   ├── main/
│   │   ├── java/com/digital_tok/
│   │   │   ├── global/     # 공통 설정 (Security, Exception, API 응답 규격 등)
│   │   │   ├── auth/       # 인증 및 인가 (Login, JWT 등)
│   │   │   │   ├── controller      # 컨트롤러
│   │   │   │   ├── convertor       # Entity, DTO 간의 변환
│   │   │   │   ├── domain          # 엔티티
│   │   │   │   ├── dto             # DTO 클래스
│   │   │   │   ├── repository      # 리포지토리
│   │   │   │   └── service         # 비즈니스 로직
│   │   │   ├── device/     # 디바이스 관리 도메인
│   │   │   ├── image/      # 이미지 처리 및 스토리지(S3) 관련
│   │   │   ├── template/   # 템플릿 관리 및 이미지 생성 로직
│   │   │   ├── user/       # 사용자 관리 도메인
│   │   │   └── DigitalTokApplication.java  # 애플리케이션 시작점
│   │   └── resources/      # 설정 파일(yml), 폰트 등의 리소스
│   └── test/               # 테스트 코드 (JUnit5 등)
├── build.gradle            # 프로젝트 의존성 및 빌드 설정
├── gradlew                 # Gradle 실행 스크립트
└── README.md               # 프로젝트 설명 문서
