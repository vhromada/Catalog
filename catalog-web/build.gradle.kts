plugins {
    id("org.springframework.boot") version "2.4.4"
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

dependencies {
    val projectParentVersion: String by rootProject.extra
    val commonVersion: String by rootProject.extra
    implementation(platform("com.github.vhromada.project:project-parent:$projectParentVersion"))
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
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation(kotlin("test-junit5"))
}

tasks {
    bootJar {
        manifest {
            attributes["Implementation-Title"] = "Web"
        }
    }
}

tasks {
    build {
        finalizedBy("copyJar")
    }
}

tasks.register<Copy>("copyJar") {
    from(file("$buildDir/libs"))
    include("catalog-web*.jar")
    into(file("$buildDir/app"))
    rename { "Catalog.jar" }
}
