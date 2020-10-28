import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

plugins {
    val kotlinVersion = "1.3.72"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
}

val projectParentVersion by extra("5.0.1")
val commonVersion by extra("6.0.9")

allprojects {
    group = "com.github.vhromada.catalog"
    version = "10.2.0"

    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
        mavenLocal()
    }

    tasks.withType<Jar> {
        manifest {
            attributes["Built-By"] = "Vladimir Hromada"
            attributes["Build-Timestamp"] = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
            attributes["Implementation-Version"] = project.version
            attributes["Created-By"] = "Gradle ${gradle.gradleVersion}"
            attributes["Build-Jdk"] = "${System.getProperty("java.version")} (${System.getProperty("java.vendor")} ${System.getProperty("java.vm.version")})"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
}

tasks.jar {
    manifest {
        attributes["Implementation-Title"] = "Catalog"
    }
}
