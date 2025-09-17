package kauanjpk.bot.kotlinaru.commands

import kauanjpk.bot.kotlinaru.services.WelcomeService
import kauanjpk.bot.kotlinaru.embeds.WelcomeEmbed
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class WelcomeJoinListener(private val service: WelcomeService) : ListenerAdapter() {

    private val embedBuilder = WelcomeEmbed()

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        val guild = event.guild
        val channelId = service.getWelcomeChannelId(guild.idLong) ?: return
        val channel = guild.getTextChannelById(channelId) ?: return

        val embedData = service.welcomeManager.getCustomEmbedData(guild.idLong)
        val embed = if (embedData?.title != null || embedData?.description != null) {
            embedBuilder.customEmbed(embedData?.title, embedData?.description, event.member)
        } else {
            embedBuilder.defaultEmbed(event.member)
        }

        channel.sendMessageEmbeds(embed).queue()
    }
}
