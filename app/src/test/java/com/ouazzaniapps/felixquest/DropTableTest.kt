package com.ouazzaniapps.felixquest

import com.ouazzaniapps.felixquest.drop.DropTable
import com.ouazzaniapps.felixquest.item.GameItems
import com.ouazzaniapps.felixquest.item.Item
import org.junit.Assert.assertTrue
import org.junit.Test

class DropTableTest {

    @Test
    fun weaponDropMatchesPlayerLevel() {
        assertPossibleDrop(DropTable.rollWeapon(1), GameItems.woodWeapon)
        assertPossibleDrop(DropTable.rollWeapon(3), GameItems.ironWeapon)
        assertPossibleDrop(DropTable.rollWeapon(5), GameItems.silverWeapon)
        assertPossibleDrop(DropTable.rollWeapon(7), GameItems.goldenWeapon)
        assertPossibleDrop(DropTable.rollWeapon(9), GameItems.diamondWeapon)
    }

    @Test
    fun weaponDoesNotDropAboveSupportedLevel() {
        assertTrue(DropTable.rollWeapon(11) == null)
    }

    @Test
    fun armorDropMatchesPlayerLevel() {
        assertPossibleDrop(DropTable.rollArmor(1), GameItems.simpleArmor)
        assertPossibleDrop(DropTable.rollArmor(4), GameItems.ironArmor)
        assertPossibleDrop(DropTable.rollArmor(6), GameItems.silverArmor)
        assertPossibleDrop(DropTable.rollArmor(8), GameItems.goldenArmor)
        assertPossibleDrop(DropTable.rollArmor(10), GameItems.diamondArmor)
    }

    @Test
    fun armorDoesNotDropAboveSupportedLevel() {
        assertTrue(DropTable.rollArmor(12) == null)
    }

    @Test
    fun healPotionRollsReturnOnlyTheirPotionOrNull() {
        assertPossibleDrop(DropTable.rollHealPotion(), GameItems.healPotion)
        assertPossibleDrop(DropTable.rollBigHealPotion(), GameItems.healBigPotion)
    }

    @Test
    fun magicPotionRollReturnsOneOfTheFourMagicPotionsOrNull() {
        val possiblePotions = setOf(
            GameItems.critPotion,
            GameItems.critProtectionPotion,
            GameItems.dodgePotion,
            GameItems.accuracyPotion
        )

        repeat(100) {
            val result = DropTable.rollMagicPotion()
            assertTrue(result == null || result in possiblePotions)
        }
    }


    private fun assertPossibleDrop(actual: Item?, expected: Item) {
        assertTrue(actual == null || actual == expected)
    }
}