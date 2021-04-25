package tic_tac_toe

import tic_tac_toe.bots.HardBot
import tornadofx.Controller
import java.lang.NullPointerException
import kotlin.random.Random

class GameController(private val gameView: GameView) : Controller() {
    private enum class PlayerType {
        PLAYER, BOT
    }

    private var currentPlayerType = PlayerType.PLAYER
    private val gameModel = GameModel()
    private val bot = HardBot(CellType.NOUGHT, gameModel)

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
        markCell(botMarkPosition.x, botMarkPosition.y)
        changeTurn()
    }

    private fun changeTurn() {
        if (gameModel.isGameOver()) {
            print(Random.nextInt())
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

    fun onGameOver() {
        when (gameModel.getWinner()) {
            CellType.NOUGHT -> gameView.openGameOverWindow("Nought Wins!!!")
            CellType.CROSS -> gameView.openGameOverWindow("Cross Wins!!!")
            CellType.EMPTY -> gameView.openGameOverWindow("Tie!")
        }
    }

    fun startNewGame() {
        currentPlayerType = PlayerType.PLAYER
        gameModel.createNewGame()
        gameView.reinitializeView()
    }
}
