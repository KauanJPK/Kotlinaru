package kauanjpk.bot.kotlinaru.services

import kauanjpk.bot.kotlinaru.config.WelcomeManager
import kauanjpk.bot.kotlinaru.embeds.WelcomeEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

class WelcomeService(val welcomeManager: WelcomeManager) {

    private val embedBuilder = WelcomeEmbed()

    fun getWelcomeChannelId(guildId: Long): Long? = welcomeManager.getWelcomeChannelId(guildId)

    fun setChannel(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return
        val channel = event.getOption("channel")?.asChannel as? TextChannel ?: return
        welcomeManager.updateWelcomeChannel(guild.idLong, channel.idLong)
        event.reply("✅ Canal de boas-vindas definido!").queue()
    }

    fun setCustomEmbed(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return
        val title = event.getOption("title")?.asString
        val description = event.getOption("description")?.asString
        welcomeManager.updateCustomEmbed(guild.idLong, title, description)
        event.reply("✅ Embed customizado definido!").queue()
    }

    fun resetCustomEmbed(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return
        welcomeManager.updateCustomEmbed(guild.idLong, null, null)
        event.reply("✅ Embed customizado resetado! Agora o padrão será usado.").queue()
    }

    fun testWelcome(event: SlashCommandInteractionEvent) {
        val guild = event.guild ?: return
        val channelId = getWelcomeChannelId(guild.idLong) ?: run {
            event.reply("⚠️ Nenhum canal de boas-vindas configurado.").setEphemeral(true).queue()
            return
        }
        val channel = guild.getTextChannelById(channelId) ?: return
        val member: Member = event.member ?: return

        val embedData = welcomeManager.getCustomEmbedData(guild.idLong)
        val embed = if (embedData?.title != null || embedData?.description != null) {
            embedBuilder.customEmbed(embedData?.title, embedData?.description, member)
        } else {
            embedBuilder.defaultEmbed(member)
        }

        channel.sendMessageEmbeds(embed).queue()
        event.reply("✅ Mensagem de teste enviada!").setEphemeral(true).queue()
    }
}
