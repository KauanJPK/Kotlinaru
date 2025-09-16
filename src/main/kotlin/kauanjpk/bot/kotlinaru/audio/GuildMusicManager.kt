package kauanjpk.bot.kotlinaru.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.audio.AudioSendHandler
import java.nio.ByteBuffer

class GuildMusicManager(playerManager: AudioPlayerManager, val guild: Guild) {
    val player: AudioPlayer = playerManager.createPlayer()
    val scheduler: TrackScheduler = TrackScheduler(player, guild)

    init {
        player.addListener(scheduler)
    }

    fun sendHandler(): AudioSendHandler = AudioPlayerSendHandler(player)
}

class AudioPlayerSendHandler(private val player: AudioPlayer) : AudioSendHandler {
    private val buffer: ByteBuffer = ByteBuffer.allocate(1024)

    override fun canProvide(): Boolean {
        val frame = player.provide() ?: return false
        buffer.clear()
        buffer.put(frame.data)
        buffer.flip()
        return true
    }

    override fun provide20MsAudio(): ByteBuffer = buffer

    override fun isOpus(): Boolean = true
}
