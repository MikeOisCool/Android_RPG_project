package com.mikeo.mykotlinplayground

import android.util.Log
import com.mikeo.mykotlinplayground.ItemNamen

fun handleEvent(
    player: Player, event: GameEvent
): Player {

    return when (event) {

        is GameEvent.TakeDamage -> {

            val newHp = (player.hp - event.amount).coerceAtLeast(0)
            val goldCost = if (player.gold >= 5) 5 else 0

            player.copy(
                hp = newHp, gold = player.gold - goldCost, isDead = newHp <= 0
            )
        }

        is GameEvent.AddGold -> {

            player.copy(
                gold = player.gold + event.amount
            )
        }

        is GameEvent.Heal -> {

            if (player.gold < event.amount) {
                player
            } else {
                val newHp = (player.hp + event.amount).coerceAtMost(player.maxHp)

                player.copy(

                    hp = newHp, gold = player.gold - event.amount
                )
            }
        }

        is GameEvent.UsePotion -> {
            usePotionByName(player, ItemNamen.HEILTRANK)
        }

        is GameEvent.UseBigPotion -> {
            usePotionByName(player, ItemNamen.GROSSER_HEILTRANK)
        }

        is GameEvent.EquipWeapon -> {
            if (player.isDead) return player
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
            if (player.isDead) return player
            val hasArmor = player.inventory.items.contains(event.armor)
            if (hasArmor) {
                player.copy(
                    equippedArmor = event.armor
                )
            } else {
                player
            }
        }

        is GameEvent.UnequipWeapon -> {
            if (player.isDead) return player
            player.copy(equippedWeapon = null)
        }

        is GameEvent.UnequipArmor -> {
            if (player.isDead) return player
            player.copy(equippedArmor = null)
        }

        is GameEvent.RemoveInventoryItem -> {
            val hasItem = player.inventory.items.contains(event.item)
            if (hasItem) {
                val newItems = player.inventory.items.filter { item ->
                    item.name != event.item.name
                }
                player.copy(
                    equippedWeapon = player.equippedWeapon?.takeIf { weapon ->
                        weapon.name != event.item.name
                    },
                    equippedArmor = player.equippedArmor?.takeIf { armor ->
                        armor.name != event.item.name
                    },
                    inventory = player.inventory.copy(
                        items = newItems
                    )
                )
            } else {
                player
            }
        }

        is GameEvent.BuyItem -> {
            val price = buyPrice(event.item, player.level)
            val itemInventory = player.inventory.items.find { item ->
                item.name == event.item.name
            }
            if (player.gold < price) {
                player
            } else if (isPotionStackFull(event.item, player.inventory)) {
                player
            } else if (isUniqueItemAlreadyInInventory(
                    item = event.item,
                    inventory = player.inventory
                )
            ) {
                player
            } else {

                val newItems = when (event.item.type) {
                    ItemType.POTION -> {
                        if (itemInventory != null) {

                            player.inventory.items.map { item ->
                                if (item.name == event.item.name) {
                                    item.copy(amount = item.amount + 1)
                                } else {
                                    item
                                }
                            }
                        } else {
                            player.inventory.items + event.item
                        }
                    }

                    ItemType.WEAPON -> {
                        player.inventory.items + event.item
                    }

                    ItemType.ARMOR -> {
                        player.inventory.items + event.item
                    }
                }
                player.copy(
                    inventory = player.inventory.copy(items = newItems),
                    gold = player.gold - price
                )
            }
        }


        is GameEvent.SellItem -> {
            val itemToSell = player.inventory.items.find { it.name == event.item.name }
            if (itemToSell == null || itemToSell.amount <= 0) {
                player
            } else {
                val newItems = player.inventory.items.map { item ->
                    if (item.name == event.item.name) {
                        item.copy(amount = item.amount - 1)
                    } else {
                        item
                    }
                }.filter { item -> item.amount > 0 }
                player.copy(
                    inventory = player.inventory.copy(
                        items = newItems
                    ),
                    gold = player.gold + sellPrice(event.item, player.level)
                )
            }

        }

        is GameEvent.Flee -> {

            if (canFlee(player.level, player.gold)) {
                player.copy(
                    gold = player.gold - fleeCost(player.level)
                )
            } else {
                player
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

private fun fleeCost(playerLevel: Int): Int {
    return 20 + (playerLevel - 1) * 10
}

private fun canFlee(playerLevel: Int, gold: Int): Boolean {
    return gold >= fleeCost(playerLevel)
}

private fun usePotionByName(
    player: Player, itemName: String
): Player {
    val potion = player.inventory.items.find { it.name == itemName }
    if (potion == null || potion.amount <= 0) {
        return player
    }
    val healAmount = calculateItemHeal(potion.heal, player.level)
    val newHp = (player.hp + healAmount).coerceAtMost(player.maxHp)

    val newItems = player.inventory.items.map { item ->
        if (item.name == itemName) {
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
    return player.copy(
        hp = newHp, inventory = updateInventory
    )
}

fun calculateAttack(
    baseAttack: Int, weaponBonus: Int
): Int {
    return (baseAttack + weaponBonus).coerceAtLeast(0)
}

fun calculateDamage(
    attackDamage: Int, critChance: Int, critMultiplier: Int
): DamageResult {
    val criticalHit = chance(critChance)
    val damage = if (criticalHit) attackDamage * critMultiplier else attackDamage
    return DamageResult(
        amount = damage, isCritical = criticalHit
    )
}

fun calculateDamageAfterDefense(
    incomingDamage: Int, defenderDefense: Int
): Int {
    return (incomingDamage - defenderDefense).coerceAtLeast(0)
}

fun damageEnemy(
    enemy: Enemy, attackDamage: Int
): EnemyDamageResult {
    val finalDamage = calculateDamageAfterDefense(
        incomingDamage = attackDamage, defenderDefense = enemy.defense
    )
    return EnemyDamageResult(
        enemy = enemy.copy(
            hp = (enemy.hp - finalDamage).coerceAtLeast(0)
        ),
        damage = finalDamage
    )
}

fun chance(
    chance: Int
): Boolean {
    val roll = (1..100).random()
    return roll <= chance
}

fun calculateItemHeal(
    baseHeal: Int, level: Int
): Int {
    return baseHeal + (level - 1) * 7
}


fun createScaledEnemy(
    baseEnemy: Enemy, playerLevel: Int
): Enemy {
    return baseEnemy.copy(
        level = playerLevel,
        hp = baseEnemy.hp + (playerLevel - 1) * 10,
        maxHp = baseEnemy.maxHp + (playerLevel - 1) * 10,
        attack = baseEnemy.attack + (playerLevel - 1) * 2,
        defense = baseEnemy.defense + (playerLevel - 1),
        goldReward = baseEnemy.goldReward + (playerLevel - 1) * 5,
        xpReward = baseEnemy.xpReward + (playerLevel - 1) * 10

    )
}
