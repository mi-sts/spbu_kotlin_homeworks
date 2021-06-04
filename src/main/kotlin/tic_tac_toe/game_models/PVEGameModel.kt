package tic_tac_toe.game_models

import javafx.util.Duration
import tic_tac_toe.CellType
import tic_tac_toe.GameField.Companion.FIELD_SIZE
import tic_tac_toe.Position
import tic_tac_toe.StepState
import tornadofx.runLater

abstract class PVEGameModel(private val playerCellType: CellType) : GameModel() {
    companion object {
        const val BOT_THINKING_DELAY = 750.0
    }

    protected var botCellType = CellType.oppositeType(playerCellType) ?: throw
    (NullPointerException("The bot cell type is null!"))

    init {
        if (botCellType == CellType.CROSS) botStep()
    }

    private fun getCellsNumber(): Map<CellType, Int> {
        val cellsNumber = mutableMapOf<CellType, Int>()
        for (x in 0 until FIELD_SIZE)
            for (y in 0 until FIELD_SIZE) {
                val cell = gameField.getCell(Position(x, y))
                require(cell != null) { IndexOutOfBoundsException("The cell does not exist!") }
                cellsNumber[cell] = cellsNumber.getOrPut(cell) { 0 } + 1
            }
        for (cellType in CellType.values()) cellsNumber[cellType] = cellsNumber.getOrPut(cellType) { 0 }

        return cellsNumber
    }

    protected fun getEmptyPositions(): List<Position> {
        val freePositions = mutableListOf<Position>()
        for (x in 0 until FIELD_SIZE)
            for (y in 0 until FIELD_SIZE) {
                val position = Position(x, y)
                if (gameField.getCell(position) == CellType.EMPTY) freePositions.add(position)
            }

        return freePositions
    }

    protected fun isBotStep(): Boolean {
        if (currentCellType != CellType.oppositeType(playerCellType)) return false

        val cellsNumber = getCellsNumber()
        require(CellType.values().any { it in cellsNumber.keys }) {
            NullPointerException("The number of the cell type is null!")
        }

        val crossCellsNumber = cellsNumber[CellType.CROSS] ?: 0
        val noughtCellsNumber = cellsNumber[CellType.NOUGHT] ?: 0
        return when (botCellType) {
            CellType.CROSS -> (crossCellsNumber - noughtCellsNumber) == 0
            CellType.NOUGHT -> (crossCellsNumber - noughtCellsNumber) == 1
            else -> false
        }
    }

    private fun isPlayerStep(): Boolean = currentCellType == playerCellType

    private fun botStep() {
        if (gameField.isGameOver || !isBotStep()) return
        val botDecisionPosition = makeDecision()
        require(botDecisionPosition != null) { NullPointerException("The bot decision is null!") }
        runLater(Duration(BOT_THINKING_DELAY)) {
            if (markCell(botDecisionPosition)) {
                val winnerCellType = gameField.winCheck()
                val botStepState = StepState(
                    botDecisionPosition, currentCellType, winnerCellType != null,
                    winnerCellType
                )
                gameController.updateGameState(botStepState)
                changeCurrentCellType()
            }
        }
    }

    private fun playerStep(position: Position) {
        if (gameField.isGameOver || !isPlayerStep()) return
        if (markCell(position)) {
            val winnerCellType = gameField.winCheck()
            val playerStepState = StepState(position, currentCellType, gameField.isGameOver, winnerCellType)
            gameController.updateGameState(playerStepState)
            changeCurrentCellType()
        }
    }

    override fun move(position: Position) {
        playerStep(position)
        botStep()
    }

    abstract fun makeDecision(): Position?
}
