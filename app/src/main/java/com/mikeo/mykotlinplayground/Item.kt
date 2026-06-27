package com.mikeo.mykotlinplayground

data class Item(
    val name: String,
    val description: String,
    val type: ItemType,
    val amount: Int,
    val damage: Int
)