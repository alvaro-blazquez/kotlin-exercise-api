package org.example.domain

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val items: List<Item>,
) {
    val orderId: Int = 1
    val orderDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    val totalAmount: Double = items.sumOf { it.price * it.quantity }
    val status: OrderStatus = OrderStatus.PENDING
}

@Serializable
data class Item(
    val productId: String,
    val quantity: Int,
    val price: Double
)

enum class OrderStatus {
    PENDING,
}