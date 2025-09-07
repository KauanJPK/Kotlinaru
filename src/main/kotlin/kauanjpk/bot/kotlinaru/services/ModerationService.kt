package kauanjpk.bot.kotlinaru.services

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.UserSnowflake
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.Permission
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

// Scheduler global para tarefas temporizadas
object TaskScheduler {
    val scheduler = Executors.newScheduledThreadPool(1)
}

open class ModerationService {

    fun ban(event: SlashCommandInteractionEvent) {
        val userOption = event.getOption("user")?.asUser
        val userIdOption = event.getOption("userId")?.asString
        val deletionTime = event.getOption("delete_days")?.asInt?.coerceIn(0, 7) ?: 0
        val reason = event.getOption("reason")?.asString ?: "Sem motivo"

        val targetSnowflake = when {
            userOption != null -> UserSnowflake.fromId(userOption.id)
            !userIdOption.isNullOrBlank() -> UserSnowflake.fromId(userIdOption)
            else -> null
        }

        if (targetSnowflake == null) {
            event.reply("❌ Você precisa informar um usuário ou ID válido.")
                .setEphemeral(true).queue()
            return
        }

        event.guild?.ban(targetSnowflake, deletionTime, TimeUnit.DAYS)?.reason(reason)?.queue(
            { event.reply("✅ Usuário banido com sucesso!").setEphemeral(true).queue() },
            { throwable -> event.reply("❌ Não foi possível banir. Erro: ${throwable.message}")
                .setEphemeral(true).queue() }
        )
    }

    fun kick(event: SlashCommandInteractionEvent) {
        val user = event.getOption("user")?.asUser
        val reason = event.getOption("reason")?.asString ?: "Sem motivo"
        if (user == null) {
            event.reply("❌ Você precisa informar um usuário válido.").setEphemeral(true).queue()
            return
        }
        event.guild?.kick(user, reason)?.queue(
            { event.reply("✅ Usuário ${user.asTag} expulso com sucesso!").setEphemeral(true).queue() },
            { event.reply("❌ Não foi possível expulsar ${user.asTag}.").setEphemeral(true).queue() }
        )
    }

    fun mute(event: SlashCommandInteractionEvent) {
        event.deferReply(true).queue() // mais tempo para processar

        val user = event.getOption("user")?.asUser
        val reason = event.getOption("reason")?.asString ?: "Sem motivo"
        val duration = event.getOption("duration")?.asInt ?: 10 // padrão: 10 minutos

        if (user == null) {
            event.hook.sendMessage("❌ Você precisa informar um usuário válido.").queue()
            return
        }

        val member: Member = event.guild?.getMember(user) ?: run {
            event.hook.sendMessage("❌ Usuário não encontrado no servidor.").queue()
            return
        }

        var mutedRole = event.guild?.roles?.find { it.name.equals("Muted", ignoreCase = true) }

        if (mutedRole == null) {
            // cria cargo se não existir
            event.guild?.createRole()
                ?.setName("Muted")
                ?.setPermissions()
                ?.setColor(0x808080)
                ?.queue({ role ->
                    mutedRole = role
                    event.guild?.textChannels?.forEach { channel ->
                        channel.upsertPermissionOverride(role)
                            ?.setDenied(Permission.MESSAGE_SEND, Permission.VOICE_SPEAK)
                            ?.queue()
                    }
                    aplicarMute(event, member, role, reason, duration)
                }, {
                    event.hook.sendMessage("❌ Não foi possível criar o cargo 'Muted'.").queue()
                })
        } else {
            aplicarMute(event, member, mutedRole!!, reason, duration)
        }
    }

    private fun aplicarMute(
        event: SlashCommandInteractionEvent,
        member: Member,
        mutedRole: net.dv8tion.jda.api.entities.Role,
        reason: String,
        duration: Int
    ) {
        event.guild?.addRoleToMember(member, mutedRole)?.reason(reason)?.queue({
            event.hook.sendMessage("✅ Usuário ${member.user.asTag} silenciado por $duration minutos!").queue()

            // agenda unmute automático
            TaskScheduler.scheduler.schedule({
                event.guild?.removeRoleFromMember(member, mutedRole)?.queue()
            }, duration.toLong(), TimeUnit.MINUTES)

        }, {
            event.hook.sendMessage("❌ Não foi possível silenciar ${member.user.asTag}.").queue()
        })
    }

    fun unmute(event: SlashCommandInteractionEvent) {
        event.deferReply(true).queue()
        val user = event.getOption("user")?.asUser
        if (user == null) {
            event.hook.sendMessage("❌ Você precisa informar um usuário válido.").queue()
            return
        }

        val member: Member = event.guild?.getMember(user) ?: run {
            event.hook.sendMessage("❌ Usuário não encontrado no servidor.").queue()
            return
        }

        val mutedRole = event.guild?.roles?.find { it.name.equals("Muted", ignoreCase = true) }
        if (mutedRole == null) {
            event.hook.sendMessage("❌ O cargo 'Muted' não existe.").queue()
            return
        }

        event.guild?.removeRoleFromMember(member, mutedRole)?.queue({
            event.hook.sendMessage("✅ Usuário ${user.asTag} foi desmutado!").queue()
        }, {
            event.hook.sendMessage("❌ Não foi possível desmutar ${user.asTag}.").queue()
        })
    }
}
