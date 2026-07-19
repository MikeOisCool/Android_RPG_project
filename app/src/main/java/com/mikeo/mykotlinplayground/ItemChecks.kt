package com.mikeo.mykotlinplayground

fun isEquippedItem(item: Item, player: Player): Boolean {
    return player.equippedWeapon?.name == item.name || player.equippedArmor?.name == item.name
}

fun isUniqueItemAlreadyInInventory(item: Item, inventory: Inventory): Boolean {
    return item.type != ItemType.POTION &&
            inventory.items.any { it.name == item.name }
}

fun isWeaponOrArmor(item: Item): Boolean {
    return item.type == ItemType.WEAPON || item.type == ItemType.ARMOR
}

fun isPotionStackFull(item: Item, inventory: Inventory): Boolean {
    val itemInventory = inventory.items.find { it.name == item.name }

    return item.type == ItemType.POTION &&
            (itemInventory?.amount ?: 0) >= 10
}

fun hasItemInInventory(item: Item, inventory: Inventory): Boolean {
    return inventory.items.any { it.name == item.name }
}
