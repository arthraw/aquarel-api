package com.example.routes.user

import com.example.data.userData.UserRepositoryImpl
import com.example.model.dto.userDTO.PatchUser
import com.example.model.dto.userDTO.UserInsert
import com.example.repository.UserRepository
import com.example.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

val data: UserRepository = UserRepositoryImpl()
val service = UserService(data)

fun Route.userRoutes() {
    get {
        val listUsers = service.getUsers()
        call.respond(listUsers)
    }
    get("/{id}") {
        val userId = call.parameters["id"]

        if (userId != null) {
            val search = service.getUserById(userId)
            call.respond(search)
        }
    }
    post {
        try {
            val data = call.receive<UserInsert>()
            val nameNotExists = service.checkUsername(data.username)
            val emailNotExists = service.checkEmail(data.email)

            if (nameNotExists && emailNotExists) {
                service.createUser(
                    UserInsert(
                        username = data.username,
                        email = data.email,
                        password = data.password,
                        createAccountDate = data.createAccountDate
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
        val content = call.receiveText()
        val updateData = Json.decodeFromString<PatchUser>(content)

        if (userId.isNotEmpty()) {
            service.updateUsername(userId, updateData.username ?: "")
            call.respondText("User name updated", status = HttpStatusCode.OK)
        } else {
            call.respondText("Error in name update, verify if user Id was correct", status = HttpStatusCode.NotFound)
        }
    }
    patch("/{id}/edit") {
        val userId = call.parameters["id"] ?: ""
        val content = call.receiveText()
        val updateData = Json.decodeFromString<PatchUser>(content)

        if (userId.isNotEmpty()) {
            service.updateUserEmail(userId, updateData.email ?: "")
            call.respondText("User email updated", status = HttpStatusCode.OK)
        } else {
            call.respondText("Error in email update, verify if user Id was correct", status = HttpStatusCode.NotFound)
        }
    }
    patch("/{id}/edit") {
        val userId = call.parameters["id"] ?: ""
        val content = call.receiveText()
        val updateData = Json.decodeFromString<PatchUser>(content)

        if (userId.isNotEmpty()) {
            service.updateUserPassword(userId, updateData.password ?: "")
            call.respondText("User password updated", status = HttpStatusCode.OK)
        } else {
            call.respondText("Error in password update, verify if user Id was correct", status = HttpStatusCode.NotFound)
        }
    }
    delete("/{id}/edit") {
        val userId = call.parameters["id"] ?: ""
        if (userId.isNotEmpty()) {
            service.deleteAccount(userId)
            call.respondText("Account deleted", status = HttpStatusCode.OK)
        } else {
            call.respondText("Error account delete, verify if user Id was correct", status = HttpStatusCode.NotFound)
        }
    }
}
