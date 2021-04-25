package tic_tac_toe.bots

import tic_tac_toe.CellType
import tic_tac_toe.GameModel
import tic_tac_toe.GameModel.Companion.FIELD_RANGE
import tic_tac_toe.Position
import java.lang.IllegalArgumentException

class HardBot(private val botCellType: CellType, private val gameModel: GameModel) : Bot(botCellType, gameModel) {
    private enum class GameStrategy {
        DEFENSIVE_INITIAL, DEFENSIVE_FINAL, OFFENSIVE
    }

    private var currentStrategy =
        if (botCellType == CellType.NOUGHT) GameStrategy.DEFENSIVE_INITIAL else GameStrategy.OFFENSIVE
    private val enemyCellType = if (botCellType == CellType.NOUGHT) CellType.CROSS else CellType.NOUGHT

    private fun isWinPosition(cellLine: List<CellType>, winnerCellType: CellType): Boolean {
        require(cellLine.count() == FIELD_RANGE.count()) {
            IllegalArgumentException("Incorrect size of the cell line!")
        }
        return cellLine.count { it == winnerCellType } == FIELD_RANGE.last && cellLine.contains(CellType.EMPTY)
    }

    private fun findWinPosition(winnerCellType: CellType): Position? {
        var winPosition: Position? = null
        for (i in FIELD_RANGE) {
            val currentRow = gameModel.getRow(i)
            val currentColumn = gameModel.getColumn(i)

            if (isWinPosition(currentRow, winnerCellType)) winPosition =
                Position(currentRow.indexOf(CellType.EMPTY), i)
            else if (isWinPosition(currentColumn, winnerCellType)) winPosition =
                Position(i, currentColumn.indexOf(CellType.EMPTY))
        }

        if (winPosition != null) {
            val diagonals = gameModel.getDiagonals()
            if (isWinPosition(diagonals.first, winnerCellType)) winPosition = Position(
                diagonals.first.indexOf(CellType.EMPTY),
                diagonals.first.indexOf(CellType.EMPTY)
            )
            if (isWinPosition(diagonals.second, winnerCellType)) winPosition = Position(
                FIELD_RANGE.last - diagonals.second.indexOf(CellType.EMPTY), diagonals.second.indexOf(CellType.EMPTY)
            )
        }

        return winPosition
    }

    private fun getEmptyCornerCellPositions(): List<Position> {
        val cornerCells = listOf(Position(0, 0), Position(FIELD_RANGE.last, 0),
            Position(FIELD_RANGE.last, FIELD_RANGE.last), Position(0, FIELD_RANGE.last)
        )
        return cornerCells.filter { gameModel.getCell(it) == CellType.EMPTY }
    }

    override fun makeDecision(): Position? {
        val emptyPositions = getEmptyPositions()
        if (!isBotStep() || emptyPositions.isEmpty()) return null

        lateinit var markPosition: Position
        if (gameModel.getCell(Position(1, 1)) == CellType.EMPTY) {
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
