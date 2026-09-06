package com.mikeo.felixquest.ui

import com.mikeo.felixquest.Item
import com.mikeo.felixquest.ItemType


fun itemIcon(item: Item): String {
    return when (item.type) {
        ItemType.POTION -> "🧪"
        ItemType.WEAPON -> "⚔️"
        ItemType.ARMOR -> "🛡️"
    }
}