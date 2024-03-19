# 멀티 스테이지 빌드 사용

# 애플리케이션 빌드 셋업 스테이지
FROM openjdk:17-jdk-slim as builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .

COPY ./core/db/src ./core/db/src
COPY ./core/db/build.gradle.kts ./core/db/build.gradle.kts

COPY ./core/jwt/src ./core/jwt/src
COPY ./core/jwt/build.gradle.kts ./core/jwt/build.gradle.kts

COPY ./core/caffeine/src ./core/caffeine/src
COPY ./core/caffeine/build.gradle.kts ./core/caffeine/build.gradle.kts

COPY ./core/redis/src ./core/redis/src
COPY ./core/redis/build.gradle.kts ./core/redis/build.gradle.kts

COPY ./core/feign/src ./core/feign/src
COPY ./core/feign/build.gradle.kts ./core/feign/build.gradle.kts

COPY ./core/error/src ./core/error/src
COPY ./core/error/build.gradle.kts ./core/error/build.gradle.kts

COPY ./service/member/src ./service/member/src
COPY ./service/member/build.gradle.kts ./service/member/build.gradle.kts

COPY ./domain/model/src ./domain/model/src
COPY ./domain/model/build.gradle.kts ./domain/model/build.gradle.kts

COPY ./service/member/src ./service/member/src
COPY ./service/member/build.gradle.kts ./service/member/build.gradle.kts

RUN chmod +x ./gradlew

# "jar" 파일 생성
RUN ./gradlew :service:member:bootJar

# 애플리케이션 실행 스테이지
FROM openjdk:17-jdk-slim

# 이전 "builder" 스테이지의 jar파일만 가져온다.
COPY --from=builder ./service/member/build/libs/*.jar app.jar

EXPOSE 8081

ARG SPRING_PROFILES_ACTIVE
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILES_ACTIVE

ENTRYPOINT ["java", "-jar", "/app.jar"]