plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

repositories {
    mavenCentral()
}

sourceSets {
    create("functionalTest") {
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.main.get().output
    }
}

val functionalTestImplementation: Configuration by configurations.getting {
}

dependencies {
    testImplementation(libs.kotest)
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


val functionalTest = task<Test>("functionalTest") {
    description = "Runs functional tests."
    group = "verification"

    testClassesDirs = sourceSets["functionalTest"].output.classesDirs
    classpath = sourceSets["functionalTest"].runtimeClasspath
}