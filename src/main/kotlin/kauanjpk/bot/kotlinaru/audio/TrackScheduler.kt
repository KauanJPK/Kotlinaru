package kauanjpk.bot.kotlinaru.audio

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.entities.Guild
import java.util.*

open class TrackScheduler(val player: AudioPlayer, private val guild: Guild) : AudioEventAdapter() {
    val queue: Queue<AudioTrack> = LinkedList()

    fun queue(track: AudioTrack) {
        queue.offer(track)
        if (player.playingTrack == null) nextTrack()
    }

    fun nextTrack() {
        val next = queue.poll()
        if (next != null) {
            player.playTrack(next)
        }
        if (player.playingTrack == null) {
            guild.audioManager.closeAudioConnection()
        }
        else {
            if (player.playingTrack == null) {
                guild.audioManager.closeAudioConnection()
            }
        }
    }



    fun stop() {
        queue.clear()
        player.stopTrack()
        guild.audioManager.closeAudioConnection()
    }

    override fun onTrackEnd(player: AudioPlayer, track: AudioTrack, endReason: AudioTrackEndReason) {
        if (endReason.mayStartNext) nextTrack() else if (endReason==AudioTrackEndReason.FINISHED && queue.isEmpty()) {
            guild.audioManager.closeAudioConnection()
        }
    }
}
