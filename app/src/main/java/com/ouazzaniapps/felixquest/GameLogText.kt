package com.ouazzaniapps.felixquest

import com.ouazzaniapps.felixquest.item.Item


fun buyItemLog(item: Item, price: Int): String {
    return "\uD83D\uDED2 Du hast $item gekauft für den Preis von $price Gold"
}

fun sellItemLog(item: Item, price: Int): String {
    return "\uD83D\uDED2 Du verkaufst $item für den Preis von $price Gold"
}

fun equippedItemSellBlockedLog(item: Item): String {
    return "⚔️ Lege $item zuerst ab, bevor du es verkaufst!"
}


fun damageLog(
    attackerName: String,
    targetName: String,
    damage: Int,
    isCritical: Boolean
): String {
    val normalDamageLog =
        "⚔️ $attackerName trifft $targetName für $damage Schaden!"
    return if (isCritical) {
        "💥 KRITISCHER TREFFER! $normalDamageLog"
    } else {
        normalDamageLog
    }
}

fun dodgeLog(
    defenderName: String,
    attackerName: String
): String {
    return ("💨 $defenderName weicht dem Angriff von $attackerName aus!")
}