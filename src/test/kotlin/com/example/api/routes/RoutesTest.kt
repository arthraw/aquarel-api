package com.example.api.routes

import com.example.model.dto.userDTO.UserInsert
import com.example.model.dto.userDTO.UserReturns
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert.assertEquals
import org.junit.Test

class RoutesTest {
    @Test
    fun `get source path and return OK`() = testApplication {
        HttpClient().get("http://localhost:8080/v1").apply {
            assertEquals(HttpStatusCode.OK, this.call.response.status)
        }
    }
    @Test
    fun `should return a list of user account`() = testApplication {
        val response = HttpClient().get("http://localhost:8080/v1/users/fbb18a57-fa0f-406f-a9b4-26c9eb8375c7").apply {
            assertEquals(HttpStatusCode.OK, this.call.response.status)
        }
        val gson = Gson()
        val expectedResponse = gson.toJson(listOf(UserReturns(id = "fbb18a57-fa0f-406f-a9b4-26c9eb8375c7", username = "etevaldo")))

        assertEquals(expectedResponse.toString() , response.call.response.bodyAsText())
    }
    @Test
    fun `should return user id and username`() = testApplication {
        val response = HttpClient().get("http://localhost:8080/v1/users/14c96d93-8d34-49bc-9968-9e1b6e2d756d").apply {
            assertEquals(HttpStatusCode.OK, this.call.response.status)
        }
        val json = response.call.response.bodyAsText().replace("[","").replace("]","")
        val gson = Gson()
        val expectedResponse = gson.fromJson(json, UserReturns::class.java)

        assertEquals(UserReturns(id = expectedResponse.id, username = expectedResponse.username), expectedResponse)
    }
    @Test
    fun `post a user and return Unit`() = testApplication {
        val response = client.post("http://localhost:8080/v1/users/14c96d93-8d34-49bc-9968-9e1b6e2d756d/edit") {
            contentType(ContentType.Application.Json)
            setBody(UserInsert(username = "aprendiz", email = "aprendiz@teste.com", password = "aprendiz123"))
        }
        assertEquals("User created", response.bodyAsText())
        assertEquals(HttpStatusCode.Created, response.status)
    }
}