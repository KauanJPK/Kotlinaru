package kauanjpk.bot.kotlinaru.database

import java.sql.Connection
import java.sql.DriverManager
import io.github.cdimascio.dotenv.dotenv

class DBConnection {
    private val dotenv = dotenv()
    private val user = dotenv["USER_DB"] ?: error("Usuário do DB não encontrado no .env")
    private val password = dotenv["PASSWORD_DB"] ?: error("Senha do DB não encontrada no .env")
    private val url = dotenv["URL_DB"] ?: error("URL do DB não encontrada no .env")

    init {
        Class.forName("com.mysql.cj.jdbc.Driver")
    }

    fun getConnection(): Connection {
        return DriverManager.getConnection(url, user, password)
    }
}
