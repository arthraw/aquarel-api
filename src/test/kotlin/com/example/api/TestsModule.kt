package com.example.api

import com.example.api.routes.RoutesTest
import io.ktor.server.testing.*

object TestsModule {
     fun MainModule() = testApplication {
         application {
             RoutesTest()
         }
     }
}