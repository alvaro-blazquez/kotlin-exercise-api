package org.example

import io.kotest.assertions.json.*
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.web.webModule

class PostOrderRouteTest : FunSpec({

    val order = """
        {
            "items": [
              {
                "productId": "prod_001",
                "quantity": 2,
                "price": 15.50
              },
              {
                "productId": "prod_002",
                "quantity": 1,
                "price": 45.00
              }
            ]
        }
    """.trimIndent()

    val currentDateTime: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    test("post /orders with a new order should return the created order in a 201 response") {
        testApplication {
            application {
                webModule()
            }
            val response = client.post("/orders") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(order)
            }

            response shouldHaveStatus HttpStatusCode.Created
            response.body<String>() shouldEqualJson """
                    {
                        "orderId": 1,
                        "items": [
                          {
                            "productId": "prod_001",
                            "quantity": 2,
                            "price": 15.5
                          },
                          {
                            "productId": "prod_002",
                            "quantity": 1,
                            "price": 45.0
                          }
                        ],
                        "orderDate": "$currentDateTime",
                        "status": "PENDING",
                        "totalAmount": 76.00
                    }
                """.trimIndent()
        }
    }
})