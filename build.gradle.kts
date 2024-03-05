plugins {
    java
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
}

repositories {
    mavenCentral()
}

subprojects {

    repositories {
        mavenCentral()
    }

    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    tasks.withType<Test> {
        useJUnitPlatform()
    }

}