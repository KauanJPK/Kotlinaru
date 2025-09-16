package kauanjpk.bot.kotlinaru.commands

import kauanjpk.bot.kotlinaru.services.VoiceService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.hooks.ListenerAdapter

class VoiceCommands(private val voiceService: VoiceService) : ListenerAdapter() {

    companion object {
        fun registerCommands(): List<SlashCommandData> = listOf(
            Commands.slash("play", "Toca uma música do YouTube").addOption(
                net.dv8tion.jda.api.interactions.commands.OptionType.STRING,
                "query",
                "Link ou nome da música",
                true
            ),
            Commands.slash("skip", "Pula para a próxima música"),
            Commands.slash("stop", "Para a música"),
            Commands.slash("queue", "Mostra a fila de músicas"),
            Commands.slash("pause", "Pausa ou retoma a música")
        )
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "play" -> {
                val query = event.getOption("query")?.asString ?: return
                voiceService.play(event, query)
            }
            "skip" -> voiceService.skip(event)
            "stop" -> voiceService.stop(event)
            "queue" -> voiceService.queue(event)
            "pause" -> voiceService.pause(event)
        }
    }
}
