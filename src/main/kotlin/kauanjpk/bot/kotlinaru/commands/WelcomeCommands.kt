package kauanjpk.bot.kotlinaru.commands

import kauanjpk.bot.kotlinaru.services.WelcomeService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class WelcomeCommands(private val service: WelcomeService) : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name) {
            "setwelcomechannel" -> service.setChannel(event)
            "setcustomembed" -> service.setCustomEmbed(event)
            "resetcustomembed" -> service.resetCustomEmbed(event)
            "testwelcome" -> service.testWelcome(event)
        }
    }
}
