package kauanjpk.bot.kotlinaru.commands

import kauanjpk.bot.kotlinaru.services.WelcomeService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class WelcomeCommands(private val service: WelcomeService) : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "set-welcome-channel" -> service.setChannel(event)
            "set-welcome-embed" -> service.setCustomEmbed(event)
        }
    }
}
