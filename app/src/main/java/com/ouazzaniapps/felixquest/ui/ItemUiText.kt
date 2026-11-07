package com.ouazzaniapps.felixquest.ui

import com.ouazzaniapps.felixquest.item.Item
import com.ouazzaniapps.felixquest.item.ItemType


fun itemIcon(item: Item): String {
    return when (item.type) {
        ItemType.POTION -> "🧪"
        ItemType.WEAPON -> "⚔️"
        ItemType.WEAPONQUEST -> "🔮"
        ItemType.ARMOR -> "🛡️"
    }
}
