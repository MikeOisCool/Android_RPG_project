package com.ouazzaniapps.felixquest

import kotlinx.serialization.Serializable

@Serializable
data class Inventory(
    val items: List<Item>
)
