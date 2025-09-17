package kauanjpk.bot.kotlinaru.embeds

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageEmbed

class WelcomeEmbed {

    fun defaultEmbed(member: Member): MessageEmbed {
        val guild = member.guild
        return EmbedBuilder()
            .setTitle("Bem-vindo ao ${guild.name}!")
            .setDescription("Olá ${member.asMention}! Seja bem-vindo ao servidor ${guild.name}.")
            .addField("Regras", "1. Respeite todos os membros.\n2. Não compartilhe conteúdo inapropriado.\n3. Use os canais corretamente.", false)
            .addField("Canais Importantes", "#geral, #ajuda, #projetos, #off-topic", false)
            .addField("Membros", "Atualmente temos ${guild.memberCount} membros!", false)
            .setFooter("Divirta-se e aproveite sua estadia!")
            .setColor(0x00FF00)
            .setImage("https://i.pinimg.com/originals/88/e5/54/88e5540465d258ab1d271bfc11189533.gif")
            .build()
    }

    fun customEmbed(title: String?, description: String?, member: Member): MessageEmbed {
        val guild = member.guild
        val safeTitle = if (title.isNullOrBlank()) "Bem-vindo!" else title
        val safeDescription = if (description.isNullOrBlank())
            "Olá ${member.asMention}! Seja bem-vindo ao servidor ${guild.name}."
        else description

        return EmbedBuilder()
            .setTitle(safeTitle)
            .setDescription(safeDescription)
            .setColor(0x00FF00)
            .build()
    }
}
