package com.example.routes.user

import com.example.data.userData.UserRepositoryImpl
import com.example.model.dto.userDTO.PatchUser
import com.example.model.dto.userDTO.UserInsert
import com.example.repository.UserRepository
import com.example.service.UserService
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

val data: UserRepository = UserRepositoryImpl()
val service = UserService(data)

val dotenv = dotenv {
    directory = "src/.env"
}

fun Route.userRoutes() {
    get {
        val apiToken = call.request.headers["token"] ?: ""
        if (apiToken.isEmpty()) {
            call.respondText("API Token is empty", status = HttpStatusCode.BadRequest)
        }
        val trueToken = dotenv.get("API_TOKEN") ?: ""
        if (apiToken != trueToken) {
            call.respondText("Wrong API Token", status = HttpStatusCode.BadRequest)
        }
        val listUsers = service.getUsers()
        call.respond(listUsers)
    }
    get("/{id}") {
        val userId = call.parameters["id"]
        val apiToken = call.request.headers["token"] ?: ""

        if (apiToken.isEmpty()) {
            call.respondText("API Token is empty", status = HttpStatusCode.BadRequest)
        }
        val trueToken = dotenv.get("API_TOKEN") ?: ""
        if (apiToken != trueToken) {
            call.respondText("Wrong API Token", status = HttpStatusCode.BadRequest)
        }
        if (userId != null) {
            val search = service.getUserById(userId)
            call.respond(search)
        }
    }
    post {
        try {
            val data = call.receive<UserInsert>()
            val apiToken = call.request.headers["token"] ?: ""

            if (apiToken.isEmpty()) {
                call.respondText("API Token is empty", status = HttpStatusCode.BadRequest)
            }
            val trueToken = dotenv.get("API_TOKEN") ?: ""
            if (apiToken != trueToken) {
                call.respondText("Wrong API Token", status = HttpStatusCode.BadRequest)
            }
            val nameNotExists = service.checkUsername(data.username)
            val emailNotExists = service.checkEmail(data.email)

            if (nameNotExists && emailNotExists.isEmpty()) {
                service.createUser(
                    UserInsert(
                        username = data.username,
                        email = data.email,
                        password = data.password
                    )
                )
            } else {
                call.respondText("Name or email already exists", status = HttpStatusCode.BadRequest)
            }
            call.respondText("User created", status = HttpStatusCode.Created)

        } catch (e: SerializationException) {
            call.respondText("Invalid data", status = HttpStatusCode.BadRequest)
        }
    }
    patch("/{id}/edit") {
        val userId = call.parameters["id"] ?: ""
        val apiToken = call.request.headers["token"] ?: ""

        if (apiToken.isEmpty()) {
            call.respondText("API Token is empty", status = HttpStatusCode.BadRequest)
        }
        val trueToken = dotenv.get("API_TOKEN") ?: ""
        if (apiToken != trueToken) {
            call.respondText("Wrong API Token", status = HttpStatusCode.BadRequest)
        }
        val content = call.receiveText()
        val updateData = Json.decodeFromString<PatchUser>(content)

        when {
            updateData.username?.isNotEmpty() == true && updateData.email?.isNotEmpty() == true -> {
                if (userId.isNotEmpty()) {
                    service.updateUsername(userId, updateData.username)
                    service.updateUserEmail(userId, updateData.email)
                    call.respondText("User updated", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Error in user update, verify if user Id was correct", status = HttpStatusCode.NotFound)
                }            }
            updateData.username?.isNotEmpty() == true -> {
                if (userId.isNotEmpty()) {
                    service.updateUsername(userId, updateData.username)
                    call.respondText("User name updated", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Error in name update, verify if user Id was correct", status = HttpStatusCode.NotFound)
                }
            }
            updateData.email?.isNotEmpty() == true -> {
                if (userId.isNotEmpty()) {
                    service.updateUserEmail(userId, updateData.email)
                    call.respondText("User email updated", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Error in email update, verify if user Id was correct", status = HttpStatusCode.NotFound)
                }
            }
            updateData.password?.isNotEmpty() == true -> {
                call.respondText("Error in password update, verify if user Id was correct", status = HttpStatusCode.NotFound)
            }
            else -> {
                call.respondText("ERROR", status = HttpStatusCode.InternalServerError)
            }
        }
    }
    patch("/{id}/edit") {
        val userId = call.parameters["id"] ?: ""
        val apiToken = call.request.headers["token"] ?: ""

        if (apiToken.isEmpty()) {
            call.respondText("API Token is empty", status = HttpStatusCode.BadRequest)
        }
        val trueToken = dotenv.get("API_TOKEN") ?: ""
        if (apiToken != trueToken) {
            call.respondText("Wrong API Token", status = HttpStatusCode.BadRequest)
        }
        val content = call.receiveText()
        val updateData = Json.decodeFromString<PatchUser>(content)

        if (userId.isNotEmpty()) {
            service.updateUserEmail(userId, updateData.email ?: "")
            call.respondText("User email updated", status = HttpStatusCode.OK)
        } else {
            call.respondText("Error in email update, verify if user Id was correct", status = HttpStatusCode.NotFound)
        }
    }
    delete("/{id}/edit") {
        val userId = call.parameters["id"] ?: ""
        val apiToken = call.request.headers["token"] ?: ""

        if (apiToken.isEmpty()) {
            call.respondText("API Token is empty", status = HttpStatusCode.BadRequest)
        }
        val trueToken = dotenv.get("API_TOKEN") ?: ""
        if (apiToken != trueToken) {
            call.respondText("Wrong API Token", status = HttpStatusCode.BadRequest)
        }
        if (userId.isNotEmpty()) {
            service.deleteAccount(userId)
            call.respondText("Account deleted", status = HttpStatusCode.OK)
        } else {
            call.respondText("Error account delete, verify if user Id was correct", status = HttpStatusCode.NotFound)
        }
    }
}
