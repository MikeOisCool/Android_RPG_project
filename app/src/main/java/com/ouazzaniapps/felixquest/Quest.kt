package com.ouazzaniapps.felixquest

import kotlinx.serialization.Serializable

@Serializable
data class Quest(
    val title: String,
    val targetEnemyName: String,
    val targetAmount: Int,
    val currentAmount: Int,
    val goldReward: Int = 0,
    val xpReward: Int = 0,
    val weapon: Item? = null,
    val weaponLevel: Int = 1,
    val isWählbar: Boolean = true,
    val isStarted: Boolean = false,
    val isRewardCollected: Boolean = false
) {
    val isCompleted: Boolean
        get() = currentAmount >= targetAmount
    val description: String
        get() = "Besiege $targetAmount $targetEnemyName um Gold und XP zu erhalten"
}

object QuestName {
    val allQuests = listOf(
        Quest(
            title = "Finde Goblins",
            targetEnemyName = "Goblin",
            targetAmount = 3,
            currentAmount = 0,
            goldReward = 100,
            xpReward = 100,
            weapon = GameItems.goblinWeapon
        ),
        Quest(
            title = "Finde Wölfe",
            targetEnemyName = "Wolf",
            targetAmount = 6,
            currentAmount = 0,
            goldReward = 100,
            xpReward = 100,
            weapon = GameItems.wolfWeapon
        ),
        Quest(
            title = "Finde den Stier",
            targetEnemyName = "Stier",
            targetAmount = 1,
            currentAmount = 0,
            goldReward = 500,
            xpReward = 500,
            weapon = GameItems.stierWeapon

        )
    )
}

fun prepareNextLevelQuest(quest: Quest): Quest {
    val nextWeaponLevel = quest.weaponLevel + 1
    val newDamage = ((quest.weapon?.damage ?: 0) * 1.5).toInt()

    val nextWeapon = quest.weapon?.copy(
        damage = newDamage,
        description = "Schaden +${newDamage}"
    )

    return quest.copy(
        isStarted = false,
        currentAmount = 0,
        targetAmount = quest.targetAmount * 2,
        goldReward = quest.goldReward * 2,
        xpReward = quest.xpReward * 2,
        weapon = nextWeapon,
        weaponLevel = nextWeaponLevel
    )
}

fun romanNumber(number: Int): String {
    return when (number) {
        1 -> "I"
        2 -> "II"
        3 -> "III"
        4 -> "IV"
        5 -> "V"
        6 -> "VI"
        7 -> "VII"
        8 -> "VIII"
        else -> number.toString()
    }
}

fun questRewardWeapon(quest: Quest): Item? {
    return quest.weapon?.copy(
        name = "${quest.weapon.name} ${romanNumber(quest.weaponLevel)}"
    )
}