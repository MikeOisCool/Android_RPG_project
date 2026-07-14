package com.mikeo.mykotlinplayground

data class Player(
    val name: String,
    val inventory: Inventory,
    val hp: Int,
    val maxHp: Int = 100,
    val attack: Int = 10,
    val equippedWeapon: Item? = null,
    val equippedArmor: Item? = null,
    val gold: Int,
    val isDead: Boolean,
    val level: Int = 1,
    val xp: Int = 0,
    val xpToNextLevel: Int = 100
)





