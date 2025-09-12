package kauanjpk.bot.kotlinaru.services

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import kauanjpk.bot.kotlinaru.embeds.WelcomeEmbed
import net.dv8tion.jda.api.EmbedBuilder

open class GeneralService {
    val help = WelcomeEmbed()

    fun ping(event: SlashCommandInteractionEvent) {
        val time = System.currentTimeMillis()
        event.reply("🏓 Pong!").setEphemeral(true)
            .queue { hook ->
                hook.editOriginal("🏓 Pong! ${System.currentTimeMillis() - time}ms").queue()
            }
    }

    fun help(event: SlashCommandInteractionEvent) {
        event.reply("Olá")
    }
}





