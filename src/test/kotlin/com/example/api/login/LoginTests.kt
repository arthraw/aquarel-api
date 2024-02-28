package com.example.api.login

import com.example.api.fake_model.fake_user.FakeUser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class LoginTests {
    @Test
    fun `should return login route`() = testApplication {
        val post = HttpClient().post("http://localhost:8080/v1/login")
        post.call.body<FakeUser>()
        assertEquals(HttpStatusCode.OK, post.status)
    }
}