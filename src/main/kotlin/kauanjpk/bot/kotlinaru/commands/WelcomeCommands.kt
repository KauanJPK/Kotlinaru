package kauanjpk.bot.kotlinaru.commands

import kauanjpk.bot.kotlinaru.services.WelcomeService
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.OptionType

class WelcomeCommands(private val service: WelcomeService) : ListenerAdapter() {

    fun registerCommands() = listOf(
        Commands.slash("setwelcomechannel", "Define o canal de boas-vindas")
            .addOption(OptionType.CHANNEL, "channel", "Canal de boas-vindas", true),
        Commands.slash("setcustomembed", "Define título e descrição do embed de boas-vindas")
            .addOption(OptionType.STRING, "title", "Título do embed", true)
            .addOption(OptionType.STRING, "description", "Descrição do embed", true),
        Commands.slash("resetcustomembed", "Reseta o embed de boas-vindas para o padrão"),
        Commands.slash("testwelcome", "Envia uma mensagem de teste de boas-vindas")
    )

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        when (event.name.lowercase()) {
            "setwelcomechannel" -> service.setChannel(event)
            "setcustomembed" -> service.setCustomEmbed(event)
            "resetcustomembed" -> service.resetCustomEmbed(event)
            "testwelcome" -> service.testWelcome(event)
        }
    }
}
