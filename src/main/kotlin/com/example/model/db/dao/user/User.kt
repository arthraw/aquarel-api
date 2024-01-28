package com.example.model.db.dao.user

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate
import java.util.UUID

object UserTable : Table() {
    val id = varchar("user_id", 255).default(UUID.randomUUID().toString())
    val username = varchar("name", 100)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val createAccountDate = date("create_account_date").default(LocalDate.now())

    override val primaryKey = PrimaryKey(id)
}