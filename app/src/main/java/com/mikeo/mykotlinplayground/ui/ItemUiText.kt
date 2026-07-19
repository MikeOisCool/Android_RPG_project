package com.mikeo.mykotlinplayground.ui

import com.mikeo.mykotlinplayground.Item
import com.mikeo.mykotlinplayground.ItemType


fun itemIcon(item: Item): String {
    return when (item.type) {
        ItemType.POTION -> "🧪"
        ItemType.WEAPON -> "⚔️"
        ItemType.ARMOR -> "🛡️"
    }
}