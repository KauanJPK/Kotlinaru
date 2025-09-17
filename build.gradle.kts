plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    application
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("net.dv8tion:JDA:5.0.0-beta.15")
    implementation("com.mysql:mysql-connector-j:9.4.0")
    implementation("dev.arbjerg:lavaplayer:2.2.2") // Lavaplayer atualizado
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
}

application {
    mainClass.set("kauanjpk.bot.kotlinaru.MainKt")
}

kotlin {
    jvmToolchain(17)
}
tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "kauanjpk.bot.kotlinaru.MainKt"
        )
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}
