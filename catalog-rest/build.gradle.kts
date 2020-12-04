plugins {
    id("org.springframework.boot") version "2.4.0"
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
    implementation("com.github.vhromada.common:common-web:$commonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.springfox:springfox-boot-starter")
    runtimeOnly("com.h2database:h2")
    testImplementation("com.github.vhromada.common:common-test:$commonVersion")
    testImplementation(kotlin("test-junit5"))
}

tasks {
    bootJar {
        manifest {
            attributes["Implementation-Title"] = "Rest"
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
    include("catalog-rest*.jar")
    into(file("$buildDir/app"))
    rename { "Catalog.jar" }
}
