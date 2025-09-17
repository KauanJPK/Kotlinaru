package kauanjpk.bot.kotlinaru.config

import kauanjpk.bot.kotlinaru.database.DBConnection
import java.sql.Connection

data class EmbedData(val title: String?, val description: String?)

class WelcomeManager(private val db: DBConnection) {

    fun getWelcomeChannelId(guildId: Long): Long? {
        val connection: Connection = db.getConnection()
        connection.prepareStatement(
            "SELECT guildChat FROM guilds WHERE guildID = ?"
        ).use { stmt ->
            stmt.setLong(1, guildId)
            stmt.executeQuery().use { rs ->
                if (rs.next()) return rs.getLong("guildChat")
            }
        }
        connection.close()
        return null
    }

    fun updateWelcomeChannel(guildId: Long, channelId: Long) {
        val connection = db.getConnection()
        connection.prepareStatement(
            """
            INSERT INTO guilds (guildID, guildChat)
            VALUES (?, ?)
            ON DUPLICATE KEY UPDATE guildChat = ?
            """
        ).use { stmt ->
            stmt.setLong(1, guildId)
            stmt.setLong(2, channelId)
            stmt.setLong(3, channelId)
            stmt.executeUpdate()
        }
        connection.close()
    }

    fun updateCustomEmbed(guildId: Long, title: String?, description: String?) {
        val connection = db.getConnection()
        connection.prepareStatement(
            """
            INSERT INTO guilds (guildID, welcomeTitle, welcomeDescription)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE welcomeTitle = ?, welcomeDescription = ?
            """
        ).use { stmt ->
            stmt.setLong(1, guildId)
            stmt.setString(2, title)
            stmt.setString(3, description)
            stmt.setString(4, title)
            stmt.setString(5, description)
            stmt.executeUpdate()
        }
        connection.close()
    }

    fun getCustomEmbedData(guildId: Long): EmbedData? {
        val connection = db.getConnection()
        connection.prepareStatement(
            "SELECT welcomeTitle, welcomeDescription FROM guilds WHERE guildID = ?"
        ).use { stmt ->
            stmt.setLong(1, guildId)
            stmt.executeQuery().use { rs ->
                if (rs.next()) {
                    return EmbedData(rs.getString("welcomeTitle"), rs.getString("welcomeDescription"))
                }
            }
        }
        connection.close()
        return null
    }
}
