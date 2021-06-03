package tic_tac_toe

data class Position(val x: Int, val y: Int)

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

class GameField {
    companion object { const val FIELD_SIZE = 3 }

    private var field = MutableList(FIELD_SIZE) { MutableList(FIELD_SIZE) { CellType.EMPTY } }

    val isGameOver: Boolean
        get() = winCheck() != null

    private val haveEmptyCells: Boolean
        get() = CellType.EMPTY in this.field.flatten()

    private fun canChange(position: Position) = isPositionExist(position) && getCell(position) == CellType.EMPTY

    private fun isCoordinateExist(coordinate: Int) = coordinate in 0 until FIELD_SIZE

    private fun isPositionExist(position: Position) = isCoordinateExist(position.x) && isCoordinateExist(position.y)

    val mainDiagonal: List<CellType>
        get() = this.field.mapIndexed { index, _ -> this.field[index][index] }

    val secondaryDiagonal: List<CellType>
        get() = this.field.mapIndexed { index, _ -> this.field[index][FIELD_SIZE - 1 - index] }

    fun getCell(position: Position): CellType? =
        if (isPositionExist(position)) field[position.y][position.x] else null

    fun setCell(position: Position, cellType: CellType): Boolean {
        if (!canChange(position)) return false

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

    private fun checkCellsWinner(checkedCells: List<CellType>): CellType? {
        return when {
            checkedCells.all { it == CellType.CROSS } -> CellType.CROSS
            checkedCells.all { it == CellType.NOUGHT } -> CellType.NOUGHT
            else -> null
        }
    }

    fun winCheck(): CellType? {
        var winner: CellType? = null
        for (i in 0 until FIELD_SIZE) {
            val horizontalWinner = checkCellsWinner(getRow(i))
            val verticalWinner = checkCellsWinner(getColumn(i))
            val mainDiagonalWinner = checkCellsWinner(mainDiagonal)
            val secondaryDiagonalWinner = checkCellsWinner(secondaryDiagonal)

            when {
                horizontalWinner != null -> winner = horizontalWinner
                verticalWinner != null -> winner = verticalWinner
                mainDiagonalWinner != null -> winner = mainDiagonalWinner
                secondaryDiagonalWinner != null -> winner = secondaryDiagonalWinner
            }
        }
        if (!haveEmptyCells) winner = CellType.EMPTY

        return winner
    }
}
