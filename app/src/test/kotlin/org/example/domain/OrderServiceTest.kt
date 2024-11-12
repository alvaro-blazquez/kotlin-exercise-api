package org.example.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import kotlinx.datetime.LocalDate
import org.example.web.dto.Item
import org.example.web.dto.OrderStatus

class OrderServiceTest : FunSpec({

    test("it should return the correct order given its id") {
        val orders: List<Order> = listOf(
            Order(
                orderId = 1,
                items = listOf(
                    Item(
                        productId = "prod1",
                        quantity = 2,
                        price = 200.0
                    )
                ),
                orderDate = LocalDate(2022, 1, 1),
                totalAmount = 400.0,
                status = OrderStatus.PENDING
            ),
            Order(
                orderId = 2,
                items = listOf(
                    Item(
                        productId = "prod2",
                        quantity = 4,
                        price = 200.0
                    )
                ),
                orderDate = LocalDate(2022, 1, 1),
                totalAmount = 800.0,
                status = OrderStatus.PENDING
            )
        )

        val orderService = OrderService(orders)

        val order: Order = orderService.getOrderById(1)!!

        order shouldBeEqual Order(
            orderId = 1,
            items = listOf(
                Item(
                    productId = "prod1",
                    quantity = 2,
                    price = 200.0
                )
            ),
            orderDate = LocalDate(2022, 1, 1),
            totalAmount = 400.0,
            status = OrderStatus.PENDING
        )
    }

})