package com.example.api

import io.ktor.server.testing.*

object TestsModule {
     fun MainModule() = testApplication {
         application {
             RoutesTest()
         }
     }
}