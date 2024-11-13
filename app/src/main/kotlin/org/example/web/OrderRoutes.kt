package org.example.web

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.util.reflect.typeInfo
import org.example.domain.OrderService
import org.example.web.dto.CreateOrderRequest

val orders = mutableListOf<CreateOrderRequest>()

fun Route.orderRoutes(orderService: OrderService) {
    route("/orders") {
        post {
            try {
                val createOrderRequest = call.receive<CreateOrderRequest>()
                orders.add(createOrderRequest)
                call.response.status(HttpStatusCode.Created)
                call.respond(createOrderRequest, typeInfo<CreateOrderRequest>())
            } catch (e: Exception) {
                e.printStackTrace()
                System.err.println(e.message)
            }
        }
        get("/{orderId}") {
            val orderId = call.parameters["orderId"]!!.toInt()
            val order: CreateOrderRequest? = orders.find { it.orderId == orderId }
            call.response.status(HttpStatusCode.OK)
            call.respond(order, typeInfo<CreateOrderRequest>())
        }

    }

}
