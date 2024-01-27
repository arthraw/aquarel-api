package com.example.model.db

import com.example.model.db.dao.user.UserTable
import io.github.cdimascio.dotenv.dotenv
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DbConfiguration {
    fun init() {
        val dotenv = dotenv {
            directory = "src/.env"
        }
        // Get env variables
        val dbUser = dotenv.get("DATABASE_USER")
        val dbPass = dotenv.get("DATABASE_PASSWORD")
        val connUrl = "jdbc:mysql://localhost:3306/aquarel"
        val classPathDb = "classpath:db/migration"

        val flyway = Flyway.configure()
            .dataSource(connUrl,dbUser,dbPass)
            .locations(classPathDb)
            .baselineOnMigrate(true)
            .load()

        flyway.migrate()

        val driverClassName = "com.mysql.cj.jdbc.Driver"
         Database.connect(connUrl, driver = driverClassName, user = dbUser, password = dbPass)

        transaction {
            SchemaUtils.create(UserTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}