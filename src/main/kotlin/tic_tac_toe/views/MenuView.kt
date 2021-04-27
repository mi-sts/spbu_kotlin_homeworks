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
        private const val MENU_WIDTH = GameView.CELL_SIZE * 3
        private const val MENU_HEIGHT = GameView.CELL_SIZE * 1.5
    }
    private val difficultyToggleGroup = ToggleGroup()
    private lateinit var easyDifficultyButton: ToggleButton
    private lateinit var hardDifficultyButton: ToggleButton
    private val sideToggleGroup = ToggleGroup()
    private lateinit var noughtSideButton: ToggleButton
    private lateinit var crossSideButton: ToggleButton
    private val menuController = MenuController(this)
    private val toggleButtonsSize = Pair(MENU_WIDTH / 4, MENU_HEIGHT / 6)
    private val menuRowSize = Pair(MENU_WIDTH, MENU_HEIGHT / 3)
    private val buttonsSeparatorSize = MENU_HEIGHT / 6

    override val root = borderpane {
        addClass(GameStyle.menuStyle)
        setPrefSize(MENU_WIDTH, MENU_HEIGHT)
        top {
            vbox(buttonsSeparatorSize) {
                borderpane {
                    left { label("Difficulty") }
                    setPrefSize(menuRowSize.first, menuRowSize.second)
                    right {
                        paddingTop = buttonsSeparatorSize
                        hbox {
                            easyDifficultyButton = togglebutton("Easy", difficultyToggleGroup) {
                                setPrefSize(toggleButtonsSize.first, toggleButtonsSize.second)
                            }
                            hardDifficultyButton = togglebutton("Hard", difficultyToggleGroup) {
                                setPrefSize(toggleButtonsSize.first, toggleButtonsSize.second)
                            }
                        }
                    }
                }
                separator()
                borderpane {
                    left { label("Side") }
                    setPrefSize(menuRowSize.first, menuRowSize.second)
                    right {
                        hbox {
                            crossSideButton = togglebutton("Cross", sideToggleGroup) {
                                setPrefSize(toggleButtonsSize.first, toggleButtonsSize.second)
                            }
                            noughtSideButton = togglebutton("Nought", sideToggleGroup) {
                                setPrefSize(toggleButtonsSize.first, toggleButtonsSize.second)
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
