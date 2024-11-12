package org.example.domain

class OrderService(orders: List<Order>) {
    val orders: MutableList<Order> = orders.toMutableList()

    fun getOrderById(orderId: Int): Order? = orders.find { it.orderId == orderId }
}