package tic_tac_toe.controllers

import tic_tac_toe.CellType
import tic_tac_toe.views.GameView
import tic_tac_toe.views.MenuView
import tornadofx.Controller

class MenuController(private val menuView: MenuView) : Controller() {
    fun onStartButtonPressed(playerCellType: CellType, botType: GameController.BotType) {
        menuView.close()
        val gameView = GameView()
        gameView.openWindow()
        gameView.initilizeGame(playerCellType, botType)
    }
}
