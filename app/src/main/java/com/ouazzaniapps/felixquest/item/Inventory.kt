package com.ouazzaniapps.felixquest.item

import kotlinx.serialization.Serializable

@Serializable
data class Inventory(
    val items: List<Item>
)
