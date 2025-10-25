plugins {
    id("org.springframework.boot") version "3.5.7"
    id("io.spring.dependency-management") version "1.1.7"
    java
}

group = "dev.louisa"
version = "0.6.1-SNAPSHOT"
description = "jam-hub-service"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/vlouisa/mail-pit")
        credentials {
            val user = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
            val key = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            if (user != null && key != null) {
                username = user
                password = key
            }
        }
    }
}


extra["jakartaMailApiVersion"] = "2.1.3"
extra["javaJwtVersion"] = "4.5.0"
extra["datafakerVersion"] = "2.5.2"
extra["flywayCoreVersion"] = "10.15.2"
extra["flywayPostgresVersion"] = "10.14.0"
extra["mockRestVersion"] = "0.5.3"
extra["mailPitVersion"] = "0.2.1"
extra["pitestVersion"] = "1.20.3"
extra["pitestJunit5Version"] = "1.2.3"
extra["resilience4jVersion"] = "2.3.0"
extra["testcontainersVersion"] = "2.0.1"
extra["wiremockVersion"] = "3.5.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("com.auth0:java-jwt:${property("javaJwtVersion")}")
    implementation("io.github.resilience4j:resilience4j-spring-boot3:${property("resilience4jVersion")}")
    implementation("io.github.resilience4j:resilience4j-micrometer:${property("resilience4jVersion")}")
    implementation("org.flywaydb:flyway-core:${property("flywayCoreVersion")}")
    implementation("org.flywaydb:flyway-database-postgresql:${property("flywayPostgresVersion")}")

    runtimeOnly("org.postgresql:postgresql")

    compileOnly("jakarta.mail:jakarta.mail-api:${property("jakartaMailApiVersion")}")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Spring Boot Actuator
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.wiremock:wiremock-standalone:${property("wiremockVersion")}")
    testImplementation("net.datafaker:datafaker:${property("datafakerVersion")}")
    testImplementation("org.testcontainers:testcontainers:${property("testcontainersVersion")}")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:jdbc")
    testImplementation("dev.louisa.victor:mock-rest:${property("mockRestVersion")}")
    testImplementation("dev.louisa.victor:mail-pit:${property("mailPitVersion")}")

    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}


tasks.register<JavaExec>("pitest") {
    group = "verification"
    description = "Run PIT mutation testing"
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("org.pitest.mutationtest.commandline.MutationCoverageReport")
    args = listOf(
        "--reportDir", "${layout.buildDirectory.get().asFile}/reports/pitest",
        "--targetClasses", "dev.louisa.jam.hub.*",
        "--targetTests", "dev.louisa.jam.hub.*",
        "--threads", "8",
        "--timeoutConst", "10000",
        "--timeoutFactor", "2.0"
    )
}
