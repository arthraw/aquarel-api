package com.example.service

import com.example.model.db.dao.user.User
import com.example.model.db.dao.user.UserInsert
import com.example.model.db.dao.user.UserReturns
import com.example.repository.UserRepository

class UserService(private val userRepository: UserRepository) {

    suspend fun createUser(user: UserInsert) {
        return userRepository.insertUser(
            username = user.username,
            email = user.email,
            password = user.password,
            createAccountDate = user.createAccountDate
        )
    }
    suspend fun getUsers(): List<User> {
        return userRepository.getUsers()
    }
    suspend fun getUserById(userId: String): List<UserReturns> {
        return userRepository.getUserById(userId).map {
            UserReturns(
                id = it.id,
                username = it.username
            )
        }
    }
    suspend fun updateUsername(userId: String, username: String) {
        userRepository.updateUsername(userId, username)
    }
    suspend fun updateUserEmail(userId: String, email: String) {
        userRepository.updateUserEmail(userId, email)
    }
    suspend fun updateUserPassword(userId: String, password: String) {
        userRepository.updateUserPassword(userId, password)
    }
    suspend fun deleteAccount(userId: String) {
        userRepository.deleteUserById(userId)
    }
}