package com.mikeo.mykotlinplayground


object GameItems {
    val woodWeapon = Item(
        name = ItemNamen.HOLZSCHWERT,
        type = ItemType.WEAPON,
        description = "Schaden + 15",
        amount = 1,
        heal = 0,
        damage = 15,
        defense = 0
    )
    val ironWeapon = Item(
        name = ItemNamen.EISENSCHWERT,
        type = ItemType.WEAPON,
        description = "Schaden + 35",
        amount = 1,
        heal = 0,
        damage = 35,
        defense = 0
    )
    val simpleArmor = Item(
        name = ItemNamen.EINFACHE_RÜSTUNG,
        type = ItemType.ARMOR,
        description = "Verteidigung + 10",
        amount = 1,
        heal = 0,
        damage = 0,
        defense = 10
    )
    val healPotion = Item(
        name = ItemNamen.HEILTRANK,
        type = ItemType.POTION,
        description = "Heilt den Spieler",
        amount = 1,
        heal = 20,
        damage = 0,
        defense = 0
    )
    val healBigPotion = Item(
        name = ItemNamen.GROSSER_HEILTRANK,
        description = "Heilt den Spieler stark",
        amount = 1,
        type = ItemType.POTION,
        heal =  50,
        damage = 0,
        defense = 0
    )

}