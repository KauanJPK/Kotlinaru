package kauanjpk.bot.kotlinaru.commands

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.Command
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
                Commands.slash("play", "Toca uma música").addOption(
                        OptionType.STRING,
                        "query",
                        "Nome ou link da música",
                        true
                    ),
                // Musicas
                Commands.slash("skip", "Pula a música atual"),
                Commands.slash("stop", "Para a música e limpa a fila"),
                Commands.slash("queue", "Mostra a fila de músicas"),
                Commands.slash("pause", "Pausa/retoma a música"),
                // Moderação
                Commands.slash("ban", "Bane um usuário do servidor").addOption(
                    OptionType.USER,
                    "user",
                    "Usuário a ser banido",
                    true
                ).addOption(
                    OptionType.STRING,
                    "reason",
                    "Motivo do banimento",
                    false
                ).addOption(OptionType.INTEGER,
                    "delete_days",
                    "Número de dias de mensagens a serem deletadas (0-7, padrão: 0)",
                    false
                ),
                Commands.slash("kick", "Expulsa um usuário do servidor").addOption(
                    OptionType.USER,
                    "user",
                    "Usuário a ser expulso",
                    true
                ).addOption(
                    OptionType.STRING,
                    "reason",
                    "Motivo da expulsão",
                    false
                ),
                Commands.slash("mute", "Silencia um usuário no servidor").addOption(
                    OptionType.USER,
                    "user",
                    "Usuário a ser silenciado",
                    true
                    ).addOption(
                    OptionType.STRING,
                    "reason",
                    "Motivo do silêncio",
                    false
                    ).addOption(
                    OptionType.INTEGER,
                    "duration",
                    "Duração do silêncio em minutos (padrão: 10)",
                    false
                ),
                Commands.slash("unmute", "Remove o silêncio de um usuário no servidor").addOption(
                    OptionType.USER,
                    "user",
                    "Usuário a ser dessilenciado",
                    true
                    ).addOption(
                    OptionType.STRING,
                    "reason",
                    "Motivo para remover o silêncio",
                    false
                )
            )
            .queue()
    }
}