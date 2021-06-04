package tic_tac_toe

import tic_tac_toe.views.GameStyle
import tic_tac_toe.views.MenuView
import tornadofx.App
import tornadofx.launch

class TicTacToe : App(MenuView::class, GameStyle::class)

fun main(args: Array<String>) {
    launch<TicTacToe>(args)
}
