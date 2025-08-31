

plugins {
    kotlin("jvm") version "2.2.0"
    application
    id("com.gradleup.shadow") version "8.3.1"
}

application {
    mainClass.set("kauanjpk.bot.kotlinaru.MainKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:6.0.0-rc.3") // ou versão que você usa
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1") // dotenv
}

tasks.processResources {
    // Garante que os arquivos em src/main/resources sejam copiados
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}