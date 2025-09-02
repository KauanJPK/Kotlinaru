package services

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import kauanjpk.bot.kotlinaru.audio.GuildMusicManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

/**
 * Serviço de comandos de voz: play, skip, stop
 */
class VoiceService(
    private val playerManager: AudioPlayerManager,
    private val musicManagers: MutableMap<Long, GuildMusicManager>
) {

    fun play(event: SlashCommandInteractionEvent, query: String) {
        val memberChannel = event.member?.voiceState?.channel ?: run {
            event.reply("Entre em um canal de voz primeiro!").queue()
            return
        }

        val guild = event.guild!!
        val guildId = guild.idLong

        val musicManager = musicManagers.computeIfAbsent(guildId) {
            GuildMusicManager(playerManager)
        }

        // Conectar ao canal e setar AudioSendHandler
        val audioManager = guild.audioManager
        if (!audioManager.isConnected) {
            audioManager.openAudioConnection(memberChannel)
        }
        audioManager.sendingHandler = musicManager.sendHandler

        val trackUrl = if (query.startsWith("http")) query else "scsearch:$query"

        // Carregar a música usando Lavaplayer
        playerManager.loadItemOrdered(musicManager, trackUrl, object : com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler {
            override fun trackLoaded(track: com.sedmelluq.discord.lavaplayer.track.AudioTrack) {
                musicManager.queue(track)
                event.reply("🎶 Tocando: ${track.info.title}").queue()
            }

            override fun playlistLoaded(playlist: com.sedmelluq.discord.lavaplayer.track.AudioPlaylist) {
                // Adiciona apenas a primeira música da playlist
                playlist.tracks.firstOrNull()?.let {
                    musicManager.queue(it)
                    event.reply("🎶 Tocando: ${it.info.title}").queue()
                } ?: event.reply("❌ Playlist vazia.").queue()
            }

            override fun noMatches() { event.reply("❌ Nenhuma música encontrada.").queue() }

            override fun loadFailed(exception: com.sedmelluq.discord.lavaplayer.tools.FriendlyException) {
                event.reply("❌ Erro ao carregar música: ${exception.message}").queue()
            }
        })
    }

    fun skip(event: SlashCommandInteractionEvent) {
        musicManagers[event.guild!!.idLong]?.nextTrack()
        event.reply("⏭️ Pulou a música!").queue()
    }

    fun stop(event: SlashCommandInteractionEvent) {
        musicManagers[event.guild!!.idLong]?.stop()
        event.reply("⏹️ Música parada!").queue()
    }
}
