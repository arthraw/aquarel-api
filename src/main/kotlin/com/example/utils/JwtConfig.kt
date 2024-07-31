package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.dto.userDTO.UserLogin
import io.github.cdimascio.dotenv.dotenv
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object JwtConfig {

    private val dotenv = dotenv {
        directory = "src/.env"
    }
    private val secret = dotenv.get("JWT_SECRET")
    private val issuer = dotenv.get("JWT_ISSUER")

    val verifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withIssuer(issuer)
        .build()

    fun createToken(user : UserLogin) : String {
        val tokenExpireDate = Date.from(
            LocalDateTime.now()
                .plusMonths(6)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        )

        return JWT.create()
            .withSubject(user.email)
            .withIssuer(issuer)
            .withExpiresAt(tokenExpireDate)
            .sign(Algorithm.HMAC256(secret))
    }
}