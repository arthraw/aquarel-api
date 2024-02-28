package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.dto.userDTO.UserLogin
import io.github.cdimascio.dotenv.dotenv

object JwtConfig {

    private val dotenv = dotenv {
        directory = "src/.env"
    }
    private val secret = dotenv.get("JWT_SECRET")
    private val issuer = dotenv.get("jwt_issuer")

    val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withIssuer(issuer)
        .build()

    fun createToken(user : UserLogin) : String {
        return JWT.create()
            .withSubject(user.email)
            .withIssuer(issuer)
            .sign(Algorithm.HMAC256(secret))
    }
}