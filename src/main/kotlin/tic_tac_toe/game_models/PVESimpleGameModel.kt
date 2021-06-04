package tic_tac_toe.game_models

import tic_tac_toe.CellType
import tic_tac_toe.Position

class PVESimpleGameModel(playerCellType: CellType) : PVEGameModel(playerCellType) {
    override fun makeDecision(): Position? {
        val emptyPositions = getEmptyPositions()
        if (!isBotStep() || emptyPositions.isEmpty()) return null

        return emptyPositions.random()
    }
}
