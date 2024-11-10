package org.example

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import java.time.LocalDate

class PostOrderRouteTest: FunSpec({

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

    val currentDateTime = LocalDate.now()

    test("post /orders with a new order should return the created order in a 201 response") {
        testApplication {
            application {
                webModule()
            }
            val response = client.post("/orders") {
                contentType(ContentType.Application.Json)
                setBody("{\"status\":\"success\"}")
            }

            response shouldHaveStatus HttpStatusCode.Created
            response.body<String>() shouldEqualJson """
                    {
                        "orderId": 1,
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
                        ],
                        "status": "PENDING",
                        "orderDate": "$currentDateTime",
                        "totalAmount": 76.00
                    }
                """.trimIndent()
        }
    }
})