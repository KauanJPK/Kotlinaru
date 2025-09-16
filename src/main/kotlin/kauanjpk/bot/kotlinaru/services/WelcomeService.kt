package kauanjpk.bot.kotlinaru.services

import kauanjpk.bot.kotlinaru.embeds.WelcomeEmbed
import kauanjpk.bot.kotlinaru.config.Config
import kauanjpk.bot.kotlinaru.config.WelcomeEmbedData
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel

open class WelcomeService {

    private val welcomeEmbedBuilder = WelcomeEmbed()
    /** Define o canal de boas-vindas */
    fun setChannel(event: SlashCommandInteractionEvent) {
        val channelOption = event.getOption("channel")?.asChannel
        if (channelOption !is TextChannel) {
            event.reply("❌ Você precisa informar um canal de texto válido.").setEphemeral(true).queue()
            return
        }

        val textChannel = channelOption as TextChannel
        Config.data.guildChannels[textChannel.guild.idLong] = textChannel.idLong
        Config.save()

        event.reply("✅ Canal de boas-vindas definido para ${textChannel.asMention}")
            .setEphemeral(true).queue()
    }

    /** Define ou atualiza um embed customizado */
    fun setCustomEmbed(event: SlashCommandInteractionEvent) {
        val guildId = event.guild?.idLong ?: run {
            event.reply("❌ Não foi possível identificar o servidor.").setEphemeral(true).queue()
            return
        }

        val embedData = WelcomeEmbedData(
            title = event.getOption("title")?.asString ?: "Bem-vindo!",
            description = event.getOption("description")?.asString ?: "Seja bem-vindo ao servidor!",
            color = event.getOption("color")?.asString?.replace("#", "")?.toIntOrNull(16) ?: 0x00FF00,
            imageUrl = event.getOption("image_url")?.asString,
            thumbnailUrl = event.getOption("thumbnail_url")?.asString,
            footer = event.getOption("footer")?.asString,
            footerIconUrl = event.getOption("footer_icon_url")?.asString,
            author = event.getOption("author")?.asString,
            authorIconUrl = event.getOption("author_icon_url")?.asString,
            authorUrl = event.getOption("author_url")?.asString
        )

        Config.data.customEmbeds[guildId] = embedData
        Config.save()

        event.reply("✅ Embed de boas-vindas customizado definido!").setEphemeral(true).queue()
    }

    /** Reseta o embed customizado */
    fun resetCustomEmbed(event: SlashCommandInteractionEvent) {
        val guildId = event.guild?.idLong ?: run {
            event.reply("❌ Não foi possível identificar o servidor.").setEphemeral(true).queue()
            return
        }

        Config.data.customEmbeds.remove(guildId)
        Config.save()
        event.reply("✅ Embed de boas-vindas resetado para o padrão!").setEphemeral(true).queue()
    }

    /** Envia a mensagem de boas-vindas */
    fun welcome(member: Member) {
        val guildId = member.guild.idLong
        val channelId = Config.data.guildChannels[guildId] ?: member.guild.defaultChannel?.idLong
        if (channelId == null) return

        val channel = member.guild.getTextChannelById(channelId) ?: return

        val customEmbed = Config.data.customEmbeds[guildId]
        val embed = if (customEmbed != null) {
            EmbedBuilder().apply {
                setTitle(customEmbed.title)
                setDescription(customEmbed.description.replace("{user}", member.asMention))
                setColor(customEmbed.color)

                customEmbed.imageUrl?.let { setImage(it) }
                customEmbed.thumbnailUrl?.let { setThumbnail(it) }

                if (customEmbed.footer != null) setFooter(customEmbed.footer, customEmbed.footerIconUrl)
                if (customEmbed.author != null) setAuthor(customEmbed.author, customEmbed.authorUrl, customEmbed.authorIconUrl)
            }.build()
        } else {
            welcomeEmbedBuilder.welcomeEmbed(member)
        }

        channel.sendMessageEmbeds(embed).queue()
    }

    /** Testa a mensagem de boas-vindas para o usuário que executar o comando */
    fun testWelcome(event: SlashCommandInteractionEvent) {
        val member = event.member ?: return
        welcome(member)
        event.reply("✅ Mensagem de boas-vindas enviada com sucesso!").setEphemeral(true).queue()
    }
}
