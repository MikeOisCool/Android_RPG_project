package com.mikeo.mykotlinplayground

data class Enemy(
    val name: String,
    val hp: Int,
    val maxHp: Int,
    val level: Int,
    val attack: Int,
    val defense: Int,
    val goldReward: Int,
    val xpReward: Int
)