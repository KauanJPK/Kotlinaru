package kauanjpk.bot.kotlinaru.services

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import kauanjpk.bot.kotlinaru.audio.GuildMusicManager
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

open class VoiceService(
    private val playerManager: AudioPlayerManager,
    private val musicManagers: MutableMap<Long, GuildMusicManager>
) {

    private fun getMusicManager(guild: net.dv8tion.jda.api.entities.Guild) =
        musicManagers.computeIfAbsent(guild.idLong) { GuildMusicManager(playerManager, guild) }

    fun play(event: SlashCommandInteractionEvent, query: String) {
        val guild = event.guild ?: return
        val musicManager = getMusicManager(guild)

        val memberChannel = event.member?.voiceState?.channel
        if (memberChannel == null) {
            event.reply("❌ Você precisa estar em um canal de voz!").queue()
            return
        }

        val audioManager = guild.audioManager
        if (!audioManager.isConnected) {
            audioManager.openAudioConnection(memberChannel)
            audioManager.sendingHandler = musicManager.sendHandler()
        }else{
            audioManager.closeAudioConnection()
        }

        event.deferReply().queue { hook ->
            val searchQuery = if (query.startsWith("http")) query else "scsearch:$query"

            playerManager.loadItemOrdered(musicManager, searchQuery, object : AudioLoadResultHandler {
                override fun trackLoaded(track: AudioTrack) {
                    musicManager.scheduler.queue(track)
                    hook.sendMessage("🎶 Tocando: ${track.info.title}").queue()
                }

                override fun playlistLoaded(playlist: AudioPlaylist) {
                    val tracks = if (playlist.isSearchResult) listOf(playlist.tracks.first()) else playlist.tracks
                    tracks.forEach { musicManager.scheduler.queue(it) }
                    val firstTitle = tracks.firstOrNull()?.info?.title ?: "Desconhecida"
                    hook.sendMessage("🎶 Adicionado ${tracks.count()} músicas! Começando com: $firstTitle").queue()
                }

                override fun noMatches() {
                    hook.sendMessage("❌ Nenhum resultado encontrado.").queue()
                }

                override fun loadFailed(exception: FriendlyException) {
                    exception.printStackTrace()

                    val musicManager = getMusicManager(event.guild!!)
                    if (musicManager.player.playingTrack != null) {
                        return
                    }

                    hook.sendMessage("⚠️ Erro ao carregar: ${exception.message}").queue()
                    audioManager.closeAudioConnection()
                }

                })
            }
        }


    fun skip(event: SlashCommandInteractionEvent) {
        val guildId = event.guild!!.idLong
        val scheduler = musicManagers[guildId]?.scheduler
        val audioManager = event.guild!!.audioManager

        if (scheduler == null) {
            event.reply("⚠️ Nenhum player ativo.").queue()
            return
        }
        if (scheduler.queue.isEmpty()) {
            event.reply("⏹️ Fila vazia. Reprodução encerrada. Saindo do canal!").queue()
            audioManager.closeAudioConnection()
        } else {
            scheduler.nextTrack()
            event.reply("⏭️ Pulou para a próxima música!").queue()
        }
    }

    fun stop(event: SlashCommandInteractionEvent) {
        val guildId = event.guild!!.idLong
        val scheduler = musicManagers[guildId]?.scheduler
        musicManagers[guildId]?.scheduler?.stop()
        if(scheduler == null) {
            event.reply("⚠️ Nenhum player ativo.").queue()
            return
        }
        event.reply("⏹️ Música parada!").queue()
    }

    fun queue(event: SlashCommandInteractionEvent) {
        val guildId = event.guild!!.idLong
        val queue = musicManagers[guildId]?.scheduler?.queue

        if (queue.isNullOrEmpty()) event.reply("📭 A fila está vazia.").queue()
        else {
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
        } else event.reply("⚠️ Nenhuma música tocando.").queue()
    }
}
