package tic_tac_toe.Bots

import tic_tac_toe.CellType
import tic_tac_toe.GameModel
import tic_tac_toe.GameModel.Companion.FIELD_RANGE
import tic_tac_toe.Position

abstract class Bot(private val botCellType: CellType, private val gameModel: GameModel) {
    private fun getCellsNumber(): Map<CellType, Int> {
        val cellsNumber = mutableMapOf<CellType, Int>()
        for (x in FIELD_RANGE)
            for (y in FIELD_RANGE) {
                val cell = gameModel.getCell(Position(x, y))
                require(cell != null) { IndexOutOfBoundsException("The cell does not exist!") }
                cellsNumber[cell] = cellsNumber.getOrPut(cell) { 0 } + 1
            }
        for (cellType in CellType.values()) cellsNumber[cellType] = cellsNumber.getOrPut(cellType) { 0 }

        return cellsNumber
    }

    protected fun getFreePositions(): List<Position> {
        val freePositions = mutableListOf<Position>()
        for (x in FIELD_RANGE)
            for (y in FIELD_RANGE) {
                val position = Position(x, y)
                if (gameModel.getCell(position) == CellType.EMPTY) freePositions.add(position)
            }

        return freePositions
    }

    protected fun isBotStep(): Boolean {
        val cellsNumber = getCellsNumber()
        require(CellType.values().any { it in cellsNumber.keys }) {
            NullPointerException("The cell type number is null!")
        }

        return when (botCellType) {
            CellType.CROSS -> (cellsNumber[CellType.CROSS]!! - cellsNumber[CellType.NOUGHT]!!) == 0
            CellType.NOUGHT -> (cellsNumber[CellType.CROSS]!! - cellsNumber[CellType.NOUGHT]!!) == 1
            else -> false
        }
    }

    abstract fun makeDecision(): Position?
}