package com.example.model.db.dao.user

import com.example.utils.LocalDateSerializer
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate
import java.util.UUID

@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    val password: String,
    @Serializable(with = LocalDateSerializer::class)
    val createAccountDate: LocalDate
)
@Serializable
data class UserReturns(
    val id: String,
    val username: String,
)
@Serializable
data class UserInsert(
    val username: String,
    val email: String,
    val password: String,
    @Serializable(with = LocalDateSerializer::class)
    val createAccountDate: LocalDate
)
object UserTable : Table() {
    val id = varchar("user_id", 100).default(UUID.randomUUID().toString())
    val username = varchar("name", 255)
    val email = varchar("email", 100)
    val password = varchar("password", 200)
    val createAccountDate = date("create_account_date")

    override val primaryKey = PrimaryKey(id)
}