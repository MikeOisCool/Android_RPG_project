package com.mikeo.mykotlinplayground

object DropManager {

    fun dropStackableItem(
        player: Player,
        item: Item
    ): DropResult {
        val oldAmount =
            player.inventory.items.find { it.name == item.name }?.amount ?: 0

        val newAmount = (oldAmount + 1).coerceAtMost(10)

        if (oldAmount >= 10) {
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
            logs = listOf("🧪 ${player.name} erhält ${item.article} ${item.name}! $oldAmount -> $newAmount")
        )

    }

    fun dropWeapon(
        player: Player,
        weapon: Item
    ): DropResult {
        val weaponName = player.inventory.items.find {
            it.name == weapon.name
        }
        if (weaponName != null) {
            return DropResult(
                player = player,
                logs = listOf("Du hast schon ${weapon.article} ${weapon.name}! Waffe kann nicht genommen werden")
            )
        }

        val newItems = player.inventory.items + weapon

        val updatedInventory =
            player.inventory.copy(
                items = newItems
            )

        return DropResult(
            player = player.copy(inventory = updatedInventory),
            logs = listOf("🧪 ${player.name} \uD83D\uDDE1\uFE0F hat ${weapon.article} ${weapon.name} gefunden! Angriff +${weapon.damage} nach Auswahl!!")

        )
    }

    fun dropArmor(
        player: Player,
        armor: Item
    ): DropResult {
        val weaponName = player.inventory.items.find {
            it.name == armor.name
        }
        if (weaponName != null) {
            return DropResult(
                player = player,
                logs = listOf("Du hast schon ${armor.article} ${armor.name}! Rüstung kann nicht genommen werden")
            )
        }

        val newItems = player.inventory.items + armor

        val updatedInventory =
            player.inventory.copy(
                items = newItems
            )

        return DropResult(
            player = player.copy(inventory = updatedInventory),
            logs = listOf("🧪 ${player.name} \uD83D\uDDE1\uFE0F hat ${armor.article} ${armor.name} gefunden! Verteidigung +${armor.defense} nach Auswahl!!")
        )
    }

    }