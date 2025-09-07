package kauanjpk.bot.kotlinaru.services

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

open class GeneralService {
    fun ping(event: SlashCommandInteractionEvent) {
        val time = System.currentTimeMillis()
        event.reply("🏓 Pong!").setEphemeral(true)
            .queue { hook ->
                hook.editOriginal("🏓 Pong! ${System.currentTimeMillis() - time}ms").queue()
            }
    }

    fun help(event: SlashCommandInteractionEvent) {
        val helpMsg = """
            📜 **Comandos disponíveis**:
            - `/ping` → Testa latência
            - `/help` → Mostra ajuda
            - `/play <música>` → Toca música
            - `/skip` → Pula música
            - `/stop` → Para e limpa fila
            - `/queue` → Mostra fila
            - `/pause` → Pausa/retoma música
        """.trimIndent()

        event.reply(helpMsg).queue()
    }
}
