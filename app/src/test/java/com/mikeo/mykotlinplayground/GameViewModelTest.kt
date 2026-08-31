package com.mikeo.mykotlinplayground

import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse


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

    @Test
    fun attackEnemyDoesNothingWhenPlayerIsDead() {
        val viewModel = GameViewModel()

        viewModel.onEvent(GameEvent.TakeDamage(amount = 1001))

        val logSizeBeforeAttack = viewModel.log.value.size

        viewModel.onEvent(GameEvent.AttackEnemy)

        assertEquals(logSizeBeforeAttack, viewModel.log.value.size)
    }

    @Test
    fun resetGameResetsStartedQuests() {
        val wolfQuest = QuestName.allQuests.first {
            it.targetEnemyName == "Wolf"
        }

        val viewModel = GameViewModel()

        viewModel.onEvent(GameEvent.StartQuest(QuestName.allQuests.first()))
        viewModel.onEvent(GameEvent.StartQuest(QuestName.allQuests.last()))
        viewModel.onEvent(GameEvent.StartQuest(wolfQuest))

        viewModel.resetGame()

        assertTrue(viewModel.quests.value.none { it.isStarted })
    }

    @Test
    fun startQuestStartsOnlySelectedQuest() {
        val goblinQuest = QuestName.allQuests.first { it.targetEnemyName == "Goblin" }

        val viewModel = GameViewModel()


        viewModel.onEvent(GameEvent.StartQuest(goblinQuest))

        assertTrue(viewModel.quests.value.first { it.targetEnemyName == "Goblin" }.isStarted)
        assertFalse(viewModel.quests.value.first { it.targetEnemyName == "Stier" }.isStarted)
    }
}
