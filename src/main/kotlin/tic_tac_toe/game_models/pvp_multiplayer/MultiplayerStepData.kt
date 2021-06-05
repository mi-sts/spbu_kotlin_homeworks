package tic_tac_toe.game_models.pvp_multiplayer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tic_tac_toe.CellType
import tic_tac_toe.Position

@Serializable
@SerialName("StepData")
data class MultiplayerStepData(val position: Position, val markedCellType: CellType)
