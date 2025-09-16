package kauanjpk.bot.kotlinaru.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import java.util.*
import net.dv8tion.jda.api.entities.Guild

class TrackScheduler(private val player: AudioPlayer, private val guild: Guild) {
    val queue: Queue<AudioTrack> = LinkedList()

    fun queue(track: AudioTrack) {
        if (!player.startTrack(track, true)) queue.offer(track)
    }

    fun nextTrack() {
        val next = queue.poll()
        if (!player.startTrack(next, false)) {
            guild.audioManager.closeAudioConnection()
        }
    }

    fun stop() {
        queue.clear()
        player.stopTrack()
        guild.audioManager.closeAudioConnection()
    }
}
