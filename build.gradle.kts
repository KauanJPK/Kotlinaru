plugins {
    kotlin("jvm") version "1.9.0"
    application
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("dev.arbjerg:lavaplayer:2.2.2")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("net.dv8tion:JDA:5.0.0-beta.15")
}

application {
    mainClass.set("kauanjpk.bot.kotlinaru.MainKt")
}
kotlin {
    jvmToolchain(17) // usa Java 20
}
