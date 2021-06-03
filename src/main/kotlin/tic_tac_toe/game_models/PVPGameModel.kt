package tic_tac_toe.game_models

import tic_tac_toe.Position
import tic_tac_toe.StepState

class PVPGameModel : GameModel() {
    override fun move(position: Position) {
        if (gameField.isGameOver) return
        if (markCell(position)) {
            val winnerCellType = gameField.winCheck()
            val stepState = StepState(position, currentCellType, winnerCellType != null, winnerCellType)
            gameController.updateGameState(stepState)
            changeCurrentCellType()
        }
    }
}
