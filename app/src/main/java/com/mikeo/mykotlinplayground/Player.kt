package com.mikeo.mykotlinplayground

data class Player(
    val name: String,
    val inventory: Inventory,
    val hp: Int,
    val maxHp: Int = 100,
    val attack: Int = 10,
    val gold: Int,
    val isDead: Boolean,
    val level: Int,
    val xp: Int = 0,
    val xpToNextLevel: Int = 100
)

data class Enemy(
    val name: String,
    val hp: Int,
    val maxHp: Int,
    val level: Int,
    val damage: Int,
    val goldReward: Int,
    val xpReward: Int
)

data class Item(
    val name: String,
    val description: String,
    val amount: Int
)

data class Inventory(
    val items: List<Item>
)