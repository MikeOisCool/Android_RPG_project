package com.mikeo.mykotlinplayground

data class Enemy(
    val name: String,
    val hp: Int,
    val maxHp: Int,
    val level: Int,
    val damage: Int,
    val goldReward: Int,
    val xpReward: Int
)