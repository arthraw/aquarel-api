package com.example.repository

import com.example.model.dto.userDTO.User

interface UserRepository {
    suspend fun getUsers() : List<User>
    suspend fun getUserById(userId: String) : List<User>
    suspend fun insertUser(username : String, email: String, password: String)
    suspend fun updateUsername(userId: String, name: String)
    suspend fun updateUserEmail(userId: String, email: String)
    suspend fun updateUserPassword(userId: String, password: String)
    suspend fun deleteUserById(userId : String)
    suspend fun checkIfNameAlreadyExists(username : String) : Boolean
    suspend fun checkIfEmailAlreadyExists(email : String) : Boolean
}