package com.mikeo.mykotlinplayground

import android.util.Log
import com.mikeo.mykotlinplayground.ItemNamen

fun handleEvent(
    player: Player,
    event: GameEvent
): Player {

    return when (event) {

        is GameEvent.TakeDamage -> {

            val newHp = (player.hp - event.amount).coerceAtLeast(0)
            val goldCost = if (player.gold >= 5) 5 else 0

            player.copy(
                hp = newHp,
                gold = player.gold - goldCost,
                isDead = newHp <= 0
            )
        }

        is GameEvent.AddGold -> {

            if (player.gold >= 1000) {
                player
            } else {
                player.copy(
                    gold = player.gold + event.amount
                )
            }
        }

        is GameEvent.Heal -> {

            if (player.gold < event.amount) {
                player
            } else {
                val newHp = (player.hp + event.amount).coerceAtMost(player.maxHp)

                player.copy(

                    hp = newHp,
                    gold = player.gold - event.amount
                )
            }
        }

        is GameEvent.UsePotion -> {
            val potion =
                player.inventory.items.find { it.name == ItemNamen.HEILTRANK }
            if (potion == null || potion.amount <= 0) {
                player
            } else {
                val healAmount = calculateItemHeal(potion.heal, player.level)
                val newHp = (player.hp + healAmount).coerceAtMost(player.maxHp)

                val newItems = player.inventory.items.map { item ->
                    if (item.name == ItemNamen.HEILTRANK) {
                        item.copy(amount = item.amount - 1)
                    } else {
                        item
                    }
                }.filter { item ->
                    item.amount > 0
                }
                val updateInventory = player.inventory.copy(
                    items = newItems
                )
                player.copy(
                    hp = newHp,
                    inventory = updateInventory
                )


            }
        }

        is GameEvent.UseBigPotion -> {
            val potionBig =
                player.inventory.items.find { it.name == ItemNamen.GROSSER_HEILTRANK }
            if (potionBig == null || potionBig.amount <= 0) {
                player
            } else {
                val healBigAmount = calculateItemHeal(potionBig.heal, player.level)
                val newHp = (player.hp + healBigAmount).coerceAtMost(player.maxHp)
                val newItems = player.inventory.items.map { item ->
                    if (item.name == ItemNamen.GROSSER_HEILTRANK) {
                        item.copy(amount = item.amount - 1)
                    } else {
                        item
                    }
                }.filter { item ->
                    item.amount > 0
                }
                val updateInventory = player.inventory.copy(
                    items = newItems
                )
                player.copy(
                    hp = newHp,
                    inventory = updateInventory
                )


            }
        }

        is GameEvent.EquipWeapon -> {
            val hasWeapon = player.inventory.items.contains(event.weapon)
            if (hasWeapon) {
                player.copy(
                    equippedWeapon = event.weapon
                )
            } else {
                player
            }
        }

        is GameEvent.EquipArmor -> {
            val hasArmor = player.inventory.items.contains(event.armor)
            if (hasArmor) {
                player.copy(
                    equippedArmor = event.armor
                )
            } else {
                player
            }
        }

        is GameEvent.Flee -> {

            val fleeCost = 20 + (player.level - 1) * 10

            if (player.gold < fleeCost) {
                player
            } else {
                player.copy(
                    gold = player.gold - fleeCost
                )
            }
        }

        is GameEvent.AttackEnemy -> {
            player
        }


        is GameEvent.GainXp -> {
            var remainingXp = player.xp + event.amount
            var newLevel = player.level
            var newMaxHp = player.maxHp
            var newXpToNextLevel = player.xpToNextLevel

            while (remainingXp >= newXpToNextLevel) {
                remainingXp -= newXpToNextLevel
                newLevel++
                newMaxHp += 10
                newXpToNextLevel = newLevel * 100
            }
            val hpNachXp = if (newLevel > player.level) {
                newMaxHp
            } else {
                player.hp
            }

            player.copy(
                level = newLevel,
                maxHp = newMaxHp,
                hp = hpNachXp,
                xp = remainingXp,
                xpToNextLevel = newXpToNextLevel
            )
        }
    }
}

fun damageEnemy(
    enemy: Enemy,
    damage: Int
): Enemy {

    return enemy.copy(
        hp = (enemy.hp - damage).coerceAtLeast(0)
    )
}

fun calculateDamage(
    baseDamage: Int,
    chance: Int,
    critMultiplier: Int,

    ): Pair<Int, Boolean> {
    val criticalHit = chance(chance)
    return if (criticalHit) {
        Pair(
            baseDamage * critMultiplier,
            true
        )
    } else {
        Pair(baseDamage, false)
    }
}

fun chance(
    chance: Int
): Boolean {
    val roll = (1..100).random()
    return roll <= chance
}

fun calculateItemHeal(
    baseHeal: Int,
    level: Int
): Int {
    return baseHeal + (level - 1) * 7
}


fun createScaledEnemy(
    baseEnemy: Enemy,
    playerLevel: Int
): Enemy {
    return baseEnemy.copy(
        level = playerLevel,
        hp = baseEnemy.hp + (playerLevel - 1) * 10,
        maxHp = baseEnemy.maxHp + (playerLevel - 1) * 10,
        damage = baseEnemy.damage + (playerLevel - 1) * 2,
        goldReward = baseEnemy.goldReward + (playerLevel - 1) * 5,
        xpReward = baseEnemy.xpReward + (playerLevel - 1) * 10

    )
}