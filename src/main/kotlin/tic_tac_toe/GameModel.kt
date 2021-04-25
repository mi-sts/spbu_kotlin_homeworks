package tic_tac_toe

import tic_tac_toe.GameController.Companion.FIELD_RANGE

private var currentCellType = CellType.CROSS
private var gameOver = false
private var winner: CellType? = null

enum class CellType {
    EMPTY, CROSS, NOUGHT
}

@Suppress("TooManyFunctions")
class GameModel {
    private var field =
        MutableList(FIELD_RANGE.count()) {
            MutableList(FIELD_RANGE.count()) { CellType.EMPTY }
        }

    private fun canChange(xPos: Int, yPos: Int) = !gameOver && getCell(xPos, yPos) == CellType.EMPTY &&
            isPositionsExist(xPos, yPos)

    private fun isPositionExist(position: Int) = position in FIELD_RANGE

    private fun isPositionsExist(xPos: Int, yPos: Int) = isPositionExist(xPos) && isPositionExist(yPos)

    private fun haveEmptyCells() = CellType.EMPTY in field.flatten()

    private fun getCell(xPos: Int, yPos: Int): CellType? =
        if (isPositionExist(xPos) && isPositionExist(yPos)) field[yPos][xPos] else null

    private fun setCell(xPos: Int, yPos: Int, cellType: CellType): Boolean {
        if (!isPositionsExist(xPos, yPos)) return false

        field[yPos][xPos] = cellType
        return true
    }

    private fun getRow(position: Int): List<CellType> {
        require(isPositionExist(position)) { IndexOutOfBoundsException("The row position out of field bounds!") }

        return field[position].toList()
    }

    private fun getColumn(position: Int): List<CellType> {
        require(isPositionExist(position)) { IndexOutOfBoundsException("The column position out of field bounds!") }

        return field.map { it[position] }
    }

    private fun getDiagonals(): Pair<List<CellType>, List<CellType>> =
        Pair(field.mapIndexed { index, _ -> field[index][index] },
            field.mapIndexed { index, _ -> field[index][FIELD_RANGE.count() - 1 - index] })

    private fun clear() {
        for (x in FIELD_RANGE)
            for (y in FIELD_RANGE)
                setCell(x, y, CellType.EMPTY)
    }

    private fun changeCurrentCellType() {
        currentCellType = if (currentCellType == CellType.CROSS) CellType.NOUGHT else CellType.CROSS
    }

    fun markCell(xPos: Int, yPos: Int): CellType? {
        if (!canChange(xPos, yPos)) return null
        changeCurrentCellType()
        setCell(xPos, yPos, currentCellType)
        val winner = winCheck(xPos, yPos)
        if (winner != null) gameOver(winner)

        return currentCellType
    }

    private fun getTypeCells(type: CellType): List<CellType> = List(FIELD_RANGE.count()) { type }

    private fun checkCellsWinner(checkedCells: List<CellType>): CellType? =
        when (checkedCells) {
            getTypeCells(CellType.CROSS) -> CellType.CROSS
            getTypeCells(CellType.NOUGHT) -> CellType.NOUGHT
            else -> null
        }

    private fun isDiagonalsPosition(xPos: Int, yPos: Int) =
        (xPos == yPos) || ((xPos + yPos) == (FIELD_RANGE.count() - 1))

    private fun winCheck(chosenCellXPos: Int, chosenCellYPos: Int): CellType? {
        var winner: CellType? = null
        val horizontalWinner = checkCellsWinner(getRow(chosenCellYPos))
        val verticalWinner = checkCellsWinner(getColumn(chosenCellXPos))

        if (horizontalWinner != null) winner = horizontalWinner
        else if (verticalWinner != null) winner = verticalWinner
        else if (isDiagonalsPosition(chosenCellXPos, chosenCellYPos)) {
            val diagonals = getDiagonals()
            val diagonalsWinners = Pair(checkCellsWinner(diagonals.first), checkCellsWinner(diagonals.second))
            if (diagonalsWinners.first != null) winner = diagonalsWinners.first
            else if (diagonalsWinners.second != null) winner = diagonalsWinners.second
        } else if (!haveEmptyCells()) winner = CellType.EMPTY

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
