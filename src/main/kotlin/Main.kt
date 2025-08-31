package kauanjpk.bot.kotlinaru

import io.github.cdimascio.dotenv.dotenv
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.JDA
import java.util.*




fun main() {
    val dotenv = dotenv()
    val token = dotenv["BOT_TOKEN"] ?: System.getenv("BOT_TOKEN")

    val jda = JDABuilder.createLight(
        token,
        EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)
    )
        .build()
    jda.presence.setActivity(Activity.playing("com ${jda.users.size} usuários!"))
    // Criar uma única instância do comando
    val commandSet = SlashBotCommandsSet()

    jda.addEventListener(commandSet)


    // Espera o bot estar pronto antes de registrar os comandos
    jda.awaitReady()
    commandSet.settingCommands(jda)

    println("Bot is ready and commands are registered!")
}
