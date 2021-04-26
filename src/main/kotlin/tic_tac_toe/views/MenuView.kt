package tic_tac_toe.views

import javafx.geometry.Pos
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import tic_tac_toe.CellType
import tic_tac_toe.controllers.GameController
import tic_tac_toe.controllers.MenuController
import tornadofx.*

class MenuView : View("Menu") {
    companion object {
        private const val MENU_WIDTH = GameView.CELL_SIZE * 1.5
        private const val MENU_HEIGHT = GameView.CELL_SIZE * 0.75
    }
    private val difficultyToggleGroup = ToggleGroup()
    lateinit var easyDifficultyButton: ToggleButton
    lateinit var hardDifficultyButton: ToggleButton
    private val sideToggleGroup = ToggleGroup()
    lateinit var noughtSideButton: ToggleButton
    lateinit var crossSideButton: ToggleButton
    private val menuController = MenuController(this)

    override val root = borderpane {
        addClass(GameStyle.menuStyle)
        setPrefSize(MENU_WIDTH, MENU_HEIGHT)
        center {
            vbox(MENU_HEIGHT / 15) {
                borderpane {
                    left { label("Difficulty") }
                    right {
                        hbox {
                            easyDifficultyButton = togglebutton("Easy", difficultyToggleGroup) {
                                setPrefSize(MENU_WIDTH / 4, height)
                            }
                            hardDifficultyButton = togglebutton("Hard", difficultyToggleGroup) {
                                setPrefSize(MENU_WIDTH / 4, height)
                            }
                        }
                    }
                }
                borderpane {
                    left { label("Side") }
                    right {
                        hbox {
                            crossSideButton = togglebutton("Cross", sideToggleGroup) {
                                setPrefSize(MENU_WIDTH / 4, height)
                            }
                            noughtSideButton = togglebutton("Nought", sideToggleGroup) {
                                setPrefSize(MENU_WIDTH / 4, height)
                            }
                        }
                    }
                }
            }
        }
        bottom {
            button("Start") {
                this.alignment = Pos.BOTTOM_CENTER
                useMaxWidth = true
                action { onStartButtonPressed() }
            }
        }
    }

    private fun onStartButtonPressed() {
        val sideCellType = if (sideToggleGroup.selectedToggle == noughtSideButton) CellType.NOUGHT else CellType.CROSS
        val botType =
            if (difficultyToggleGroup.selectedToggle == easyDifficultyButton) GameController.BotType.SIMPLE
            else GameController.BotType.HARD
        menuController.onStartButtonPressed(sideCellType, botType)
    }
}
