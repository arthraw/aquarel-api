package com.example.plugins

import com.example.model.dto.notFound.NotFoundResponse
import com.example.routes.user.loginRoute
import com.example.routes.user.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        sourcePath()
        route("/v1") {
            get {
                call.respond(NotFoundResponse(message = "Nothing to see here", status = 404))
            }
            route("/users") {
                userRoutes()
            }
            route("/login") {
                loginRoute()
            }

        }
    }
}

fun Route.sourcePath() {
    get("/") {
        call.respondRedirect("http://localhost:8080/v1")
    }
}
