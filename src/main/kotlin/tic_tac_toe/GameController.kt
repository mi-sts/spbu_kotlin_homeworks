package tic_tac_toe

import tornadofx.Controller

class GameController(private val gameView: GameView) : Controller() {
    companion object { val FIELD_RANGE = 0..2 }
    private val gameModel = GameModel()

    fun markCell(xPos: Int, yPos: Int) {
        val markedCellType = gameModel.markCell(xPos, yPos)
        gameView.changeCellType(xPos, yPos, markedCellType)
        if (gameModel.isGameOver()) {
            when (gameModel.getWinner()) {
                CellType.NOUGHT -> gameView.openGameOverWindow("Nought Wins!!!")
                CellType.CROSS -> gameView.openGameOverWindow("Cross Wins!!!")
                CellType.EMPTY -> gameView.openGameOverWindow("Tie!")
            }
        }
    }

    fun startNewGame() {
        gameModel.createNewGame()
        gameView.reinitializeView()
    }
}
