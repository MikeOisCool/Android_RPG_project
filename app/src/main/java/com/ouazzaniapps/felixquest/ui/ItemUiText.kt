package com.ouazzaniapps.felixquest.ui

import com.ouazzaniapps.felixquest.Item
import com.ouazzaniapps.felixquest.ItemType


fun itemIcon(item: Item): String {
    return when (item.type) {
        ItemType.POTION -> "🧪"
        ItemType.WEAPON -> "⚔️"
        ItemType.ARMOR -> "🛡️"
    }
}
