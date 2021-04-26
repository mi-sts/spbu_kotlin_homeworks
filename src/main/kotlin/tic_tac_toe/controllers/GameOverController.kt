package tic_tac_toe.controllers

import tic_tac_toe.views.GameOverView
import tic_tac_toe.views.MenuView
import tornadofx.Controller

class GameOverController(private val gameOverView: GameOverView) : Controller() {
    fun onMenuButtonPressed() {
        gameOverView.close()
        find<MenuView>().openWindow()
    }
}
