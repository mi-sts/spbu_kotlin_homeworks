package tic_tac_toe.views

import javafx.scene.text.FontWeight
import tornadofx.Stylesheet
import tornadofx.cssclass
import tornadofx.label
import tornadofx.px

class GameStyle : Stylesheet() {
    companion object {
        val fieldStyle by cssclass()
        val menuStyle by cssclass()
    }
    init {
        fieldStyle {
            button {
                fontSize = (GameView.CELL_SIZE / 6).px
                fontWeight = FontWeight.EXTRA_BOLD
                fontFamily = "Comic Sans MS"
            }
        }
        menuStyle {
            label {
                fontSize = (GameView.CELL_SIZE / 5).px
                fontFamily = "Comic Sans MS"
            }
            button {
                fontSize = (GameView.CELL_SIZE / 5).px
                fontFamily = "Comic Sans MS"
            }
        }
    }
}
