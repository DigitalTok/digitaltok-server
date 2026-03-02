# 1. Java 17 환경을 베이스로 사용 (가볍고 많이 쓰는 alpine 버전)
FROM eclipse-temurin:17-jdk-alpine

# 2. 작업할 디렉토리 설정
WORKDIR /app

# 3. 로컬에서 빌드된 jar 파일을 도커 컨테이너 안의 /app.jar로 복사
COPY build/libs/digitalTok.jar app.jar

# 4. 서버가 사용할 8080 포트 개방
EXPOSE 8080

# 5. 도커 컨테이너가 켜질 때 실행할 명령어 (java -jar app.jar)
ENTRYPOINT ["java", "-jar", "app.jar"]