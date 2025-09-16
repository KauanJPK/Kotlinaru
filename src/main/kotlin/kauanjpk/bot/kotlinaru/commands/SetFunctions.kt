package kauanjpk.bot.kotlinaru.commands

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands

object SetFunctions {
    fun register(jda: JDA) {
        jda.updateCommands()
            .addCommands(
                // Gerais
                Commands.slash("ping", "Mostra o tempo de resposta do bot"),
                Commands.slash("help", "Mostra a lista de comandos"),

                // Música
                Commands.slash("play", "Toca uma música").addOption(
                    OptionType.STRING,
                    "query",
                    "Nome ou link da música",
                    true
                ),
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
                ).addOption(
                    OptionType.INTEGER,
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
                ),

                // Welcome
                Commands.slash("setwelcomechannel", "Define o canal de boas-vindas")
        .addOption(OptionType.CHANNEL, "channel", "Canal de texto para mensagens de boas-vindas", true),

        Commands.slash("setcustomembed", "Define um embed de boas-vindas customizado")
            .addOption(OptionType.STRING, "title", "Título do embed", false)
            .addOption(OptionType.STRING, "description", "Descrição do embed (use {user} para mencionar)", false)
            .addOption(OptionType.STRING, "color", "Cor em hexadecimal (#RRGGBB)", false)
            .addOption(OptionType.STRING, "image_url", "URL da imagem principal", false)
            .addOption(OptionType.STRING, "thumbnail_url", "URL da thumbnail", false)
            .addOption(OptionType.STRING, "footer", "Texto do rodapé", false)
            .addOption(OptionType.STRING, "footer_icon_url", "URL do ícone do rodapé", false)
            .addOption(OptionType.STRING, "author", "Nome do autor", false)
            .addOption(OptionType.STRING, "author_icon_url", "URL do ícone do autor", false)
            .addOption(OptionType.STRING, "author_url", "URL clicável do autor", false),

         Commands.slash("resetcustomembed", "Reseta o embed de boas-vindas para o padrão"),

         Commands.slash("testwelcome", "Testa a mensagem de boas-vindas no servidor")
            ).queue()

    }
}