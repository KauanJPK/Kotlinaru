package kauanjpk.bot.kotlinaru.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame
import net.dv8tion.jda.api.audio.AudioSendHandler
import java.nio.ByteBuffer
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

/**
 * Gerencia o player e a fila de músicas de um servidor (Guild)
 */
class GuildMusicManager(playerManager: AudioPlayerManager) {
    val player: AudioPlayer = playerManager.createPlayer()
    val queue: BlockingQueue<AudioTrack> = LinkedBlockingQueue()

    // AudioSendHandler para o JDA enviar o áudio
    val sendHandler = object : AudioSendHandler {
        private val buffer = ByteBuffer.allocate(1024)
        private val frame = MutableAudioFrame().apply { setBuffer(buffer) }

        override fun canProvide(): Boolean = player.provide(frame)

        override fun provide20MsAudio(): ByteBuffer? {
            buffer.flip()
            return buffer
        }

        override fun isOpus(): Boolean = true
    }

    // Adiciona uma música à fila ou toca imediatamente
    fun queue(track: AudioTrack) {
        if (!player.startTrack(track, true)) {
            queue.offer(track)
        }
    }

    // Passa para a próxima música
    fun nextTrack() {
        player.startTrack(queue.poll(), false)
    }

    // Para o player e limpa a fila
    fun stop() {
        player.stopTrack()
        queue.clear()
    }
}
