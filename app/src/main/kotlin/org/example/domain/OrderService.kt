package org.example.domain

import org.example.web.dto.CreateOrderRequest

class OrderService(orders: List<Order> = listOf()) {
    val orders: MutableList<Order> = orders.toMutableList()

    fun getOrderById(orderId: Int): Order? = orders.find { it.orderId == orderId }

    fun saveOrder(orderRequest: CreateOrderRequest): Order {
        val order = Order.create(orderRequest)
        orders.add(order)
        return order
    }

    fun getAllOrders(): List<Order> = orders.toList()
}
