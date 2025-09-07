package kauanjpk.bot.kotlinaru
import kauanjpk.bot.kotlinaru.commands.GeneralCommands
import kauanjpk.bot.kotlinaru.commands.SetFunctions
import kauanjpk.bot.kotlinaru.commands.VoiceCommands
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.requests.GatewayIntent
import kauanjpk.bot.kotlinaru.services.GeneralService
import io.github.cdimascio.dotenv.dotenv
import kauanjpk.bot.kotlinaru.audio.GuildMusicManager
import kauanjpk.bot.kotlinaru.commands.ModerationCommands
import kauanjpk.bot.kotlinaru.services.ModerationService
import kauanjpk.bot.kotlinaru.services.VoiceService

fun main() {
    // Carregar .env
    val dotenv = dotenv()
    val token = dotenv["BOT_TOKEN"] ?: error("Token não encontrado no .env")

    // Iniciar JDA
    val jda = JDABuilder.createDefault(token)
        .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MEMBERS)
        .setActivity(Activity.playing("🎶 Música no Discord - Made by KauanJPK"))
        .build()

    // Player manager (para música)
    val playerManager = DefaultAudioPlayerManager()
    AudioSourceManagers.registerRemoteSources(playerManager)
    AudioSourceManagers.registerLocalSource(playerManager)

    val musicManagers = mutableMapOf<Long, GuildMusicManager>()
    val voiceService = VoiceService(playerManager, musicManagers)
    val generalService = GeneralService()
    val moderationService = ModerationService()
    // Registrar listeners
    jda.addEventListener(GeneralCommands(generalService))
    jda.addEventListener(VoiceCommands(voiceService))
    jda.addEventListener(ModerationCommands(moderationService))


    // Registrar comandos de slash
    jda.awaitReady()
    SetFunctions.register(jda)

    println("✅ Bot iniciado com sucesso!")
}
