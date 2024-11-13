plugins {
    alias(libs.plugins.kotlin.jvm)
    kotlin("plugin.serialization") version "2.0.20"
    id("io.ktor.plugin") version "3.0.1"
    application
    idea
}

repositories {
    mavenCentral()
}

sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
    create("functionalTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

idea {
    module {
        testSources.from(sourceSets["integrationTest"].kotlin.srcDirs)
        testSources.from(sourceSets["functionalTest"].kotlin.srcDirs)
    }
}

val integrationTestImplementation: Configuration by configurations.getting {
    extendsFrom(configurations.implementation.get())
}

val functionalTestImplementation: Configuration by configurations.getting {
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.1")

    testImplementation(libs.kotest)
    testImplementation(libs.mockk)

    integrationTestImplementation(libs.mockk)
    integrationTestImplementation(libs.kotest)
    integrationTestImplementation(libs.ktor.server.test)
    integrationTestImplementation(libs.kotest.assertions.ktor)
    integrationTestImplementation(libs.kotest.assertions.json)

    functionalTestImplementation(libs.kotest)
    functionalTestImplementation(libs.kotest.assertions.ktor)
    functionalTestImplementation(libs.kotest.assertions.json)
    functionalTestImplementation(libs.ktor.client.core)
    functionalTestImplementation(libs.ktor.client.okhttp)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "org.example.AppKt"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

val integrationTest = task<Test>("integrationTest") {
    description = "Runs integration tests."
    group = "verification"

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath
    shouldRunAfter(tasks.test)
}

tasks.check {
    dependsOn(integrationTest)
}

val functionalTest = task<Test>("functionalTest") {
    description = "Runs functional tests."
    group = "verification"

    testClassesDirs = sourceSets["functionalTest"].output.classesDirs
    classpath = sourceSets["functionalTest"].runtimeClasspath
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("myktorapp")
    }
}
