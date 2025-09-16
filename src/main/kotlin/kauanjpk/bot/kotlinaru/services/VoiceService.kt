package kauanjpk.bot.kotlinaru.services

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import kauanjpk.bot.kotlinaru.audio.GuildMusicManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

class VoiceService(
    private val playerManager: AudioPlayerManager,
    private val musicManagers: MutableMap<Long, GuildMusicManager>
) {
    private fun getMusicManager(guild: net.dv8tion.jda.api.entities.Guild) =
        musicManagers.computeIfAbsent(guild.idLong) { GuildMusicManager(playerManager, guild) }

    fun play(event: SlashCommandInteractionEvent, query: String) {
        val guild = event.guild ?: return
        val musicManager = getMusicManager(guild)
        val memberChannel = event.member?.voiceState?.channel ?: run {
            event.reply("❌ Você precisa estar em um canal de voz!").setEphemeral(true).queue()
            return
        }

        guild.audioManager.openAudioConnection(memberChannel)
        guild.audioManager.sendingHandler = musicManager.sendHandler()

        val searchQuery = if (query.startsWith("http")) query else "scsearch:$query"

        event.deferReply().queue()

        playerManager.loadItemOrdered(musicManager, searchQuery, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                musicManager.scheduler.queue(track)
                event.hook.editOriginal("🎶 Tocando agora: **${track.info.title}**").queue()
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                val tracks = if (playlist.isSearchResult) listOf(playlist.tracks.first()) else playlist.tracks
                tracks.forEach { musicManager.scheduler.queue(it) }
                val firstTitle = tracks.firstOrNull()?.info?.title ?: "Desconhecida"
                event.hook.editOriginal("🎶 Adicionado ${tracks.size} músicas! Começando com: **$firstTitle**").queue()
            }

            override fun noMatches() {
                event.hook.editOriginal("❌ Nenhum resultado encontrado.").queue()
            }

            override fun loadFailed(exception: FriendlyException) {
                exception.printStackTrace()
                event.hook.editOriginal("⚠️ Erro ao carregar a música. Tente usar um link direto.\n> ${exception.message}").queue()
                guild.audioManager.closeAudioConnection()
            }
        })
    }


    fun skip(event: SlashCommandInteractionEvent) {
        val musicManager = musicManagers[event.guild!!.idLong] ?: return
        musicManager.scheduler.nextTrack()
        event.reply("⏭️ Pulou para a próxima música!").queue()
    }

    fun stop(event: SlashCommandInteractionEvent) {
        val musicManager = musicManagers[event.guild!!.idLong] ?: return
        musicManager.scheduler.stop()
        event.reply("⏹️ Música parada!").queue()
    }

    fun pause(event: SlashCommandInteractionEvent) {
        val player = musicManagers[event.guild!!.idLong]?.player ?: return
        player.isPaused = !player.isPaused
        event.reply(if (player.isPaused) "⏸️ Pausado!" else "▶️ Retomado!").queue()
    }

    fun queue(event: SlashCommandInteractionEvent) {
        val musicManager = musicManagers[event.guild!!.idLong] ?: return
        val scheduler = musicManager.scheduler
        val playingNow = musicManager.player.playingTrack?.info?.title ?: "Nenhuma música tocando"

        if (scheduler.queue.isEmpty()) {
            event.reply("📭 A fila está vazia. Tocando agora: $playingNow").queue()
        } else {
            val list = scheduler.queue.joinToString("\n") { it.info.title }
            event.reply("🎶 Tocando agora: $playingNow\nFila:\n$list").queue()
        }
    }
}
