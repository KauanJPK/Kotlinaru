package kauanjpk.bot.kotlinaru.config

import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.File

@Serializable
data class WelcomeEmbedData(
    val title: String = "Bem-vindo!",
    val description: String = "Seja bem-vindo ao servidor!",
    val color: Int = 0x00FF00,
    val imageUrl: String? = null
)
@Serializable
data class WelcomeConfig(
    val guildChannels: MutableMap<Long, Long> = mutableMapOf(), // guildId -> channelId
    val customEmbeds: MutableMap<Long, WelcomeEmbedData> = mutableMapOf() // guildId -> embed customizado
)
@Serializable
object Config {
    private val file = File("welcome_config.json")
    val data: WelcomeConfig = if (file.exists()) {
        Json.decodeFromString(file.readText())
    } else {
        WelcomeConfig()
    }

    fun save() {
        file.writeText(Json.encodeToString(data))
    }
}
