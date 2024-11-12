package org.example.domain

import kotlinx.datetime.LocalDate
import org.example.web.dto.Item
import org.example.web.dto.OrderStatus

data class Order(
    val orderId: Int,
    val items: List<Item>,
    val orderDate: LocalDate,
    val totalAmount: Double,
    val status: OrderStatus
)