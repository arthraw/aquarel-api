package com.example.data.userData

import com.example.model.db.DbConfiguration.dbQuery
import com.example.model.db.dao.user.User
import com.example.model.db.dao.user.UserTable
import com.example.repository.UserRepository
import com.example.utils.PasswordHash
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.LocalDate

class UserRepositoryImpl : UserRepository {

    private fun resultUserRow(row : ResultRow) = User(
        id = row[UserTable.id],
        username = row[UserTable.username],
        email = row[UserTable.email],
        password = row[UserTable.password],
        createAccountDate = row[UserTable.createAccountDate]
    )
    override suspend fun getUsers(): List<User> = dbQuery {
        UserTable.selectAll().map(::resultUserRow)
    }

    override suspend fun getUserById(userId : String): List<User> = dbQuery {
        UserTable.select(UserTable.id eq userId).map(::resultUserRow)
    }

    override suspend fun insertUser(username : String, email: String, password: String, createAccountDate: LocalDate): Unit = dbQuery {
        val insertData = UserTable.insert {
            it[UserTable.username] = username
            it[UserTable.email] = email
            it[UserTable.password] = password
            it[UserTable.createAccountDate] = createAccountDate
        }
        insertData.resultedValues?.singleOrNull()?.let(::resultUserRow)
    }

    override suspend fun updateUsername(userId: String, name: String): Unit = dbQuery {
        UserTable.update({ UserTable.id eq userId }) {
            it[username] = name
        }
    }

    override suspend fun updateUserEmail(userId: String, email: String): Unit = dbQuery {
        UserTable.update({ UserTable.id eq userId }) {
            it[UserTable.email] = email
        }
    }

    override suspend fun updateUserPassword(userId: String, password: String): Unit = dbQuery {
        UserTable.update({ UserTable.id eq userId }) {
            it[UserTable.password] = PasswordHash.hashPassword(password)
        }
    }

    override suspend fun deleteUserById(userId: String): Unit = dbQuery {
        UserTable.deleteWhere{ UserTable.id eq userId }
    }
}