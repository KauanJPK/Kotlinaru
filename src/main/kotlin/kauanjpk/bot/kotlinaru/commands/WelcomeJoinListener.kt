package kauanjpk.bot.kotlinaru.commands

import kauanjpk.bot.kotlinaru.services.WelcomeService
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class WelcomeJoinListener(private val service: WelcomeService) : ListenerAdapter() {
    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        service.welcome(event.member)
    }
}
