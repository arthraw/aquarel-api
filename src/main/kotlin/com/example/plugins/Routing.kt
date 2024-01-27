package com.example.plugins

import com.example.routes.user.userRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        sourcePath()
        route("/v1") {
            get {
                call.respondText("foi")
            }
            route("/user") {
                userRoutes()
            }

        }
    }
}

fun Route.sourcePath() {
    get("/") {
        call.respondRedirect("http://localhost:8080/v1")
    }
}
