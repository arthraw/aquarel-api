package com.example.routes.user

import com.example.model.dto.userDTO.UserLogin
import com.example.utils.JwtConfig
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException

fun Route.loginRoute() {
    post {
        try {
            val userData = call.receive<UserLogin>()

            val user = service.getUsers().filter {
                it.email == userData.email
            }
            if (user.first().email != userData.email) {
                call.respondText("wrong user email", status = HttpStatusCode.NotFound)
            }
            val userPassHashed = user.map { it.password }

            val authUser = service.verifyPassword(userData.password, userPassHashed.first())

            val userLogin = UserLogin(
                email = userData.email,
                password = userData.password
            )
            if (authUser) {
                val token : String = JwtConfig.createToken(userLogin)
                val responseMap = mapOf("token" to token)
                val jsonResponse = Gson().toJson(responseMap)

                call.respondText(text = jsonResponse, contentType = ContentType.Application.Json, status = HttpStatusCode.OK)
            } else {
                call.respondText("wrong user password", status = HttpStatusCode.NotFound)
            }
        } catch (e : SerializationException) {
            call.respondText("Internal error", status = HttpStatusCode.InternalServerError)
        }
    }
}