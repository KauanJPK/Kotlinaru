package kauanjpk.bot.kotlinaru.commands

import services.VoiceService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

/**
 * Comandos de voz: /play, /skip, /stop
 */
class VoiceCommands(private val voiceService: VoiceService) : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "play" -> {
                val query = event.getOption("musica")?.asString
                if (query.isNullOrBlank()) {
                    event.reply("❌ Informe o nome ou link da música!").queue()
                    return
                }
                voiceService.play(event, query)
            }

            "skip" -> voiceService.skip(event)
            "stop" -> voiceService.stop(event)

        }
    }
}
