package com.mikeo.mykotlinplayground

const val SELL_PRICE_PERCENT = 50
const val POTION_PRICE_PER_LEVEL = 10

fun availableShopItems(playerLevel: Int): List<Item> {

    return when (playerLevel) {
        in 1..2 -> listOf(GameItems.healPotion, GameItems.woodWeapon, GameItems.simpleArmor)
        in 3..4 -> listOf(GameItems.healBigPotion, GameItems.ironWeapon, GameItems.ironArmor)
        in 5..6 -> listOf(GameItems.silverWeapon)
        in 8..10 -> listOf(GameItems.diamondWeapon, GameItems.goldenWeapon)
        else -> listOf(GameItems.healPotion, GameItems.healBigPotion)
    }
}

fun sellPrice(item: Item, playerLevel: Int): Int {
    return (buyPrice(item, playerLevel) * SELL_PRICE_PERCENT) / 100
}

fun buyPrice(item: Item, playerLevel: Int): Int {
    return when (item.type) {
        ItemType.POTION -> item.itemPrice + (playerLevel - 1) * POTION_PRICE_PER_LEVEL
        else -> item.itemPrice
    }
}

