package kauanjpk.bot.kotlinaru.commands

import kauanjpk.bot.kotlinaru.services.ModerationService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class ModerationCommands(private val service: ModerationService): ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when(event.name) {
            "ban" -> service.ban(event)
            "kick" -> service.kick(event)
            "mute" -> service.mute(event)
            "unmute" -> service.unmute(event)
        }
    }
}
