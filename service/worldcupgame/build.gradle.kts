plugins {
    java
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "com.masikga.itwc"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    // default
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // mybatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.2")

    // validation
    implementation("org.springframework.boot:spring-boot-starter-validation")


    // ???
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // monitor
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus:1.11.5")
    implementation("ca.pjer:logback-awslogs-appender:1.4.0")

    // cache
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("com.github.ben-manes.caffeine:caffeine")

    // api docs
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // database
    runtimeOnly("com.h2database:h2:1.4.200")

    // local redis
    testImplementation("it.ozimov:embedded-redis:0.7.3")

    // test container
    testImplementation("org.testcontainers:testcontainers:1.16.0") // TC 의존성
    testImplementation("org.testcontainers:junit-jupiter:1.16.2")  // TC 의존성
    testImplementation("org.testcontainers:jdbc:1.16.0")          // DB와의 JDBC connection

    // logack json
    implementation("ch.qos.logback.contrib:logback-jackson:0.1.5")
    implementation("ch.qos.logback.contrib:logback-json-classic:0.1.5")

    implementation("com.google.guava:guava:32.1.2-jre")

    implementation("mysql:mysql-connector-java:8.0.33")

    implementation("org.springframework.security:spring-security-crypto")

    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.5")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}