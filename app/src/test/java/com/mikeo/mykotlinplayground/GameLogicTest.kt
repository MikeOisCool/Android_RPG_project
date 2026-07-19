package com.mikeo.mykotlinplayground

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class GameLogicTest {

    @Test
    fun useAmountOfPotionFromOnetoTwo() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.healPotion))
        )

        val updatedPlayer = handleEvent(player, GameEvent.BuyItem(GameItems.healPotion))

        val potion = updatedPlayer.inventory.items.single()

        assertEquals(2, potion.amount)
        assertEquals(ItemNamen.HEILTRANK, potion.name)
    }

    @Test
    fun buyPotionIncreasesAmount() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.healPotion)),
            gold = 200
        )
        var updatedPlayer = player
        for (x in 1..12 ) {
            updatedPlayer = handleEvent(updatedPlayer, GameEvent.BuyItem(GameItems.healPotion))
        }
        val potion = updatedPlayer.inventory.items.single()
        assertEquals(10, potion.amount)

    }

    @Test
    fun potionStackFullReturnsTrueAtTen() {
        val inventory = Inventory(
            items = listOf(GameItems.healPotion.copy(amount = 10))
        )

        val result = isPotionStackFull(
            item = GameItems.healPotion,
            inventory = inventory
        )

        assertTrue(result)
    }

    @Test
    fun sellPotionIncreasesGold() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.healPotion)),
            gold = 200
        )
        val updatedPlayer = handleEvent(player, GameEvent.SellItem(GameItems.healPotion))

        assertEquals(205, updatedPlayer.gold)
    }

    @Test
    fun buyWithoutGoldDoesNothing() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.healPotion)),
            gold = 0
        )
        var updatedPlayer = player
        repeat (12) {
            updatedPlayer = handleEvent(updatedPlayer, GameEvent.BuyItem(GameItems.healPotion))
        }
        val potion = updatedPlayer.inventory.items.single()
        assertEquals(1, potion.amount)
    }

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
    fun hasItemInInventoryReturnsTrueWhenItemExists() {
        val inventory = Inventory(items = listOf(GameItems.woodWeapon))

        val result = hasItemInInventory(
            item = GameItems.woodWeapon,
            inventory = inventory
        )

        assertTrue(result)
    }

    @Test
    fun hasItemInInventoryReturnsFalseWhenItemIsMissing() {
        val inventory = Inventory(items = emptyList())

        val result = hasItemInInventory(
            item = GameItems.woodWeapon,
            inventory = inventory
        )

        assertFalse(result)
    }

    @Test
    fun buyWeaponDoesNotAddDuplicateWeapon() {
        val player = testPlayer(
            inventory = Inventory(items = emptyList()),
            gold = 100)
        var updatedPlayer = handleEvent(player, GameEvent.BuyItem(GameItems.woodWeapon))
        updatedPlayer = handleEvent(updatedPlayer, GameEvent.BuyItem(GameItems.woodWeapon))
        updatedPlayer = handleEvent(updatedPlayer, GameEvent.BuyItem(GameItems.woodWeapon))

        val weapon = updatedPlayer.inventory.items.single()

        assertEquals(ItemNamen.HOLZSCHWERT, weapon.name)
        assertEquals(1, weapon.amount)
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
    fun sellWeaponIncreasesGoldAndRemovesIt() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.woodWeapon)),
        )
        val updatedPlayer = handleEvent(player, GameEvent.SellItem(GameItems.woodWeapon))
        assertEquals(55, updatedPlayer.gold)
        assertEquals(emptyList<Item>(), updatedPlayer.inventory.items)
    }

    @Test
    fun weaponUnequipsIt() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.woodWeapon)),
            equippedWeapon = GameItems.woodWeapon
        )

        val updatedPlayer = handleEvent(
            player,
            GameEvent.UnequipWeapon
        )

        assertNotNull(player.equippedWeapon)
        assertNull(updatedPlayer.equippedWeapon)
        assertEquals(listOf(GameItems.woodWeapon), updatedPlayer.inventory.items)
    }

    @Test
    fun armorUnequipsIt() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.simpleArmor)),
            equippedArmor = GameItems.simpleArmor
        )

        val updatedPlayer = handleEvent(
            player,
            GameEvent.UnequipArmor
        )

        assertNotNull(player.equippedArmor)
        assertNull(updatedPlayer.equippedArmor)
        assertEquals(listOf(GameItems.simpleArmor), updatedPlayer.inventory.items)
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

    @Test
    fun weaponIsWeaponOrArmor() {
        val result = isWeaponOrArmor(GameItems.woodWeapon)

        assertTrue(result)
    }

    @Test
    fun potionIsNotUniqueItemAlreadyInInventory() {
        val inventory = Inventory(items = listOf(GameItems.healPotion))

        val result = isUniqueItemAlreadyInInventory(
            item = GameItems.healPotion,
            inventory = inventory
        )

        assertFalse(result)
    }

    @Test
    fun equippedWeaponIsEquippedItem() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.woodWeapon)),
            equippedWeapon = GameItems.woodWeapon
        )

        val result = isEquippedItem(
            item = GameItems.woodWeapon,
            player = player
        )

        assertTrue(result)
    }

    private fun testPlayer(
        hp: Int = 100,
        maxHp: Int = 100,
        level: Int = 1,
        inventory: Inventory = Inventory(items = emptyList()),
        equippedWeapon: Item? = null,
        equippedArmor: Item? = null,
        gold: Int = 50
    ): Player {
        return Player(
            name = "TestPlayer",
            inventory = inventory,
            hp = hp,
            maxHp = maxHp,
            attack = 10,
            equippedWeapon = equippedWeapon,
            equippedArmor = equippedArmor,
            gold = gold,
            isDead = false,
            level = level
        )
    }
}
