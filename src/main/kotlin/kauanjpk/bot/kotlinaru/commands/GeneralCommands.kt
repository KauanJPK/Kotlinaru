package kauanjpk.bot.kotlinaru.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kauanjpk.bot.kotlinaru.services.GeneralService

class GeneralCommands  (
    private val service: GeneralService
) : ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "ping" -> service.ping(event)
            "help" -> service.help(event)
        }
    }
}
