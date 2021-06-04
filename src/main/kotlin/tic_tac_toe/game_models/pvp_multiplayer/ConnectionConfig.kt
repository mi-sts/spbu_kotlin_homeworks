package tic_tac_toe.game_models.pvp_multiplayer

import kotlinx.serialization.Serializable

@Serializable
data class ConnectionConfig(val host: String, val port: Int, val path: String)
