package kauanjpk.bot.kotlinaru.services

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import kauanjpk.bot.kotlinaru.audio.GuildMusicManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

open class VoiceService(
    private val playerManager: AudioPlayerManager,
    private val musicManagers: MutableMap<Long, GuildMusicManager>
) {

    private fun getMusicManager(guildId: Long) =
        musicManagers.computeIfAbsent(guildId) { GuildMusicManager(playerManager) }

    fun play(event: SlashCommandInteractionEvent, query: String) {
        val guild = event.guild!!
        val guildId = guild.idLong
        val musicManager = getMusicManager(guildId)

        val memberChannel = event.member?.voiceState?.channel
        if (memberChannel == null) {
            event.reply("❌ Você precisa estar em um canal de voz!").queue()
            return
        }

        val audioManager = guild.audioManager
        if (!audioManager.isConnected) {
            audioManager.openAudioConnection(memberChannel)
            audioManager.sendingHandler = musicManager.sendHandler()
        }


        val searchQuery = if (query.startsWith("http")) query else "scsearch:$query"

        playerManager.loadItemOrdered(musicManager, searchQuery, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                musicManager.scheduler.queue(track)
                event.reply("🎶 Tocando: ${track.info.title}").queue()
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                // Aqui é onde você coloca o código da playlist
                val tracks = if (playlist.isSearchResult) {
                    listOf(playlist.tracks.first()) // pesquisa retorna só o primeiro resultado
                } else {
                    playlist.tracks // playlist normal: adiciona todas
                }

                tracks.forEach { musicManager.scheduler.queue(it) }

                val firstTitle = tracks.firstOrNull()?.info?.title ?: "Desconhecida"
                event.reply("🎶 Adicionado: ${tracks.size} músicas da playlist! Começando com: $firstTitle").queue()
            }

            override fun noMatches() {
                event.reply("❌ Nenhum resultado encontrado.").queue()
            }

            override fun loadFailed(exception: FriendlyException) {
                event.reply("⚠️ Erro ao carregar: ${exception.message}").queue()
            }
        })
    }


        fun skip(event: SlashCommandInteractionEvent) {
        val guildId = event.guild!!.idLong
        musicManagers[guildId]?.scheduler?.nextTrack()
        event.reply("⏭️ Pulou a música!").queue()
    }

    fun stop(event: SlashCommandInteractionEvent) {
        val guildId = event.guild!!.idLong
        musicManagers[guildId]?.scheduler?.stop()
        event.reply("⏹️ Música parada!").queue()
    }

    fun queue(event: SlashCommandInteractionEvent) {
        val guildId = event.guild!!.idLong
        val queue = musicManagers[guildId]?.scheduler?.queue

        if (queue.isNullOrEmpty()) {
            event.reply("📭 A fila está vazia.").queue()
        } else {
            val list = queue.joinToString("\n") { it.info.title }
            event.reply("🎶 Fila atual:\n$list").queue()
        }
    }

    fun pause(event: SlashCommandInteractionEvent) {
        val guildId = event.guild!!.idLong
        val player = musicManagers[guildId]?.player
        if (player != null) {
            player.isPaused = !player.isPaused
            event.reply(if (player.isPaused) "⏸️ Pausado!" else "▶️ Retomado!").queue()
        } else {
            event.reply("⚠️ Nenhuma música tocando.").queue()
        }
    }
}
