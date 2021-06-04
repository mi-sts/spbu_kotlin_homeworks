package tic_tac_toe.views

import javafx.geometry.Pos
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToggleGroup
import tornadofx.*

@Suppress("MagicNumber")
class MenuView : View("Menu") {
    companion object {
        private const val MENU_WIDTH = GameView.CELL_SIZE * 3
        private const val MENU_HEIGHT = GameView.CELL_SIZE * 1.5
    }

    private val modeToggleGroup = ToggleGroup()
    private lateinit var pvpModeButton: ToggleButton
    private lateinit var pveModeButton: ToggleButton
    private val toggleButtonsSize = Pair(MENU_WIDTH / 4, MENU_HEIGHT / 6)
    private val menuRowSize = Pair(MENU_WIDTH, MENU_HEIGHT / 3)
    private val buttonsSeparatorSize = MENU_HEIGHT / 6

    override val root = borderpane {
        addClass(GameStyle.menuStyle)
        setPrefSize(MENU_WIDTH, MENU_HEIGHT)
        top {
            vbox {
                borderpane {
                    left { label("Game mode") }
                    setPrefSize(menuRowSize.first, menuRowSize.second)
                    right {
                        paddingTop = buttonsSeparatorSize
                        hbox {
                            pvpModeButton = togglebutton("PVP", modeToggleGroup) {
                                setPrefSize(toggleButtonsSize.first, toggleButtonsSize.second)
                            }
                            pveModeButton = togglebutton("PVE", modeToggleGroup) {
                                setPrefSize(toggleButtonsSize.first, toggleButtonsSize.second)
                            }
                        }
                    }
                }
            }
        }
        bottom {
            button("Select") {
                this.alignment = Pos.BOTTOM_CENTER
                useMaxWidth = true
                action { onSelectButtonPressed() }
            }
        }
    }

    private fun onSelectButtonPressed() {
        when (modeToggleGroup.selectedToggle) {
            pvpModeButton -> onPVPModeButtonPressed()
            pveModeButton -> onPVEModeButtonPressed()
        }
    }

    private fun onPVPModeButtonPressed() {
        close()
        find<PVPMenuView>().openWindow()
    }

    private fun onPVEModeButtonPressed() {
        close()
        find<PVEMenuView>().openWindow()
    }
}
