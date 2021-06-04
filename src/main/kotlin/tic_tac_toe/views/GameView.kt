package tic_tac_toe.views

import javafx.scene.control.Button
import tic_tac_toe.CellType
import tic_tac_toe.GameField.Companion.FIELD_SIZE
import tic_tac_toe.GameController
import tornadofx.*

class GameView : View("Tic-tac-toe") {
    private val gameController: GameController by inject()

    companion object { const val CELL_SIZE = 150.0 }
    private val cells: List<MutableList<Button>> = List(FIELD_SIZE) { mutableListOf() }

    fun changeCellType(xPos: Int, yPos: Int, cellType: CellType?) {
        cellType ?: return
        cells[yPos][xPos].text = when (cellType) {
            CellType.NOUGHT -> "O"
            CellType.CROSS -> "X"
            CellType.EMPTY -> " "
        }
    }

    override val root = vbox {
        addClass(GameStyle.fieldStyle)
        setPrefSize(CELL_SIZE * FIELD_SIZE, CELL_SIZE * FIELD_SIZE)
        for (y in 0 until FIELD_SIZE) {
            hbox {
                for (x in 0 until FIELD_SIZE) {
                    cells[y].add(button(" ") {
                        setPrefSize(CELL_SIZE, CELL_SIZE)
                        action { gameController.markCell(x, y) } })
                }
            }
        }
    }

    fun openGameOverWindow(gameOverText: String) {
        val gameOverWindow = GameOverView(gameOverText)
        gameOverWindow.openWindow()
    }

    private fun clearCellField() {
        for (x in 0 until FIELD_SIZE)
            for (y in 0 until FIELD_SIZE)
                changeCellType(x, y, CellType.EMPTY)
    }

    fun reinitializeView() {
        clearCellField()
    }
}
