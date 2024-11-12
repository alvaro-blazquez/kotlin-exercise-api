package org.example.domain

class OrderService(val orders: List<Order>) {

    fun getOrderById(orderId: Int): Order? = orders.find { it.orderId == orderId }
}