package tic_tac_toe.controllers

import javafx.util.Duration
import tic_tac_toe.CellType
import tic_tac_toe.GameModel
import tic_tac_toe.Position
import tic_tac_toe.bots.Bot
import tic_tac_toe.bots.HardBot
import tic_tac_toe.bots.SimpleBot
import tic_tac_toe.views.GameView
import tornadofx.Controller
import tornadofx.runLater
import java.lang.IllegalArgumentException
import kotlin.NullPointerException

class GameController(private val gameView: GameView) : Controller() {
    companion object {
        private const val BOT_THINKING_DELAY = 750.0
        private const val GAME_OVER_DELAY = 1000.0
    }
    private enum class PlayerType {
        PLAYER, BOT
    }

    enum class BotType {
        SIMPLE, HARD
    }

    private lateinit var currentPlayerType: PlayerType
    private val gameModel = GameModel()
    private lateinit var bot: Bot
    private lateinit var playerCellType: CellType
    private lateinit var botCellType: CellType

    private fun markCell(xPos: Int, yPos: Int): CellType? {
        val markedCellType = gameModel.markCell(Position(xPos, yPos))
        gameView.changeCellType(xPos, yPos, markedCellType)

        return markedCellType
    }

    fun playerStep(xPos: Int, yPos: Int) {
        if (currentPlayerType != PlayerType.PLAYER) return
        markCell(xPos, yPos) ?: return
        changeTurn()
    }

    private fun botStep() {
        if (currentPlayerType != PlayerType.BOT) return
        val botMarkPosition = bot.makeDecision()
        require(botMarkPosition != null) { NullPointerException("The bot decision is null!") }
        runLater(Duration(BOT_THINKING_DELAY)) {
            markCell(botMarkPosition.x, botMarkPosition.y)
            changeTurn()
        }
    }

    private fun changeTurn() {
        if (gameModel.isGameOver()) {
            onGameOver()
            return
        }

        when (currentPlayerType) {
            PlayerType.PLAYER -> {
                currentPlayerType = PlayerType.BOT
                botStep()
            }
            PlayerType.BOT -> currentPlayerType = PlayerType.PLAYER
        }
    }

    private fun onGameOver() {
        runLater(Duration(GAME_OVER_DELAY)) {
            gameView.close()
            when (gameModel.getWinner()) {
                playerCellType -> gameView.openGameOverWindow("YOU WIN!")
                botCellType -> gameView.openGameOverWindow("YOU LOSE!")
                CellType.EMPTY -> gameView.openGameOverWindow("TIE!")
                else -> throw IllegalArgumentException("Player and bot cell types are the same!")
            }
        }
    }

    fun startNewGame(playerCellType: CellType, botType: BotType) {
        this.playerCellType = playerCellType
        currentPlayerType = if (playerCellType == CellType.CROSS) PlayerType.PLAYER else PlayerType.BOT
        val botCellType: CellType = CellType.oppositeType(playerCellType) ?: throw
        NullPointerException("The bot cell type is null!")
        this.botCellType = botCellType

        bot = when (botType) {
            BotType.SIMPLE -> SimpleBot(botCellType, gameModel)
            BotType.HARD -> HardBot(botCellType, gameModel)
        }
        gameModel.createNewGame()
        gameView.reinitializeView()

        if (playerCellType == CellType.NOUGHT) botStep()
    }
}
