package tic_tac_toe.bots

import tic_tac_toe.CellType
import tic_tac_toe.GameModel
import tic_tac_toe.Position

class SimpleBot(botCellType: CellType, gameModel: GameModel) : Bot(botCellType, gameModel) {
    override fun makeDecision(): Position? {
        val emptyPositions = getEmptyPositions()
        if (!isBotStep() || emptyPositions.isEmpty()) return null

        return emptyPositions.random()
    }
}
