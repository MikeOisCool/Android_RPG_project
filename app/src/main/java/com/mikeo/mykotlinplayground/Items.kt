package com.mikeo.mykotlinplayground


object GameItems {
    val woodWeapon = Item(
        name = ItemNamen.HOLZSCHWERT,
        type = ItemType.WEAPON,
        description = "Schaden + 15",
        amount = 1,
        damage = 15
    )
    val ironWeapon = Item(
        name = ItemNamen.EISENSCHWERT,
        type = ItemType.WEAPON,
        description = "Schaden + 35",
        amount = 1,
        damage = 35
    )

}