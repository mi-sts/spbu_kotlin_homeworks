package tic_tac_toe

enum class CellType {
    EMPTY, CROSS, NOUGHT;

    companion object {
        fun oppositeType(cellType: CellType): CellType? = when (cellType) {
            CROSS -> NOUGHT
            NOUGHT -> CROSS
            else -> null
        }
    }
}

data class Position(val x: Int, val y: Int)

@Suppress("TooManyFunctions")
class GameModel {
    companion object { val FIELD_RANGE = 0..2 }

    private var currentCellType = CellType.CROSS
    private var gameOver = false
    private var winner: CellType? = null

    private var field =
        MutableList(FIELD_RANGE.count()) { MutableList(FIELD_RANGE.count()) { CellType.EMPTY } }

    private fun canChange(position: Position) = !gameOver && getCell(position) == CellType.EMPTY &&
            isPositionExist(position)

    private fun isCoordinateExist(coordinate: Int) = coordinate in FIELD_RANGE

    private fun isPositionExist(position: Position) = isCoordinateExist(position.x) && isCoordinateExist(position.y)

    private fun haveEmptyCells() = CellType.EMPTY in field.flatten()

    fun getCell(position: Position): CellType? =
        if (isPositionExist(position)) field[position.y][position.x] else null

    private fun setCell(position: Position, cellType: CellType): Boolean {
        if (!isPositionExist(position)) return false

        field[position.y][position.x] = cellType
        return true
    }

    fun getRow(index: Int): List<CellType> {
        require(isCoordinateExist(index)) { IndexOutOfBoundsException("The row index out of field bounds!") }

        return field[index].toList()
    }

    fun getColumn(index: Int): List<CellType> {
        require(isCoordinateExist(index)) { IndexOutOfBoundsException("The column index out of field bounds!") }

        return field.map { it[index] }
    }

    fun getDiagonals(): Pair<List<CellType>, List<CellType>> =
        Pair(field.mapIndexed { index, _ -> field[index][index] },
            field.mapIndexed { index, _ -> field[index][FIELD_RANGE.count() - 1 - index] })

    private fun clear() {
        for (x in FIELD_RANGE)
            for (y in FIELD_RANGE)
                setCell(Position(x, y), CellType.EMPTY)
    }

    private fun changeCurrentCellType() {
        currentCellType = CellType.oppositeType(currentCellType) ?: throw
        NullPointerException("The current cell type is null!")
    }

    fun markCell(position: Position): CellType? {
        if (!canChange(position)) return null
        setCell(position, currentCellType)
        val winner = winCheck(position)
        if (winner != null) gameOver(winner)

        return currentCellType.also { changeCurrentCellType() }
    }

    private fun getTypeCells(type: CellType): List<CellType> = List(FIELD_RANGE.count()) { type }

    private fun checkCellsWinner(checkedCells: List<CellType>): CellType? =
        when (checkedCells) {
            getTypeCells(CellType.CROSS) -> CellType.CROSS
            getTypeCells(CellType.NOUGHT) -> CellType.NOUGHT
            else -> null
        }

    private fun isDiagonalsPosition(position: Position) =
        (position.x == position.y) || ((position.x + position.y) == (FIELD_RANGE.count() - 1))

    private fun winCheck(chosenPosition: Position): CellType? {
        var winner: CellType? = null
        val horizontalWinner = checkCellsWinner(getRow(chosenPosition.y))
        val verticalWinner = checkCellsWinner(getColumn(chosenPosition.x))

        if (horizontalWinner != null) winner = horizontalWinner
        else if (verticalWinner != null) winner = verticalWinner
        else if (isDiagonalsPosition(chosenPosition)) {
            val diagonals = getDiagonals()
            val diagonalsWinners = Pair(checkCellsWinner(diagonals.first), checkCellsWinner(diagonals.second))
            if (diagonalsWinners.first != null) winner = diagonalsWinners.first
            else if (diagonalsWinners.second != null) winner = diagonalsWinners.second
        }

        if (!haveEmptyCells() && winner == null) winner = CellType.EMPTY

        return winner
    }

    private fun gameOver(newWinner: CellType) {
        gameOver = true
        winner = newWinner
    }

    fun isGameOver(): Boolean = gameOver

    fun getWinner(): CellType? = if (gameOver) winner else null

    fun createNewGame() {
        gameOver = false
        winner = null
        currentCellType = CellType.CROSS
        clear()
    }
}
