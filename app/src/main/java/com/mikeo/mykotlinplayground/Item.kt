package com.mikeo.mykotlinplayground

data class Item(
    val name: String,
    val article: String = "ein",
    val description: String,
    val type: ItemType,
    val itemPrice: Int = 1,
    val amount: Int,
    val heal: Int,
    val damage: Int,
    val defense: Int
){
    override fun toString(): String {
        return "$article $name"
    }
}