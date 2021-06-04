package tic_tac_toe.game_models.pvp_multiplayer

import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import tic_tac_toe.CellType
import tic_tac_toe.Position
import tic_tac_toe.StepState
import tic_tac_toe.game_models.GameModel
import java.io.FileNotFoundException

class PVPMultiplayerGameModel : GameModel() {
    private var playerCellType: CellType? = null
    private val client = HttpClient { install(WebSockets) }
    private var session: DefaultClientWebSocketSession? = null
    private var isPlayerTurn: Boolean = false
    private lateinit var lastSentData: MultiplayerStepData

    init {
        val updateStateThread = Thread { updateGameState() }
        updateStateThread.start()
        Thread.sleep(CONNECTION_TIME_MILLIS)
    }

    private fun updateGameState() {
        lateinit var connectionConfig: ConnectionConfig
        try {
            val configText = javaClass.getResource("ConnectionConfig.json").readText()
            connectionConfig = Json.decodeFromString<ConnectionConfig>(configText)
        } catch (e: SerializationException) {
            println("Serialize error while decoding the connection config: " + e.localizedMessage)
        } catch (e: FileNotFoundException) {
            println("The connection config file not found!")
        }

        runBlocking {
            client.webSocket(
                method = HttpMethod.Get,
                host = connectionConfig.host,
                port = connectionConfig.port,
                path = connectionConfig.path
            ) {
                session = this
                launch { receiveStepData() }.join()
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.receiveStepData() {
        if (gameField.isGameOver) return

        try {
            for (receivedStateText in incoming) {
                receivedStateText as? Frame.Text ?: continue
                val receivedStateString = receivedStateText.readText()
                println("Received: $receivedStateString")
                if (receivedStateString == RECEIVED_MESSAGE) {
                    onEnemyReceive()
                } else {
                    val stepData = Json.decodeFromString<MultiplayerStepData>(receivedStateString)
                    onStepDataReceived(stepData)
                    launch { session?.send(RECEIVED_MESSAGE) }
                }
            }
        } catch (e: SerializationException) {
            println("Serialize error while receiving: " + e.localizedMessage)
        }
    }

    private suspend fun DefaultClientWebSocketSession.sendStepData(stepData: MultiplayerStepData) {
        try {
            val stepDataString = Json.encodeToString(stepData)
            println("Sent: $stepDataString")
            send(stepDataString)
        } catch (e: SerializationException) {
            println("Serialize error while sending: " + e.localizedMessage)
        }
    }

    private fun onStepDataReceived(stepData: MultiplayerStepData) {
        if (playerCellType == null) playerCellType = CellType.oppositeType(stepData.markedCellType)
        val cellType = playerCellType ?: throw NullPointerException("The player cell type is null!")

        if (!isPlayerTurn && stepData.markedCellType == CellType.oppositeType(cellType)) {
            if (markCell(stepData.position, stepData.markedCellType)) {
                gameController.updateGameState(
                    StepState(stepData.position, stepData.markedCellType, gameField.isGameOver, gameField.winCheck())
                )
                isPlayerTurn = true
            }
        }
    }

    private fun onEnemyReceive() {
        if (markCell(lastSentData.position, lastSentData.markedCellType)) {
            val lastStepState =
                StepState(lastSentData.position, lastSentData.markedCellType, gameField.isGameOver,
                    gameField.winCheck())
            gameController.updateGameState(lastStepState)
            isPlayerTurn = false
        }
    }

    override fun move(position: Position) {
        if (playerCellType == null) {
            playerCellType = CellType.CROSS
            isPlayerTurn = true
        }

        val cellType = playerCellType ?: return
        if (!isPlayerTurn || gameField.isGameOver) return

        lastSentData = MultiplayerStepData(position, cellType)

        runBlocking {
            launch {
                session?.sendStepData(lastSentData)
            }
        }
    }

    companion object {
        private const val RECEIVED_MESSAGE = "RECEIVED"
        private const val CONNECTION_TIME_MILLIS = 15L
    }
}
