plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io") // necessário para LavaPlayer fork
}

dependencies {
    implementation(kotlin("stdlib"))

    // JDA 5
    implementation("net.dv8tion:JDA:5.0.0-beta.15") {
        exclude(module = "opus-java") // opcional se você não usar voz nativa
    }

    // LavaPlayer fork (sem dependência comum)
    implementation("dev.arbjerg:lavaplayer:2.2.2")

    // Dotenv para carregar variáveis de ambiente
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
}


application {
    mainClass.set("kauanjpk.bot.kotlinaru.MainKt")
}


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17)) // define JVM 17 para Kotlin e Java
    }
}

kotlin {
    jvmToolchain(17)
}
