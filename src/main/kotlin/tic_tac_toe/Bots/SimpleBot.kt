package tic_tac_toe.Bots

import tic_tac_toe.CellType
import tic_tac_toe.GameModel
import tic_tac_toe.Position

class SimpleBot(botCellType: CellType, gameModel: GameModel) : Bot(botCellType, gameModel) {
    override fun makeDecision(): Position? {
        if (!isBotStep()) return null
        val freePositions = getFreePositions()

        if (freePositions.isEmpty()) return null
        return freePositions.random()
    }
}