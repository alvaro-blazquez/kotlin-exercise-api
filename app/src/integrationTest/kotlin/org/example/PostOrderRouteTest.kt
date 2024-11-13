package org.example

import io.kotest.assertions.json.*
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*

class PostOrderRouteTest : FunSpec({

    test("sample integration test") {
        testApplication {
            application {
                TODO("add app module")
            }
            val response = client.post("/")

            response shouldHaveStatus HttpStatusCode.Created
            response.body<String>() shouldEqualJson """
            
                """.trimIndent()
        }
    }
})
