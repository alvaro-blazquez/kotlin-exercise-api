plugins {
    alias(libs.plugins.kotlin.jvm)
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
    testImplementation(libs.kotest)

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
}

val functionalTest = task<Test>("functionalTest") {
    description = "Runs functional tests."
    group = "verification"

    testClassesDirs = sourceSets["functionalTest"].output.classesDirs
    classpath = sourceSets["functionalTest"].runtimeClasspath
}