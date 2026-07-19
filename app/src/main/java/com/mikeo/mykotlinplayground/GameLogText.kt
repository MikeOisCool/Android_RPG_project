package com.mikeo.mykotlinplayground


fun buyItemLog(item: Item, price: Int): String {
    return "\uD83D\uDED2 Du hast $item gekauft für den Preis von $price Gold"
}

fun sellItemLog(item: Item, price: Int): String {
    return "\uD83D\uDED2 Du verkaufst $item für den Preis von $price Gold"
}

fun equippedItemSellBlockedLog(item: Item): String {
    return "⚔️ Lege $item zuerst ab, bevor du es verkaufst!"
}
