package org.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.example.domain.OrderService
import org.example.web.webModule

fun main() {
    val orderService = OrderService()
    embeddedServer(Netty, port = 8080) {
        webModule(orderService)
    }.start(wait = true)
}
