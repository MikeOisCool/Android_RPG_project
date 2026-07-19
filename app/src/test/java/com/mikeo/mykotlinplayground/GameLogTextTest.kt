package com.mikeo.mykotlinplayground

import org.junit.Assert.assertTrue
import org.junit.Test

class GameLogTextTest {

    @Test
    fun buyItemLogContainsItemAndPrice() {
        val log = buyItemLog(GameItems.woodWeapon, 10)

        assertTrue(log.contains(GameItems.woodWeapon.toString()))
        assertTrue(log.contains("10"))
    }

    @Test
    fun sellItemLogContainsItemAndPrice() {
        val log = sellItemLog(GameItems.woodWeapon, 10)

        assertTrue(log.contains(GameItems.woodWeapon.toString()))
        assertTrue(log.contains("10"))
    }

    @Test
    fun equippedItemSellBlockedLogContainsItem() {
        val log = equippedItemSellBlockedLog(GameItems.woodWeapon)

        assertTrue(log.contains(GameItems.woodWeapon.toString()))
        assertTrue(log.contains("zuerst ab"))
    }
}