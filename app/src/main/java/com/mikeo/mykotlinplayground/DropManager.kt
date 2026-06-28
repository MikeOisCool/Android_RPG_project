package com.mikeo.mykotlinplayground

object DropManager {

    fun dropStackableItem(
        player: Player,
        item: Item
    ): DropResult {
        val oldAmount =
            player.inventory.items.find { it.name == item.name }?.amount ?:0

        val newAmount = (oldAmount + 1).coerceAtMost(10)

        if(oldAmount >= 10) {
            return DropResult(
                player = player,
                logs = listOf("🧪 ${item.name} Inventar (10) voll, Trank kann nicht genommen werden!")
            )
        }

        val newItems =
            player.inventory.items.filter { it.name != item.name } + item.copy(amount = newAmount)

        val updatedPlayer = player.copy(
            inventory = player.inventory.copy(items = newItems)
        )

        return DropResult(
            player = updatedPlayer,
            logs = listOf("🧪 ${player.name} erhält einen ${item.name}! $oldAmount -> $newAmount")
        )


    }

    fun dropPotion(
        player: Player,
        healPotion: Item
    ): DropResult {
        val oldPotions =
            player.inventory.items.find { it.name == healPotion.name }?.amount ?: 0

        val newPotions = (oldPotions + 1).coerceAtMost(10)

        if (oldPotions >= 10) {
            return DropResult(
                player = player,
                logs = listOf("🧪 Heiltrank Inventar (10) voll, Trank kann nicht genommen werden!")
            )
        }

        val newItems =
            player.inventory.items.filter { it.name != healPotion.name } +
                    healPotion.copy(amount = newPotions)

        val updatedInventory = player.inventory.copy(items = newItems)

        val updatedPlayer = player.copy(inventory = updatedInventory)

        return DropResult(
            player = updatedPlayer,
            logs = listOf("🧪 ${player.name} erhält einen Heiltrank! $oldPotions -> $newPotions")
        )
    }

    fun dropBigPotion(
        player: Player,
        healBigPotion: Item
    ): DropResult {
        val oldPotions =
            player.inventory.items.find { it.name == healBigPotion.name }?.amount ?: 0

        val newPotions = (oldPotions + 1).coerceAtMost(10)

        if (oldPotions >= 10) {
            return DropResult(
                player = player,
                logs = listOf("🧪 Großer Heiltrank Inventar (10) voll, Trank kann nicht genommen werden!")
            )
        }

        val newItems =
            player.inventory.items.filter { it.name != healBigPotion.name } +
                    healBigPotion.copy(amount = newPotions)

        val updatedInventory = player.inventory.copy(items = newItems)

        val updatedPlayer = player.copy(inventory = updatedInventory)

        return DropResult(
            player = updatedPlayer,
            logs = listOf("🧪 ${player.name} erhält einen großen Heiltrank! $oldPotions -> $newPotions")
        )
    }
}