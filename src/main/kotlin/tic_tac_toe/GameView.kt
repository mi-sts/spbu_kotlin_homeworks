package tic_tac_toe

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.text.FontWeight
import tic_tac_toe.GameController.Companion.FIELD_RANGE
import tic_tac_toe.GameView.Companion.CELL_SIZE
import tornadofx.*

class TicTacApp : App(GameView::class, GameStyle::class)
class GameView : View("Tic-tac-toe") {
    companion object { const val CELL_SIZE = 150.0 }

    private val gameController = GameController(this)
    private val cells: List<MutableList<Button>> = List(FIELD_RANGE.count()) { mutableListOf() }

    fun changeCellType(xPos: Int, yPos: Int, cellType: CellType?) {
        cellType ?: return
        cells[yPos][xPos].text = when (cellType) {
            CellType.NOUGHT -> "O"
            CellType.CROSS -> "X"
            CellType.EMPTY -> " "
        }
    }

    override val root = vbox {
        setPrefSize(CELL_SIZE * FIELD_RANGE.count(), CELL_SIZE * FIELD_RANGE.count())
        for (y in FIELD_RANGE) {
            hbox {
                for (x in FIELD_RANGE) {
                    cells[y].add(button(" ") {
                        setPrefSize(CELL_SIZE, CELL_SIZE)
                        action { gameController.markCell(x, y) } })
                }
            }
        }
    }

    fun openGameOverWindow(gameOverText: String) {
        val gameOverWindow = GameOverWindow(gameOverText, gameController)
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

class GameOverWindow(
    private val gameOverText: String,
    private val gameController: GameController
) : Fragment("GAME OVER") {
    override val root = vbox {
        alignment = Pos.BOTTOM_CENTER
        setPrefSize(CELL_SIZE * 2, CELL_SIZE * 0.8)
        label(gameOverText)
        button("New Game") {
            action { onNewGameButtonPressed() }
            useMaxWidth = true
        }
    }

    private fun onNewGameButtonPressed() {
        gameController.startNewGame()
        close()
    }

    private fun onWindowClosed() {
        find<GameView>().close()
        close()
    }

    override fun onDock() {
        currentWindow?.setOnCloseRequest { onWindowClosed() }
    }
}

class GameStyle : Stylesheet() {
    init {
        button {
            fontSize = (CELL_SIZE / 6).px
            fontWeight = FontWeight.EXTRA_BOLD
            fontFamily = "Comic Sans MS"
        }
        label {
            fontSize = (CELL_SIZE / 5).px
            fontWeight = FontWeight.EXTRA_BOLD
            fontFamily = "Comic Sans MS"
        }
    }
}

fun main(args: Array<String>) {
    launch<TicTacApp>(args)
}
