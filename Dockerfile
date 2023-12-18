# 멀티 스테이지 빌드 사용

# 애플리케이션 빌드 셋업 스테이지
FROM openjdk:17-jdk-slim as builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

RUN chmod +x ./gradlew

# "jar" 파일 생성
RUN ./gradlew bootJar

# 애플리케이션 실행 스테이지
FROM openjdk:17-jdk-slim

# 이전 "builder" 스테이지의 jar파일만 가져온다.
COPY --from=builder build/libs/*.jar app.jar

EXPOSE 80

ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE

ENTRYPOINT ["java", "-jar", "/app.jar"]