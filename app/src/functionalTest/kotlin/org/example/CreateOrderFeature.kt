package org.example

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.time.LocalDate

//Feature: Create Order
//    As an e-commerce platform user
//    I want to be able to create a new order
//    So that I can place orders for customers with detailed information about the items
class CreateOrderFeature : BehaviorSpec({

    val currentDateTime = LocalDate.now()
    val host = "localhost"
    val port = 8080
    val path = "orders"

    // Scenario: Successfully create a new order
    Given("""I have a customer with ID "customer_123" """) {
        And(
            """And I have items with product details
            | product_id | quantity | price |
            | prod_001   | 2        | 15.50 |
            | prod_002   | 1        | 45.00 |
        """.trimMargin()
        ) {
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

            When("""I send a POST request to "/orders" with the order data""") {
                val response: HttpResponse = HttpClient().post("http://$host:$port/$path") {
                    contentType(ContentType.Application.Json)
                    setBody(order)
                }

                Then("I receive a 201 Created status code") {
                    response shouldHaveStatus HttpStatusCode.Created
                }
                And("the response should be the created order response") {
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
        }
    }

})