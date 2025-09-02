package kauanjpk.bot.kotlinaru.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import net.dv8tion.jda.api.audio.AudioSendHandler
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame
import java.nio.ByteBuffer

class GuildMusicManager(manager: AudioPlayerManager) {
    val player: AudioPlayer = manager.createPlayer()
    val scheduler: TrackScheduler = TrackScheduler(player)

    init {
        player.addListener(scheduler)
    }

    fun sendHandler(): AudioSendHandler {
        return object : AudioSendHandler {
            private val buffer = ByteBuffer.allocate(1024)
            private val frame = MutableAudioFrame().apply { setBuffer(buffer) }

            override fun canProvide(): Boolean {
                return player.provide(frame)
            }

            override fun provide20MsAudio(): ByteBuffer? {
                buffer.flip()
                return buffer
            }

            override fun isOpus(): Boolean = true
        }
    }
}
