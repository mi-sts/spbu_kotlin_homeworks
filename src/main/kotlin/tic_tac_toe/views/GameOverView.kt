package tic_tac_toe.views

import javafx.geometry.Pos
import tornadofx.*

class GameOverView(private val gameOverText: String) : Fragment("GAME OVER") {
    companion object {
        private const val MENU_WIDTH = GameView.CELL_SIZE * 3.2
        private const val MENU_HEIGHT = GameView.CELL_SIZE * 1
    }

    override val root = vbox {
        addClass(GameStyle.menuStyle)
        alignment = Pos.BOTTOM_CENTER
        setPrefSize(MENU_WIDTH, MENU_HEIGHT)
        label(gameOverText)
        button("Menu") {
            action { onMenuButtonPressed() }
            useMaxWidth = true
        }
    }

    private fun onMenuButtonPressed() {
        close()
        find<MenuView>().openWindow()
    }
}
