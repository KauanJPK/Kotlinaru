package kauanjpk.bot.kotlinaru

import kauanjpk.bot.kotlinaru.services.VoiceService
import kauanjpk.bot.kotlinaru.commands.VoiceCommands
import kauanjpk.bot.kotlinaru.audio.GuildMusicManager
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import io.github.cdimascio.dotenv.dotenv
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.entities.Activity

fun main() {
    val dotenv = dotenv()
    val token = dotenv["BOT_TOKEN"] ?: error("Token não encontrado no .env")

    val jda = JDABuilder.createDefault(token)
        .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES)
        .setActivity(Activity.playing("🎶 Música"))
        .build()
        .awaitReady()

    val playerManager = DefaultAudioPlayerManager()
    AudioSourceManagers.registerRemoteSources(playerManager)
    AudioSourceManagers.registerLocalSource(playerManager)

    val musicManagers = mutableMapOf<Long, GuildMusicManager>()
    val voiceService = VoiceService(playerManager, musicManagers)

    jda.addEventListener(VoiceCommands(voiceService))

    println("✅ Bot iniciado com música funcionando via Lavaplayer!")
}
