package com.mikeo.mykotlinplayground


object GameItems {
    val woodWeapon = Item(
        name = ItemNamen.HOLZSCHWERT,
        article = "ein",
        type = ItemType.WEAPON,
        description = "Schaden +15",
        amount = 1,
        heal = 0,
        damage = 15,
        defense = 0
    )
    val ironWeapon = Item(
        name = ItemNamen.EISENSCHWERT,
        article = "ein",
        type = ItemType.WEAPON,
        description = "Schaden +35",
        amount = 1,
        heal = 0,
        damage = 35,
        defense = 0
    )

    val silverWeapon = Item(
        name = ItemNamen.SILBERSCHWERT,
        article = "ein",
        type = ItemType.WEAPON,
        description = "Schaden +50",
        amount = 1,
        heal = 0,
        damage = 50,
        defense = 0
    )

    val goldenWeapon = Item(
        name = ItemNamen.GOLDENESSCHWERT,
        article = "ein",
        type = ItemType.WEAPON,
        description = "Schaden +75",
        amount = 1,
        heal = 0,
        damage = 75,
        defense = 0
    )


    val diamondWeapon = Item(
        name = ItemNamen.DIAMANTENSCHWERT,
        article = "ein",
        type = ItemType.WEAPON,
        description = "Schaden +100",
        amount = 1,
        heal = 0,
        damage = 100,
        defense = 0
    )
    val simpleArmor = Item(
        name = ItemNamen.EINFACHE_RÜSTUNG,
        article = "eine",
        type = ItemType.ARMOR,
        description = "Verteidigung +10",
        amount = 1,
        heal = 0,
        damage = 0,
        defense = 10
    )

    val ironArmor = Item(
        name = ItemNamen.EISEN_RÜSTUNG,
        article = "eine",
        type = ItemType.ARMOR,
        description = "Verteidigung +20",
        amount = 1,
        heal = 0,
        damage = 0,
        defense = 20
    )
    val healPotion = Item(
        name = ItemNamen.HEILTRANK,
        article = "ein",
        type = ItemType.POTION,
        description = "Heilung +20",
        amount = 1,
        heal = 20,
        damage = 0,
        defense = 0
    )
    val healBigPotion = Item(
        name = ItemNamen.GROSSER_HEILTRANK,
        article = "einen",
        description = "Heilung +25",
        amount = 1,
        type = ItemType.POTION,
        heal =  50,
        damage = 0,
        defense = 0
    )

}