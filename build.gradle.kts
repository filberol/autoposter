plugins {
    kotlin("jvm") version "2.1.10"
}

group = "ru.social.ai"
val architecture = "macos_amd64"

repositories {
    mavenCentral()
    maven { url = uri("https://mvn.mchv.eu/repository/mchv/") }
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

    // Align all TDLight artifacts
    implementation(platform("it.tdlight:tdlight-java-bom:3.4.0+td.1.8.26"))
    implementation("it.tdlight:tdlight-java")
    // Replace classifier with your OS: e.g. macos_x86_64, linux_amd64_gnu_ssl1, windows_x86_64
    implementation("it.tdlight:tdlight-natives") {
        artifact {
            classifier = architecture
        }
    }

    // ZXing for QR-code generation
    implementation("com.google.zxing:core:3.5.0")
    implementation("com.google.zxing:javase:3.5.0")

    // ChatBot Api
    implementation("io.github.sashirestela:simple-openai:3.21.0") {
        exclude(group = "org.slf4j", module = "slf4j-simple")
    }

    // OkHttpClient
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // SLF4J API and Logback
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.5.13")

    // Reflection
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")
    implementation("org.reflections:reflections:0.10.2")

    // Tests
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
