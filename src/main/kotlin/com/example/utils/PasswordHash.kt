package com.example.utils

import org.mindrot.jbcrypt.BCrypt

object PasswordHash {
    fun hashPassword(pass: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(pass, salt)
    }

    fun checkPasword(inputPass : String, hashPass: String): Boolean {
        return BCrypt.checkpw(inputPass, hashPass)
    }
}