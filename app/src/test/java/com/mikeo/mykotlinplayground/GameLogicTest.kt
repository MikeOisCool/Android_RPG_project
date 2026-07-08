package com.mikeo.mykotlinplayground

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class GameLogicTest {

    @Test
    fun usePotionHealsPlayerAndRemovesLastPotion() {
        val player = testPlayer(
            hp = 50,
            inventory = Inventory(items = listOf(GameItems.healPotion))
        )

        val updatedPlayer = handleEvent(player, GameEvent.UsePotion())

        assertEquals(70, updatedPlayer.hp)
        assertEquals(emptyList<Item>(), updatedPlayer.inventory.items)
    }

    @Test
    fun useBigPotionScalesHealWithPlayerLevel1() {
        val player = testPlayer(
            hp = 20,
            inventory = Inventory(items = listOf(GameItems.healBigPotion))
        )

        val updatedPlayer = handleEvent(player, GameEvent.UseBigPotion())

        assertEquals(70, updatedPlayer.hp)
        assertEquals(emptyList<Item>(), updatedPlayer.inventory.items)
    }

    @Test
    fun useBigPotionScalesHealWithPlayerLevel3() {
        val player = testPlayer(
            hp = 20,
            maxHp = 120,
            level = 3,
            inventory = Inventory(items = listOf(GameItems.healBigPotion))
        )

        val updatedPlayer = handleEvent(player, GameEvent.UseBigPotion())

        assertEquals(84, updatedPlayer.hp)
        assertEquals(emptyList<Item>(), updatedPlayer.inventory.items)
    }

    @Test
    fun equipWeaponOnlyWorksWhenWeaponIsInInventory() {
        val player = testPlayer(inventory = Inventory(items = emptyList()))

        val updatedPlayer = handleEvent(player, GameEvent.EquipWeapon(GameItems.woodWeapon))

        assertNull(updatedPlayer.equippedWeapon)
    }

    @Test
    fun removeEquippedWeaponAlsoUnequipsIt() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.woodWeapon)),
            equippedWeapon = GameItems.woodWeapon
        )

        val updatedPlayer = handleEvent(
            player,
            GameEvent.RemoveInventoryItem(GameItems.woodWeapon)
        )

        assertNull(updatedPlayer.equippedWeapon)
        assertEquals(emptyList<Item>(), updatedPlayer.inventory.items)
    }

    @Test
    fun removeEquippedArmorAlsoUnequipsIt() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.simpleArmor)),
            equippedArmor = GameItems.simpleArmor
        )

        val updatedPlayer = handleEvent(
            player,
            GameEvent.RemoveInventoryItem(GameItems.simpleArmor)
        )

        assertNull(updatedPlayer.equippedArmor)
        assertEquals(emptyList<Item>(), updatedPlayer.inventory.items)
    }

    private fun testPlayer(
        hp: Int = 100,
        maxHp: Int = 100,
        level: Int = 1,
        inventory: Inventory = Inventory(items = emptyList()),
        equippedWeapon: Item? = null,
        equippedArmor: Item? = null
    ): Player {
        return Player(
            name = "Test",
            inventory = inventory,
            hp = hp,
            maxHp = maxHp,
            attack = 10,
            equippedWeapon = equippedWeapon,
            equippedArmor = equippedArmor,
            gold = 50,
            isDead = false,
            level = level
        )
    }
}
