package com.ouazzaniapps.felixquest

import kotlinx.serialization.Serializable

@Serializable
data class GameSave(
    val player: Player,
    val enemy: Enemy,
    val quests: List<Quest>,
    val log: List<String>,
    val elapsedSeconds: Int,
    val savedAtMillis: Long = System.currentTimeMillis()
)
