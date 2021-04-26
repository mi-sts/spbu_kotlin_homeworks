package tic_tac_toe.views

import javafx.scene.control.Button
import tic_tac_toe.CellType
import tic_tac_toe.controllers.GameController
import tic_tac_toe.GameModel.Companion.FIELD_RANGE
import tornadofx.*

class GameView : View("Tic-tac-toe") {
    companion object { const val CELL_SIZE = 150.0 }
    private val cells: List<MutableList<Button>> = List(FIELD_RANGE.count()) { mutableListOf() }
    private val gameController = GameController(this)

    fun changeCellType(xPos: Int, yPos: Int, cellType: CellType?) {
        cellType ?: return
        cells[yPos][xPos].text = when (cellType) {
            CellType.NOUGHT -> "O"
            CellType.CROSS -> "X"
            CellType.EMPTY -> " "
        }
    }

    fun initilizeGame(playerCellType: CellType, botType: GameController.BotType) {
        gameController.startNewGame(playerCellType, botType)
    }

    override val root = vbox {
        addClass(GameStyle.fieldStyle)
        setPrefSize(CELL_SIZE * FIELD_RANGE.count(), CELL_SIZE * FIELD_RANGE.count())
        for (y in FIELD_RANGE) {
            hbox {
                for (x in FIELD_RANGE) {
                    cells[y].add(button(" ") {
                        setPrefSize(CELL_SIZE, CELL_SIZE)
                        action { gameController.playerStep(x, y) } })
                }
            }
        }
    }

    fun openGameOverWindow(gameOverText: String) {
        val gameOverWindow = GameOverView(gameOverText)
        gameOverWindow.openWindow()
    }

    private fun clearCellField() {
        for (x in FIELD_RANGE)
            for (y in FIELD_RANGE)
                changeCellType(x, y, CellType.EMPTY)
    }

    fun reinitializeView() {
        clearCellField()
    }
}
