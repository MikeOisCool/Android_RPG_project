package com.mikeo.mykotlinplayground

import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class DropManagerTest {

    @Test
    fun dropStackableItemIncreasesExistingAmount() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.healPotion.copy(amount = 2)))
        )

        val result = DropManager.dropStackableItem(player, GameItems.healPotion)

        val potion = result.player.inventory.items.single()
        assertEquals(ItemNamen.HEILTRANK, potion.name)
        assertEquals(3, potion.amount)
    }

    @Test
    fun dropStackableItemDoesNotExceedTen() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.healPotion.copy(amount = 10)))
        )

        val result = DropManager.dropStackableItem(player, GameItems.healPotion)

        assertSame(player, result.player)
        assertEquals(10, result.player.inventory.items.single().amount)
        assertTrue(result.logs.single().contains("voll"))
    }

    @Test
    fun dropUniqueItemAddsNewWeapon() {
        val player = testPlayer()

        val result = DropManager.dropUniqueItem(player, GameItems.woodWeapon)

        assertEquals(listOf(GameItems.woodWeapon), result.player.inventory.items)
    }

    @Test
    fun dropUniqueItemDoesNotAddDuplicateWeapon() {
        val player = testPlayer(
            inventory = Inventory(items = listOf(GameItems.woodWeapon))
        )

        val result = DropManager.dropUniqueItem(player, GameItems.woodWeapon)

        assertSame(player, result.player)
        assertEquals(listOf(GameItems.woodWeapon), result.player.inventory.items)
        assertTrue(result.logs.single().contains("schon"))
    }

    private fun testPlayer(
        inventory: Inventory = Inventory(items = emptyList())
    ): Player {
        return Player(
            name = "Test",
            inventory = inventory,
            hp = 100,
            maxHp = 100,
            attack = 10,
            gold = 50,
            isDead = false,
            level = 1
        )
    }
}
