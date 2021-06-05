package tic_tac_toe.game_models

import tic_tac_toe.CellType
import tic_tac_toe.GameField
import tic_tac_toe.Position
import tic_tac_toe.GameController
import tornadofx.find

abstract class GameModel {
    protected val gameController = find<GameController>()
    protected var currentCellType = CellType.CROSS
    protected var gameField: GameField = GameField()

    abstract fun move(position: Position)

    protected fun markCell(position: Position, cellType: CellType): Boolean =
        gameField.setCell(position, cellType)

    protected fun changeCurrentCellType() {
        currentCellType = CellType.oppositeType(currentCellType) ?: throw
        IllegalArgumentException("The empty cell doesn't have the opposite cell type!")
    }

    protected fun createNewGame() {
        currentCellType = CellType.CROSS
        gameField = GameField()
    }
}
