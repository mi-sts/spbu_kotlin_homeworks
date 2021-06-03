package tic_tac_toe.game_models

import tic_tac_toe.CellType
import tic_tac_toe.GameField.Companion.FIELD_SIZE
import tic_tac_toe.Position
import java.lang.IllegalArgumentException

class PVEHardGameModel(playerCellType: CellType) : PVEGameModel(playerCellType) {
    private enum class GameStrategy {
        DEFENSIVE_INITIAL, DEFENSIVE_FINAL, OFFENSIVE
    }

    private var currentStrategy =
        if (botCellType == CellType.NOUGHT) GameStrategy.DEFENSIVE_INITIAL else GameStrategy.OFFENSIVE

    private val enemyCellType = if (botCellType == CellType.NOUGHT) CellType.CROSS else CellType.NOUGHT

    private fun isWinPosition(cellLine: List<CellType>, winnerCellType: CellType): Boolean {
        require(cellLine.count() == FIELD_SIZE) {
            IllegalArgumentException("Incorrect size of the cell line!")
        }
        return cellLine.count { it == winnerCellType } == (FIELD_SIZE - 1) && cellLine.contains(CellType.EMPTY)
    }

    private fun findWinPosition(winnerCellType: CellType): Position? {
        var winPosition: Position? = null
        for (i in 0 until FIELD_SIZE) {
            val currentRow = gameField.getRow(i)
            val currentColumn = gameField.getColumn(i)

            if (isWinPosition(currentRow, winnerCellType)) winPosition =
                Position(currentRow.indexOf(CellType.EMPTY), i)
            else if (isWinPosition(currentColumn, winnerCellType)) winPosition =
                Position(i, currentColumn.indexOf(CellType.EMPTY))
        }

        if (winPosition != null) {
            val mainDiagonal = gameField.mainDiagonal
            val secondaryDiagonal = gameField.secondaryDiagonal

            if (isWinPosition(mainDiagonal, winnerCellType)) winPosition = Position(
                mainDiagonal.indexOf(CellType.EMPTY),
                mainDiagonal.indexOf(CellType.EMPTY)
            )
            if (isWinPosition(secondaryDiagonal, winnerCellType)) winPosition = Position(
                (FIELD_SIZE - 1) - secondaryDiagonal.indexOf(CellType.EMPTY),
                secondaryDiagonal.indexOf(CellType.EMPTY)
            )
        }

        return winPosition
    }

    private fun getEmptyCornerCellPositions(): List<Position> {
        val cornerCells = listOf(
            Position(0, 0), Position((FIELD_SIZE - 1), 0),
            Position((FIELD_SIZE - 1), (FIELD_SIZE - 1)), Position(0, (FIELD_SIZE - 1))
        )
        return cornerCells.filter { gameField.getCell(it) == CellType.EMPTY }
    }

    override fun makeDecision(): Position? {
        val emptyPositions = getEmptyPositions()
        if (!isBotStep() || emptyPositions.isEmpty()) return null

        lateinit var markPosition: Position
        if (gameField.getCell(Position(1, 1)) == CellType.EMPTY) {
            currentStrategy = GameStrategy.OFFENSIVE
            markPosition = Position(1, 1)
        } else {
            val winPosition = findWinPosition(botCellType)
            val enemyWinPosition = findWinPosition(enemyCellType)
            markPosition = winPosition ?: enemyWinPosition ?: when (currentStrategy) {
                GameStrategy.OFFENSIVE -> emptyPositions.random()
                GameStrategy.DEFENSIVE_INITIAL -> {
                    val emptyCornerCells = getEmptyCornerCellPositions()
                    currentStrategy = GameStrategy.DEFENSIVE_FINAL
                    if (emptyCornerCells.isNotEmpty()) emptyCornerCells.random()
                    else emptyCornerCells.random()
                }
                GameStrategy.DEFENSIVE_FINAL -> emptyPositions.random()
            }
        }

        return markPosition
    }
}
