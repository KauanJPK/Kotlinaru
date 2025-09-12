package kauanjpk.bot.kotlinaru.services

import kauanjpk.bot.kotlinaru.embeds.WelcomeEmbed
import kauanjpk.bot.kotlinaru.config.Config
import kauanjpk.bot.kotlinaru.config.WelcomeEmbedData
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

class WelcomeService {

    private val welcomeEmbedBuilder = WelcomeEmbed()

    /** Define a welcome channel */
    fun setChannel(event: SlashCommandInteractionEvent) {
        val channelOption = event.getOption("channel")?.asChannel
        if (channelOption !is TextChannel) {
            event.reply("❌ Você precisa informar um canal de texto válido.").setEphemeral(true).queue()
            return
        }

        val textChannel = channelOption as TextChannel
        Config.data.guildChannels[textChannel.guild.idLong] = textChannel.idLong
        Config.save()

        event.reply("✅ Canal de boas-vindas definido para ${textChannel.asMention}").setEphemeral(true).queue()
    }

    /** Define a custom embed */
    fun setCustomEmbed(event: SlashCommandInteractionEvent) {
        val title = event.getOption("title")?.asString ?: "Bem-vindo!"
        val description = event.getOption("description")?.asString ?: "Seja bem-vindo ao servidor!"
        val color = event.getOption("color")?.asString?.replace("#", "")?.toIntOrNull(16) ?: 0x00FF00
        val imageUrl = event.getOption("image_url")?.asString

        val guildId = event.guild?.idLong ?: run {
            event.reply("❌ Não foi possível identificar o servidor.").setEphemeral(true).queue()
            return
        }

        Config.data.customEmbeds[guildId] = WelcomeEmbedData(
            title = title,
            description = description,
            color = color,
            imageUrl = imageUrl
        )
        Config.save()
        event.reply("✅ Embed de boas-vindas customizado definido!").setEphemeral(true).queue()
    }

    /** Send a welcome message to a user */
    fun welcome(member: Member) {
        val guildId = member.guild.idLong
        val channelId = Config.data.guildChannels[guildId] ?: member.guild.defaultChannel?.idLong
        if (channelId == null) return

        val channel = member.guild.getTextChannelById(channelId) ?: return

        // Verify if there's a custom embed
        val customEmbed = Config.data.customEmbeds[guildId]
        val embed = if (customEmbed != null) {
            val builder = EmbedBuilder()
                .setTitle(customEmbed.title)
                .setDescription(customEmbed.description.replace("{user}", member.asMention))
                .setColor(customEmbed.color)
            customEmbed.imageUrl?.let { builder.setImage(it) }
            builder.build()
        } else {
            welcomeEmbedBuilder.welcomeEmbed(member) // embed default
        }

        channel.sendMessageEmbeds(embed).queue()
    }
}
