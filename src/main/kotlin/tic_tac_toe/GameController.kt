package tic_tac_toe

import javafx.util.Duration
import tic_tac_toe.views.GameView
import tic_tac_toe.game_models.GameModel
import tic_tac_toe.game_models.PVEHardGameModel
import tic_tac_toe.game_models.PVESimpleGameModel
import tic_tac_toe.game_models.PVPGameModel
import tic_tac_toe.game_models.pvp_multiplayer.PVPMultiplayerGameModel
import tornadofx.Controller
import tornadofx.runLater

data class StepState(
    val position: Position,
    val markedCellType: CellType,
    val isGameOver: Boolean,
    val winner: CellType?
)

enum class GameMode {
    PVP, PVP_MULTIPLAYER, PVE_SIMPLE, PVE_HARD
}

class GameController : Controller() {
    companion object {
        private const val GAME_OVER_DELAY = 1000.0
    }
    private lateinit var gameModel: GameModel

    private val gameView = find<GameView>()

    fun startNewGame(gameMode: GameMode, playerCellType: CellType?) {
        if (gameMode == GameMode.PVP || gameMode == GameMode.PVP_MULTIPLAYER) {
            if (gameMode == GameMode.PVP) gameModel = PVPGameModel()
            else if (gameMode == GameMode.PVP_MULTIPLAYER) gameModel = PVPMultiplayerGameModel()
        } else {
            require(playerCellType != null) { NullPointerException("The player cell type is null!") }
            if (gameMode == GameMode.PVE_SIMPLE) gameModel = PVESimpleGameModel(playerCellType)
            else if (gameMode == GameMode.PVE_HARD) gameModel = PVEHardGameModel(playerCellType)
        }
    }

    fun markCell(xPos: Int, yPos: Int) = gameModel.move(Position(xPos, yPos))

    fun updateGameState(stepState: StepState) {
        runLater { gameView.changeCellType(stepState.position.x, stepState.position.y, stepState.markedCellType) }
        if (stepState.isGameOver) {
            require(stepState.winner != null) { NullPointerException("The winner is null!") }
            onGameOver(stepState.winner)
        }
    }

    private fun onGameOver(winner: CellType) {
        runLater(Duration(GAME_OVER_DELAY)) {
            gameView.close()
            when (winner) {
                CellType.NOUGHT -> gameView.openGameOverWindow("NOUGHT WINS!")
                CellType.CROSS -> gameView.openGameOverWindow("CROSS WINS!")
                CellType.EMPTY -> gameView.openGameOverWindow("TIE!")
            }
        }
    }
}
