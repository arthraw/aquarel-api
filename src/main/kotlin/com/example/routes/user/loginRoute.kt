package com.example.routes.user

import com.example.model.dto.userDTO.UserLogin
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
                call.respondText("wrong user email", status = HttpStatusCode.InternalServerError)
            }
            val userPassHashed = user.map { it.password }

            val authUser = service.verifyPassword(userData.password, userPassHashed.first())

            if (authUser) {
                call.respondText("User authenticated", status = HttpStatusCode.OK)
            } else {
                call.respondText("wrong user password", status = HttpStatusCode.NotFound)
            }
        } catch (e : SerializationException) {
            call.respondText("Invalid data", status = HttpStatusCode.InternalServerError)

        }
    }
}