package com.example.routes.login

import com.example.model.dto.userDTO.UserLogin
import com.example.routes.user.service
import com.example.utils.JwtConfig
import com.google.gson.Gson
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException


val dotenv = dotenv {
    directory = "src/.env"
}

fun Route.loginRoute() {
    post {
        try {
            val userData = call.receive<UserLogin>()
            val userSearch = service.checkEmail(userData.email)
            val apiToken = call.request.headers["token"] ?: ""

            if (apiToken.isEmpty()) {
                call.respondText("API Token is empty", status = HttpStatusCode.BadRequest)
            }
            val trueToken = dotenv.get("API_TOKEN") ?: ""
            if (apiToken != trueToken) {
                call.respondText("Wrong API Token", status = HttpStatusCode.BadRequest)
            }

            if (userSearch.isEmpty()) {
                call.respondText("Wrong user email", status = HttpStatusCode.NotFound)
            }

            val userPassHashed = userSearch.map { it.password }

            val authUser = service.verifyPassword(userData.password, userPassHashed.first())

            val userLogin = UserLogin(
                email = userData.email,
                password = userData.password
            )

            val userName = userSearch[0].username
            if (authUser) {
                val token : String = JwtConfig.createToken(userLogin)
                val responseMap: Map<String, String> = mapOf("token" to token, "username" to userName)
                val jsonResponse = Gson().toJson(responseMap)

                call.respondText(text = jsonResponse, contentType = ContentType.Application.Json, status = HttpStatusCode.OK)
            } else {
                call.respondText("Wrong user password", status = HttpStatusCode.NotFound)
            }
        } catch (e : SerializationException) {
            call.respondText("Internal error", status = HttpStatusCode.InternalServerError)
        }
    }
}