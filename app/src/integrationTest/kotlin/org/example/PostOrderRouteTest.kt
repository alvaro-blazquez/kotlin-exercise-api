package org.example

import io.kotest.assertions.json.*
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.core.spec.style.FunSpec
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.domain.Order
import org.example.domain.OrderService
import org.example.web.dto.Item
import org.example.web.dto.OrderStatus
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

    val orderService = mockk<OrderService>()

    test("post /orders with a new order should return the created order in a 201 response") {
        testApplication {
            application {
                webModule(orderService)
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

    test("get /orders/{orderId} should return a previously created order in a 200 response") {
        testApplication {
            application {
                webModule(orderService)
            }
            client.post("/orders") {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                setBody(order)
            }

            val response = client.get("/orders/1") {
                accept(ContentType.Application.Json)
            }

            response shouldHaveStatus HttpStatusCode.OK
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

    test("get /orders should return all available orders in a 200 response") {

        val orders: List<Order> = listOf(
            mockk<Order> {
                every { orderId } returns 1
                every { items } returns listOf<Item>(
                    Item(
                        productId = "prod_001",
                        quantity = 2,
                        price = 15.5,
                    )
                )
                every { orderDate } returns LocalDate(2022, 1, 1)
                every { totalAmount } returns 31.0
                every { status } returns OrderStatus.PENDING
            },
            mockk<Order> {
                every { orderId } returns 2
                every { items } returns listOf<Item>(
                    Item(
                        productId = "prod_002",
                        quantity = 2,
                        price = 15.5,
                    )
                )
                every { orderDate } returns LocalDate(2022, 1, 1)
                every { totalAmount } returns 31.0
                every { status } returns OrderStatus.PENDING
            }
        )

        every { orderService.getAllOrders() } returns orders

        testApplication {
            application {
                webModule(orderService)
            }

            val response = client.get("/orders") {
                accept(ContentType.Application.Json)
            }

            response shouldHaveStatus HttpStatusCode.OK
            response.body<String>() shouldEqualJson """
                    [
                        {
                            "orderId": 1,
                            "items": [
                              {
                                "productId": "prod_001",
                                "quantity": 2,
                                "price": 15.5
                              }
                            ],
                            "orderDate": "${LocalDate(2022, 1, 1)}",
                            "status": "PENDING",
                            "totalAmount": 31.00
                        },
                        {
                            "orderId": 2,
                            "items": [
                              {
                                "productId": "prod_002",
                                "quantity": 2,
                                "price": 15.5
                              }
                            ],
                            "orderDate": "${LocalDate(2022, 1, 1)}",
                            "status": "PENDING",
                            "totalAmount": 31.00
                        }
                    ]
                """.trimIndent()
            verify(exactly = 1) { orderService.getAllOrders() }
        }
    }
})
