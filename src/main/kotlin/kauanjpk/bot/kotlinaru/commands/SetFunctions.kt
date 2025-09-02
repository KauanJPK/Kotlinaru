package kauanjpk.bot.kotlinaru.commands

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

//this object is where control the settings of a slash command
object SetFunctions {
    fun register(jda: JDA) {
        jda.updateCommands()
            .addCommands(
                // Gerais
                Commands.slash("ping", "Mostra o tempo de resposta do bot"),
                Commands.slash("help", "Mostra a lista de comandos"),

                // Voz
                Commands.slash("play", "Toca uma música")
                    .addOption(
                        OptionType.STRING,
                        "query", // <- o nome precisa ser "query"
                        "Nome ou link da música",
                        true // obrigatório
                    ),

                        Commands.slash("skip", "Pula a música atual"),
                Commands.slash("stop", "Para a música e limpa a fila"),
                Commands.slash("queue", "Mostra a fila de músicas"),
                Commands.slash("pause", "Pausa/retoma a música")
            )
            .queue()
    }
}
