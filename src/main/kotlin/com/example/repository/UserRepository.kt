package com.example.repository

import com.example.model.db.dao.user.User
import java.time.LocalDate

interface UserRepository {
    suspend fun getUsers() : List<User>
    suspend fun getUserById(userId: String) : List<User>
    suspend fun insertUser(username : String, email: String, password: String, createAccountDate: LocalDate)
    suspend fun updateUsername(userId: String, name: String)
    suspend fun updateUserEmail(userId: String, email: String)
    suspend fun updateUserPassword(userId: String, password: String)
    suspend fun deleteUserById(userId : String)
}