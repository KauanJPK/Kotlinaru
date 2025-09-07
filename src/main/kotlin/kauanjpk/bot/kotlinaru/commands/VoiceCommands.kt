package kauanjpk.bot.kotlinaru.commands

import kauanjpk.bot.kotlinaru.services.VoiceService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands


class VoiceCommands(private val voiceService: VoiceService): ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "play" -> {
                val query = event.getOption("query")?.asString
                if (query != null && query.isNotBlank()) {
                    voiceService.play(event, query)
                } else {
                    event.reply("❌ Você precisa informar um nome ou link da música.").queue()
                }
            }

            "skip" -> {
                voiceService.skip(event)
            }

            "stop" -> {
                voiceService.stop(event)
            }

            "queue" -> {
                voiceService.queue(event)
            }
            "pause" -> {
                voiceService.pause(event)
            }
        }
    }


}

