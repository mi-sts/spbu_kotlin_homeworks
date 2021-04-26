package tic_tac_toe.views

import javafx.geometry.Pos
import tic_tac_toe.controllers.GameOverController
import tornadofx.*

class GameOverView(private val gameOverText: String) : Fragment("GAME OVER") {
    companion object {
        private const val MENU_WIDTH = GameView.CELL_SIZE * 1.8
        private const val MENU_HEIGHT = GameView.CELL_SIZE * 0.5
    }
    private val gameOverController = GameOverController(this)

    override val root = vbox {
        addClass(GameStyle.menuStyle)
        alignment = Pos.BOTTOM_CENTER
        setPrefSize(MENU_WIDTH, MENU_HEIGHT)
        label(gameOverText)
        button("Menu") {
            action { gameOverController.onMenuButtonPressed() }
            useMaxWidth = true
        }
    }
}
