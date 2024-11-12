package org.example.web

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.util.reflect.typeInfo
import org.example.domain.Order

val orders = mutableListOf<Order>()

fun Route.orderRoutes() {
    route("/orders") {
        post {
            try {
                val order = call.receive<Order>()
                orders.add(order)
                call.response.status(HttpStatusCode.Created)
                call.respond(order, typeInfo<Order>())
            } catch (e: Exception) {
                e.printStackTrace()
                System.err.println(e.message)
            }
        }
        get("/{orderId}") {
            val orderId = call.parameters["orderId"]!!.toInt()
            val order: Order? = orders.find { it.orderId == orderId }
            call.response.status(HttpStatusCode.OK)
            call.respond(order, typeInfo<Order>())
        }

    }

}