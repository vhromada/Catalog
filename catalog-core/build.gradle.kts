plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

dependencies {
    val commonVersion: String by rootProject.extra
    implementation(platform("com.github.vhromada.project:project-parent:5.0.0"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    api("com.github.vhromada.common:common-core:$commonVersion")
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("io.github.microutils:kotlin-logging")
    api("org.flywaydb:flyway-core")
    implementation("com.github.vhromada.common:common-account:$commonVersion")
    testImplementation("com.github.vhromada.common:common-test:$commonVersion")
    testImplementation("com.h2database:h2")
}

tasks.jar {
    manifest {
        attributes["Implementation-Title"] = "Core"
    }
}
