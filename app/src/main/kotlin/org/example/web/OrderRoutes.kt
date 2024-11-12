package org.example.web

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.util.reflect.typeInfo
import org.example.domain.Order

fun Route.orderRoutes() {
    route("/orders") {
        post {
            try {
                val order = call.receive<Order>()
                call.response.status(HttpStatusCode.Created)
                call.respond(order, typeInfo<Order>())
            } catch (e: Exception) {
                e.printStackTrace()
                System.err.println(e.message)
            }
        }
    }

}