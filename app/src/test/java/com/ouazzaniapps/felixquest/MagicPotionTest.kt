package com.ouazzaniapps.felixquest

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class MagicPotionTest {

    @Test
    fun criticalPotionActivatesFiveCriticalAttacksAndIsConsumed() {
        val updated = usePotion(GameItems.critPotion)

        assertEquals(5, updated.guaranteedCritsRemaining)
        assertPotionConsumed(updated, GameItems.critPotion)
    }

    @Test
    fun criticalProtectionPotionActivatesAndIsConsumed() {
        val updated = usePotion(GameItems.critProtectionPotion)

        assertEquals(5, updated.critProtectionRemaining)
        assertPotionConsumed(updated, GameItems.critProtectionPotion)
    }

    @Test
    fun dodgePotionActivatesAndIsConsumed() {
        val updated = usePotion(GameItems.dodgePotion)

        assertEquals(5, updated.guaranteedDodgesRemaining)
        assertPotionConsumed(updated, GameItems.dodgePotion)
    }

    @Test
    fun accuracyPotionActivatesAndIsConsumed() {
        val updated = usePotion(GameItems.accuracyPotion)

        assertEquals(5, updated.unavoidableAttacksRemaining)
        assertPotionConsumed(updated, GameItems.accuracyPotion)
    }

    @Test
    fun usingMagicPotionWithoutHavingItDoesNothing() {
        val player = samplePlayer()

        val updated = handleEvent(
            player,
            GameEvent.UseMagicPotion(ItemNamen.KRIT_TRANK)
        )

        assertEquals(player, updated)
    }

    @Test
    fun deadPlayerCannotUseMagicPotion() {
        val player = samplePlayer(
            inventory = Inventory(listOf(GameItems.critPotion)),
            isDead = true
        )

        val updated = handleEvent(
            player,
            GameEvent.UseMagicPotion(ItemNamen.KRIT_TRANK)
        )

        assertEquals(player, updated)
    }

    private fun usePotion(potion: Item): Player {
        val player = samplePlayer(inventory = Inventory(listOf(potion)))

        return handleEvent(
            player,
            GameEvent.UseMagicPotion(potion.name)
        )
    }

    private fun assertPotionConsumed(updated: Player, potion: Item) {
        assertFalse(updated.inventory.items.any { it.name == potion.name })
    }

    private fun samplePlayer(
        inventory: Inventory = Inventory(emptyList()),
        isDead: Boolean = false
    ) = Player(
        name = "Felix",
        inventory = inventory,
        hp = 80,
        gold = 100,
        isDead = isDead
    )
}