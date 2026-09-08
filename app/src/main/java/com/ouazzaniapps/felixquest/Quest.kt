package com.ouazzaniapps.felixquest

data class Quest(
    val title: String,
    val targetEnemyName: String,
    val targetAmount: Int,
    val currentAmount: Int,
    val goldReward: Int = 0,
    val xpReward: Int = 0,
    val description: String = "Besiege $targetAmount $targetEnemyName um Gold und XP zu erhalten",
    val isWählbar: Boolean = true,
    val isStarted: Boolean = false,
    val isRewardCollected: Boolean = false
) {
    val isCompleted: Boolean
        get() = currentAmount >= targetAmount
}

object QuestName {
    val allQuests = listOf(
        Quest(
            title = "Finde Goblins",
            targetEnemyName = "Goblin",
            targetAmount = 3,
            currentAmount = 0,
            goldReward = 100,
            xpReward = 100
        ),
        Quest(
            title = "Finde Wölfe",
            targetEnemyName = "Wolf",
            targetAmount = 3,
            currentAmount = 0,
            goldReward = 100,
            xpReward = 100
        ),
        Quest(
            title = "Finde den Stier",
            targetEnemyName = "Stier",
            targetAmount = 1,
            currentAmount = 0,
            goldReward = 500,
            xpReward = 500
        )
    )
}
