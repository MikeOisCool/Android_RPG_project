package com.mikeo.mykotlinplayground

data class Item(
    val name: String,
    val article: String = "ein",
    val description: String,
    val type: ItemType,
    val amount: Int,
    val heal: Int,
    val damage: Int,
    val defense: Int
)