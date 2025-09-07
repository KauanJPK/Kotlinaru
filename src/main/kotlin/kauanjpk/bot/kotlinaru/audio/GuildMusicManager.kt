package kauanjpk.bot.kotlinaru.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler
import net.dv8tion.jda.api.entities.Guild
import java.nio.ByteBuffer

open class GuildMusicManager(playerManager: AudioPlayerManager, val guild: Guild) {
   val player: AudioPlayer = playerManager.createPlayer()
    val scheduler: TrackScheduler = TrackScheduler(player, guild)

    init {
        player.addListener(scheduler)
    }

    private var lastFrame: AudioFrame? = null

    fun sendHandler(): AudioSendHandler = object : AudioSendHandler {
        override fun canProvide(): Boolean {
            lastFrame = player.provide()
            return lastFrame != null
        }

        override fun provide20MsAudio(): ByteBuffer {
            return ByteBuffer.wrap(lastFrame!!.data)
        }

        override fun isOpus(): Boolean = true
    }
}
