package com.example

import com.example.model.db.DbConfiguration
import com.example.model.dto.userDTO.UserIdToken
import com.example.model.dto.userDTO.UserLogin
import com.example.plugins.*
import com.example.utils.JwtConfig
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(args: Array<String>) : Unit = io.ktor.server.netty.EngineMain.main(args)


fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
        }
    }
    DbConfiguration.init()
    configureRouting()
}
