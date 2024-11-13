package org.example

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.example.web.webModule

fun main() {
    embeddedServer(Netty, port = 8080) {
        webModule()
    }.start(wait = true)
}
