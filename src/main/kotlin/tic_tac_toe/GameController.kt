package tic_tac_toe

class GameController {
    companion object {
        val FIELD_RANGE = 0..2
        private var currentCellType = CellType.CROSS
        private var gameOver = true
        private var isTie = true

        enum class CellType(val symbol: Char) {
            EMPTY('*'), CROSS('X'), NOUGHT('O')
        }

        class GameField {
            companion object {
                private var field =
                    MutableList(FIELD_RANGE.count()) {
                        MutableList(FIELD_RANGE.count()) { CellType.EMPTY }
                    }

                private fun isPositionExist(position: Int) = position in FIELD_RANGE

                fun getCell(xPos: Int, yPos: Int): CellType? =
                    if (isPositionExist(xPos) && isPositionExist(yPos)) field[yPos][xPos] else null

                fun setCell(xPos: Int, yPos: Int, cellType: CellType): Boolean {
                    if (!gameOver || !isPositionExist(xPos) && isPositionExist(yPos)
                        || getCell(xPos, yPos) != CellType.EMPTY)
                        return false

                    field[yPos][xPos] = cellType
                    return true
                }

                fun getRow(position: Int): List<CellType>? {
                    if (!isPositionExist(position))
                        return null

                    return field[position].toList()
                }

                fun getColumn(position: Int): List<CellType>? {
                    if (!isPositionExist(position))
                        return null

                    return field.map { it[position] }
                }

                fun getDiagonals(): Pair<List<CellType>, List<CellType>> =
                    Pair(field.mapIndexed {
                            index, _ -> field[index][index] },
                        field.mapIndexed {
                            index, _ -> field[index][FIELD_RANGE.count() - 1 - index]
                        })

                fun clear() {
                    field = MutableList(FIELD_RANGE.count()) { MutableList(FIELD_RANGE.count()) { CellType.EMPTY } }
                }
            }
        }

        private fun finishGame(winner: CellType) {
            when (winner) {
                CellType.CROSS -> {
                    GameView.showGameOverScreen("Cross wins!!!")
                    isTie = false
                }
                CellType.NOUGHT -> {
                    GameView.showGameOverScreen("Nought wins!!!")
                    isTie = false
                }
                CellType.EMPTY -> GameView.showGameOverScreen("Tie!!!")
            }
            gameOver = false
        }

        private fun getTypeCells(type: CellType) = List(FIELD_RANGE.count()) { type }

        private fun isAllCellsMarked(): Boolean {
            var isMarked = true

            for (i in FIELD_RANGE) {
                if (GameField.getRow(i)?.contains(CellType.EMPTY) == true) {
                    isMarked = false
                    break
                }
            }

            return isMarked
        }

        private fun checkWin(placedXPos: Int, placedYPos: Int) {
            checkWinCells(GameField.getColumn(placedXPos))
            checkWinCells(GameField.getRow(placedYPos))
            val diagonals = GameField.getDiagonals()
            checkWinCells(diagonals.first)
            checkWinCells(diagonals.second)

            if (isTie && isAllCellsMarked())
                finishGame(CellType.EMPTY)
        }

        private fun checkWinCells(checkedCells: List<CellType>?) {
            when (checkedCells) {
                getTypeCells(CellType.CROSS) -> finishGame(CellType.CROSS)
                getTypeCells(CellType.NOUGHT) -> finishGame(CellType.NOUGHT)
            }
        }

        fun markCell(xPos: Int, yPos: Int): String? {
            return if (GameField.setCell(xPos, yPos, currentCellType)) {
                checkWin(xPos, yPos)
                currentCellType = if (currentCellType == CellType.CROSS) CellType.NOUGHT else CellType.CROSS
                currentCellType.symbol.toString()
            } else null
        }

        fun restart() {
            currentCellType = CellType.CROSS
            gameOver = true
            isTie = true
            GameField.clear()
        }
    }
}
