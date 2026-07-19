package com.mikeo.mykotlinplayground

import org.junit.Assert.assertTrue
import org.junit.Test


class GameViewModelTest {

    @Test
    fun buyUniqueItemAlreadyInInventoryWritesLog() {
        val viewModel = GameViewModel()

        viewModel.onEvent(GameEvent.BuyItem(GameItems.woodWeapon))
        viewModel.onEvent(GameEvent.BuyItem(GameItems.woodWeapon))

        val lastLog = viewModel.log.value.last()

        assertTrue(lastLog.contains("schon im Inventar"))
    }

    @Test
    fun sellEquippedWeaponWritesLog() {
        val viewModel = GameViewModel()
        viewModel.onEvent(GameEvent.BuyItem(GameItems.woodWeapon))
        viewModel.onEvent(GameEvent.EquipWeapon(GameItems.woodWeapon))
        viewModel.onEvent(GameEvent.SellItem(GameItems.woodWeapon))

        val lastLog = viewModel.log.value.last()

        assertTrue(lastLog.contains("${GameItems.woodWeapon} zuerst ab"))
    }
}