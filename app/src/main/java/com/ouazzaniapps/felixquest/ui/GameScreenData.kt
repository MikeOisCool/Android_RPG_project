package com.ouazzaniapps.felixquest.ui

import com.ouazzaniapps.felixquest.Player
import com.ouazzaniapps.felixquest.calculateAttack

fun getItemAmount(
    player: Player,
    itemName: String
): Int {
    return player.inventory.items.find {
        it.name == itemName
    }?.amount ?: 0
}

fun totalDefense (
    player: Player
): Int {
    val armorDefense = player.equippedArmor?.defense ?: 0
    val weaponDefense = player.equippedWeapon?.defense ?: 0
    return armorDefense + weaponDefense
}

fun totalAttack (
    player: Player
): Int {
    return calculateAttack(
        baseAttack = player.attack,
        weaponBonus = player.equippedWeapon?.damage ?: 0
    )
}

