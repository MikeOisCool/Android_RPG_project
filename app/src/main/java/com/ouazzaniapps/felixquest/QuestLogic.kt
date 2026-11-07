package com.ouazzaniapps.felixquest

fun advanceQuestProgress(
    quest: Quest,
    defeatedEnemyName: String
): Quest {
    if (!quest.isStarted) return quest
    if (defeatedEnemyName != quest.targetEnemyName) return quest

    return quest.copy(
        currentAmount = quest.currentAmount + 1
    )
}