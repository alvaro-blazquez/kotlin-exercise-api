package org.example.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.web.dto.CreateOrderRequest
import org.example.web.dto.Item
import org.example.web.dto.OrderStatus

class OrderServiceTest : FunSpec({

    val orders: List<Order> = listOf(
        mockk<Order> {
            every { orderId } returns 1
            every { items } returns listOf<Item>(
                Item(
                    productId = "prod1",
                    quantity = 2,
                    price = 200.0,
                )
            )
            every { orderDate } returns LocalDate(2022, 1, 1)
            every { totalAmount } returns 400.0
            every { status } returns OrderStatus.PENDING
        },
        mockk<Order> {
            every { orderId } returns 2
            every { items } returns listOf<Item>(
                Item(
                    productId = "prod2",
                    quantity = 4,
                    price = 200.0,
                )
            )
            every { orderDate } returns LocalDate(2022, 1, 1)
            every { totalAmount } returns 800.0
            every { status } returns OrderStatus.PENDING
        }
    )

    test("it should return the correct order given its id") {
        val orderService = OrderService(orders)

        val order: Order = orderService.getOrderById(1)!!

        order.orderId shouldBe 1
        order.items[0] shouldBeEqual Item(
            productId = "prod1",
            quantity = 2,
            price = 200.0
        )
        order.orderDate shouldBe LocalDate(2022, 1, 1)
        order.totalAmount shouldBe 400.0
        order.status shouldBe OrderStatus.PENDING
    }

    test("it should persist a given order") {
        val orderRequest = CreateOrderRequest(
            items = listOf(
                Item(
                    productId = "prod1",
                    quantity = 2,
                    price = 200.0
                )
            )
        )

        val orderService = OrderService()

        val order: Order = orderService.saveOrder(orderRequest)

        mockk<Order> {
            every { orderId } returns 1
            every { items } returns listOf<Item>(
                Item(
                    productId = "prod1",
                    quantity = 2,
                    price = 200.0,
                )
            )
            every { orderDate } returns Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            every { totalAmount } returns 400.0
            every { status } returns OrderStatus.PENDING
        }

        orderService.getOrderById(order.orderId)!! shouldBeEqual mockk<Order> {
            every { orderId } returns 1
            every { items } returns listOf<Item>(
                Item(
                    productId = "prod1",
                    quantity = 2,
                    price = 200.0,
                )
            )
            every { orderDate } returns Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            every { totalAmount } returns 400.0
            every { status } returns OrderStatus.PENDING
        }
    }

    test("getAllOrders should return all orders") {
        val orderService = OrderService(orders)

        val actualOrders: List<Order> = orderService.getAllOrders()

        actualOrders shouldBeEqual orders
    }
})
