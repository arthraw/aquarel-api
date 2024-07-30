package com.example.data.userData

import com.example.model.db.DbConfiguration.dbQuery
import com.example.model.db.dao.user.UserTable
import com.example.model.dto.userDTO.User
import com.example.repository.UserRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

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

    override suspend fun insertUser(username : String, email: String, password: String): Unit = dbQuery {
        val insertData = UserTable.insert {
            it[UserTable.username] = username
            it[UserTable.email] = email
            it[UserTable.password] = password
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
            it[UserTable.password] = password
        }
    }

    override suspend fun deleteUserById(userId: String): Unit = dbQuery {
        UserTable.deleteWhere{ UserTable.id eq userId }
    }

    override suspend fun checkIfNameAlreadyExists(username: String): Boolean = dbQuery {
        UserTable.select(UserTable.username eq username).empty()
    }

    override suspend fun checkIfEmailAlreadyExists(email: String): List<User> = dbQuery {
        UserTable.select(UserTable.email eq email).map(::resultUserRow)
    }
}