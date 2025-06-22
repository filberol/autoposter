plugins {
    kotlin("jvm") version "2.1.10"
}

group = "ru.social.ai"

repositories {
    mavenCentral()
}

dependencies {
    // SQLite JDBC driver (file-based DB)
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")

    // Exposed (core + DAO + JDBC)
    implementation("org.jetbrains.exposed:exposed-core:0.45.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.45.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.45.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    // Telegram Api
    implementation("org.telegram:telegrambots-longpolling:9.0.0")
    implementation("org.telegram:telegrambots-client:9.0.0")

    // ChatBot Api
    implementation("io.github.sashirestela:simple-openai:3.21.0") {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // SLF4J API and Logback
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.5.13")

    // Tests
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
