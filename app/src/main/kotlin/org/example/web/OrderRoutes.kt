package org.example.web

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.util.reflect.typeInfo
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.example.domain.Order
import org.example.domain.OrderService
import org.example.web.dto.CreateOrderRequest

val orders = mutableListOf<CreateOrderRequest>()

@Serializable
data class NotFoundError(val statusCode: Int, val message: String)

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
            if(order != null) {
                call.response.status(HttpStatusCode.OK)
                call.respond(order, typeInfo<CreateOrderRequest>())
            } else {
                call.response.status(HttpStatusCode.NotFound)
                call.respond(
                    NotFoundError(
                        HttpStatusCode.NotFound.value,
                        "Order with orderId $orderId not found"
                    ), typeInfo<NotFoundError>()
                )
            }
        }
        get {
            if (call.request.queryParameters.contains("date")) {
                val filterDate = LocalDate.parse(call.request.queryParameters["date"]!!)
                val orders = orderService.getOrdersByDate(filterDate)
                call.response.status(HttpStatusCode.OK)
                call.respond(orders, typeInfo<List<Order>>())
            } else {
                val orders = orderService.getAllOrders()
                call.response.status(HttpStatusCode.OK)
                call.respond(orders, typeInfo<List<Order>>())
            }
        }

    }

}
