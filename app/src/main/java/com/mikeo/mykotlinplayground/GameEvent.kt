package com.mikeo.mykotlinplayground

sealed class GameEvent {

    object AttackEnemy : GameEvent()

    data class TakeDamage(val amount: Int = 10) : GameEvent()

    data class AddGold(val amount: Int = 25) : GameEvent()

    data class Heal(val amount: Int = 10) : GameEvent()

    object Flee : GameEvent()

    data class GainXp(val amount: Int = 50) : GameEvent()

    data class UsePotion(val amount: Int = 1) : GameEvent()

    data class UseBigPotion(val amount: Int = 1) : GameEvent()

    data class EquipWeapon(val weapon: Item) : GameEvent()

    data class EquipArmor(val armor: Item) : GameEvent()

    data class RemoveInventoryItem(val item: Item) : GameEvent()

    object UnequipWeapon : GameEvent()

    object UnequipArmor : GameEvent()

    data class BuyItem(val item: Item) : GameEvent()
    data class SellItem(val item: Item) : GameEvent()
}

