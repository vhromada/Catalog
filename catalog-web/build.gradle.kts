plugins {
    id("org.springframework.boot") version "2.3.3.RELEASE"
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

dependencies {
    val commonVersion = "6.0.0"
    implementation(platform("com.github.vhromada.project:project-parent:5.0.0"))
    implementation(project(":catalog-core"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.vhromada.common:common-account:$commonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity5")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("org.hibernate.validator:hibernate-validator")
    runtimeOnly("com.h2database:h2")
    testImplementation("com.github.vhromada.common:common-test:$commonVersion")
    testImplementation(kotlin("test-junit5"))
}

tasks.jar {
    manifest {
        attributes["Implementation-Title"] = "Web"
    }
}
