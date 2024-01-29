package com.example.service

import com.example.model.dto.userDTO.User
import com.example.model.dto.userDTO.UserInsert
import com.example.model.dto.userDTO.UserReturns
import com.example.repository.UserRepository
import com.example.utils.PasswordHash


class UserService(private val userRepository: UserRepository) {

    suspend fun createUser(user: UserInsert) {
        return userRepository.insertUser(
            username = user.username,
            email = user.email,
            password = PasswordHash.hashPassword(user.password),
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
        userRepository.updateUserPassword(userId, PasswordHash.hashPassword(password))
    }
    suspend fun deleteAccount(userId: String) {
        userRepository.deleteUserById(userId)
    }
    suspend fun checkUsername(name : String) : Boolean {
        return userRepository.checkIfNameAlreadyExists(name)
    }
    suspend fun checkEmail(email : String) : Boolean {
        return userRepository.checkIfEmailAlreadyExists(email)
    }
    fun verifyPassword(inputPass : String, password: String) : Boolean {
        return PasswordHash.checkPassword(inputPass = inputPass, hashPass = password)
    }

}