@file:Suppress("MagicNumber")
package tic_tac_toe

import javafx.scene.Parent
import javafx.scene.text.FontWeight
import tornadofx.App
import tornadofx.View
import tornadofx.Fragment
import tornadofx.vbox
import tornadofx.hbox
import tornadofx.button
import tornadofx.label
import tornadofx.Stylesheet
import tornadofx.px
import tornadofx.text
import tornadofx.launch

class TicTacApp : App(GameView::class, GameStyle::class)

class GameView : View("Tic-tac-toe") {
    companion object {
        private var fieldView = GameFieldView()

        fun showGameOverScreen(winnerName: String) = GameOverView(winnerName, fieldView).openWindow()
    }

    override val root = fieldView.root
}

class GameFieldView : Fragment("Tic-tac-toe") {
    companion object {
        private const val CELL_SIZE = 100.0
    }
    override var root = getInitializedField()

    private fun getInitializedField() =
        vbox {
            setPrefSize(CELL_SIZE * 3, CELL_SIZE * 3)
            for (y in GameController.FIELD_RANGE)
                hbox {
                    for (x in GameController.FIELD_RANGE)
                        button(" ") {
                            setPrefSize(CELL_SIZE, CELL_SIZE)
                            setOnAction {
                                val markedCellText = GameController.markCell(x, y)
                                if (markedCellText != null) text = markedCellText
                            }
                        }
                }
        }

    fun clearField() {
        root.clear()
        root = getInitializedField()
    }
}

class GameOverView(
    private val gameOverText: String,
    private val gameFieldView: GameFieldView
) : View("Game over") {
    companion object {
        private const val GAME_OVER_SCREEN_WIDTH = 120.0
        private const val GAME_OVER_SCREEN_HEIGHT = 100.0
    }

    override val root: Parent = vbox {
        label(gameOverText) {
            setPrefSize(GAME_OVER_SCREEN_WIDTH, GAME_OVER_SCREEN_HEIGHT)
        }
        button {
            setPrefSize(GAME_OVER_SCREEN_WIDTH / 4.0, GAME_OVER_SCREEN_HEIGHT / 4.0)
            text("Restart") {
                setOnAction {
                    GameController.restart()
                    close()
                    gameFieldView.clearField()
                }
            }
        }
    }
}

class GameStyle : Stylesheet() {
    init {
        button {
            fontSize = 25.px
            fontWeight = FontWeight.EXTRA_BOLD
            fontFamily = "Comic Sans MS"
        }
        label {
            fontSize = 20.px
            fontWeight = FontWeight.EXTRA_BOLD
            fontFamily = "Comic Sans MS"
        }
    }
}

fun main(args: Array<String>) {
    launch<TicTacApp>(args)
}
