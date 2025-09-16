package kauanjpk.bot.kotlinaru

import kauanjpk.bot.kotlinaru.commands.*
import kauanjpk.bot.kotlinaru.services.*
import kauanjpk.bot.kotlinaru.audio.GuildMusicManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import io.github.cdimascio.dotenv.dotenv
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent

fun main() {
    val dotenv = dotenv()
    val token = dotenv["BOT_TOKEN"] ?: error("Token não encontrado no .env")

    val jda = JDABuilder.createDefault(token)
        .enableIntents(
            GatewayIntent.MESSAGE_CONTENT,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_MEMBERS
        )
        .setActivity(Activity.playing("🎶 Música e Moderação - Kotlinaru"))
        .build()

    val playerManager = DefaultAudioPlayerManager()
    AudioSourceManagers.registerRemoteSources(playerManager)
    AudioSourceManagers.registerLocalSource(playerManager)
    val musicManagers = mutableMapOf<Long, GuildMusicManager>()
    val voiceService = VoiceService(playerManager, musicManagers)

    val generalService = GeneralService()
    val moderationService = ModerationService()
    val welcomeService = WelcomeService()

    jda.addEventListener(GeneralCommands(generalService))
    jda.addEventListener(VoiceCommands(voiceService))
    jda.addEventListener(ModerationCommands(moderationService))
    jda.addEventListener(WelcomeCommands(welcomeService))
    jda.addEventListener(WelcomeJoinListener(welcomeService))

    jda.awaitReady()
    SetFunctions.register(jda)

    println("✅ Bot iniciado com sucesso!")
}
