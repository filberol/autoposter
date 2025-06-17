plugins {
    kotlin("jvm") version "2.1.10"
}

group = "ru.social.ai"

repositories {
    mavenCentral()
}

dependencies {
    // Telegram Api
    implementation("org.telegram:telegrambots-longpolling:9.0.0")
    implementation("org.telegram:telegrambots-client:9.0.0")

    // Logging
    implementation("org.slf4j:slf4j-simple:2.0.17")

    // Tests
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
