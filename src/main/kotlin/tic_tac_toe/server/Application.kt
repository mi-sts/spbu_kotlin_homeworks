package tic_tac_toe.server

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*

fun Application.module() {
    install(WebSockets)
    routing {
        val connections = mutableListOf<DefaultWebSocketSession>()

        webSocket("/game") {
            connections.add(this)
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                connections.forEach {
                    if (it != this) {
                        it.send(receivedText)
                    }
                }
            }
            connections.remove(this)
        }
    }
}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
