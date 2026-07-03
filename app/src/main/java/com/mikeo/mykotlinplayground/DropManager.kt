package com.mikeo.mykotlinplayground

object DropManager {

    fun dropStackableItem(
        player: Player, item: Item
    ): DropResult {
        val oldAmount = player.inventory.items.find { it.name == item.name }?.amount ?: 0

        val newAmount = (oldAmount + 1).coerceAtMost(10)

        if (oldAmount >= 10) {
            return DropResult(
                player = player,
                logs = listOf("🧪 ${item.name} Inventar (10) ist voll, Trank kann nicht genommen werden!")
            )
        }

        val newItems =
            player.inventory.items.filter { it.name != item.name } + item.copy(amount = newAmount)

        val updatedPlayer = player.copy(
            inventory = player.inventory.copy(items = newItems)
        )

        return DropResult(
            player = updatedPlayer,
            logs = listOf("🧪 ${player.name} erhält ${item}! $oldAmount -> $newAmount")
        )

    }


    fun dropUniqueItem(player: Player, item: Item): DropResult {
        val existingItem = player.inventory.items.find { it.name == item.name }

        if (existingItem != null) {
            val typeName = when (item.type) {
                ItemType.WEAPON -> "Waffe"
                ItemType.ARMOR -> "Rüstung"
                else -> "Item"
            }
            return DropResult(
                player = player,
                logs = listOf("Du hast schon ${item.article} ${item.name}! $typeName kann nicht genommen werden")
            )
        }

        val newItems = player.inventory.items + item

        val logMessage = when (item.type) {
            ItemType.WEAPON -> "\uD83D\uDDE1\uFE0F${player.name}  hat ${item.toString()} gefunden! Angriff +${item.damage} nach Auswahl!!"
            ItemType.ARMOR -> "\uD83D\uDEE1\uFE0F ${player.name} hat $item gefunden! Verteidigung +${item.defense} nach Auswahl!!"
            ItemType.POTION -> error("POTION darf nicht an dropUniqueItem übergeben werden")
        }

        return DropResult(
            player = player.copy(
                inventory = player.inventory.copy(
                    items = newItems
                )
            ),
            logs = listOf(logMessage)
        )
    }
}