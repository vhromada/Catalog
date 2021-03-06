import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

plugins {
    val kotlinVersion = "1.4.32"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion
}

val projectParentVersion by extra(initialValue = "5.0.5")
val commonVersion by extra(initialValue = "6.1.1")

allprojects {
    group = "com.github.vhromada.catalog"
    version = "11.0.2"

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
