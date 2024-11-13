package org.example.web

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import org.example.domain.OrderService

fun Application.webModule(orderService: OrderService) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        orderRoutes(orderService)
    }
}
