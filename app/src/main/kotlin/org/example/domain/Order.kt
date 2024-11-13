package org.example.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.example.web.dto.CreateOrderRequest
import org.example.web.dto.Item
import org.example.web.dto.OrderStatus
import kotlinx.serialization.Serializable

@Serializable
class Order private constructor(
    val orderId: Int,
    val items: List<Item>,
    val orderDate: LocalDate,
    val totalAmount: Double,
    val status: OrderStatus
) {

    companion object {
        fun create(orderRequest: CreateOrderRequest): Order {
            return Order(
                orderId = 1,
                items = orderRequest.items,
                orderDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
                totalAmount = orderRequest.items.sumOf { it.price * it.quantity },
                status = OrderStatus.PENDING,
            )
        }
    }

    override fun hashCode(): Int {
        var result = orderId
        result = 31 * result + items.hashCode()
        result = 31 * result + orderDate.hashCode()
        result = 31 * result + totalAmount.hashCode()
        result = 31 * result + status.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as org.example.domain.Order

        if (orderId != other.orderId) return false
        if (items != other.items) return false
        if (orderDate != other.orderDate) return false
        if (totalAmount != other.totalAmount) return false
        if (status != other.status) return false

        return true
    }
}
