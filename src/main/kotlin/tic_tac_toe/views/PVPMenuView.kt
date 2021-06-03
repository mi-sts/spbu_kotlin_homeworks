package tic_tac_toe.views

import javafx.geometry.Pos
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import tic_tac_toe.GameController
import tic_tac_toe.GameMode
import tornadofx.*

@Suppress("MagicNumber")
class PVPMenuView : View("Menu") {
    companion object {
        private const val MENU_WIDTH = GameView.CELL_SIZE * 3
        private const val MENU_HEIGHT = GameView.CELL_SIZE * 1.5
    }

    private val pvpModeToggleGroup = ToggleGroup()
    private lateinit var defaultModeButton: ToggleButton
    private val toggleButtonsSize = Pair(MENU_WIDTH / 4, MENU_HEIGHT / 6)
    private val menuRowSize = Pair(MENU_WIDTH, MENU_HEIGHT / 3)
    private val buttonsSeparatorSize = MENU_HEIGHT / 6

    override val root = borderpane {
        addClass(GameStyle.menuStyle)
        setPrefSize(MENU_WIDTH, MENU_HEIGHT)
        top {
            vbox {
                borderpane {
                    left { label("PVP Mode") }
                    setPrefSize(menuRowSize.first, menuRowSize.second)
                    right {
                        paddingTop = buttonsSeparatorSize
                        hbox {
                            defaultModeButton = togglebutton("Default", pvpModeToggleGroup) {
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
        close()
        val gameView = find<GameView>()
        gameView.openWindow()
        gameView.reinitializeView()
        initializeGameMode()
    }

    private fun initializeGameMode() {
        val gameController = find<GameController>()
        val gameMode = when (pvpModeToggleGroup.selectedToggle) {
            defaultModeButton -> GameMode.PVP
            else -> throw NullPointerException("The selected mode is null!")
        }

        gameController.startNewGame(gameMode, null)
    }
}
