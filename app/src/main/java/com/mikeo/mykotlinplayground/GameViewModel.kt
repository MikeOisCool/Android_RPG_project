package com.mikeo.mykotlinplayground

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val initialPlayer = Player(
        name = "Felix", hp = 100, maxHp = 100, attack = 10, inventory = Inventory(
            items = listOf(
                GameItems.healPotion
            )
        ), gold = 50, isDead = false, level = 1
    )
    private val _player = MutableStateFlow(initialPlayer)
    private val _log = MutableStateFlow<List<String>>(emptyList())

    private var healingInProgress = false
    val log: StateFlow<List<String>> = _log
    val player: StateFlow<Player> = _player
    private val playerDodgeChance = 10
    private val enemyDodgeChance = 15
    private val enemyCritChance = 15 // 15 Prozent Wahrscheinlichkeit
    private val playerCritMultiplier = 4
    private val enemyCritMultiplier = 2
    private val playerCritChance = 20 /* 20 Prozent Wahrscheinlichkeit */

    private val weaponDropChance = 80
    private val armorDropChance = 50
    private val healDropChance = 30
    private val potionDropChance = 30
    private val bigPotionDropChance = 20
    private val baseHeal = 25
    private val _enemy = MutableStateFlow(
        EnemyFactory.createRandomEnemy(_player.value.level)
    )
    val enemy: StateFlow<Enemy> = _enemy


    fun startGame(name: String) {
        _player.value = initialPlayer.copy(name = name)
    }

    fun onEvent(event: GameEvent) {

        when (event) {
            is GameEvent.TakeDamage -> {

                applyEvent(event)
                addLog("\uD83D\uDC80 ${_player.value.name} hat ${_player.value.hp} HP übrig")
                if (_player.value.isDead) {
                    addLog("\uD83D\uDC80 ${_player.value.name} ist gestorben")
                    addLog("${_player.value.name} hat das Level ${_player.value.level} erreicht und hat ${_player.value.xp} XP! Sein Gold: ${_player.value.gold}")
                }
            }

            is GameEvent.AddGold -> {
                applyEvent(event)
                addLog("\uD83D\uDCB0 ${_player.value.name} hat ${_player.value.gold} Gold")
            }

            is GameEvent.UsePotion -> {
                usePotionWithLogs(
                    event, ItemNamen.HEILTRANK, "kleinen", "Der kleine", "Ein"
                )
            }

            is GameEvent.UseBigPotion -> {
                usePotionWithLogs(
                    event, ItemNamen.GROSSER_HEILTRANK, "großen", "Großer", "Einen großen"
                )
            }

            is GameEvent.Heal -> {

                if (healingInProgress) {
                    addLog("💁 Heilung läuft bereits!")
                    return
                }

                healingInProgress = true

                viewModelScope.launch {
                    try {
                        addLog("💚 Heilung startet...")
                        delay(1000)

                        val oldHp = _player.value.hp

                        if (oldHp >= _player.value.maxHp) {
                            addLog("💚 Bereits volle HP!")
                            return@launch
                        }
                        if (_player.value.gold < event.amount) {
                            addLog("💰 Nicht genug Gold zum Heilen!")
                            return@launch
                        }

                        applyEvent(event)

                        val newHp = _player.value.hp
                        val healedAmount = newHp - oldHp

                        addLog("💚 +$healedAmount HP geheilt")
                        addLog("💁 ${_player.value.name} hat jetzt $newHp/${_player.value.maxHp} HP")

                    } finally {
                        healingInProgress = false
                    }
                }
            }

            is GameEvent.EquipWeapon -> {
                applyEvent(event)

                val totalAttack = _player.value.attack + (_player.value.equippedWeapon?.damage ?: 0)

                addLog(
                    "⚔️ ${_player.value.equippedWeapon?.name} ausgerüstet! Angriff: $totalAttack"
                )
            }

            is GameEvent.EquipArmor -> {
                applyEvent(event)

                val armorDefense = event.armor.defense
                addLog("🛡️ Rüstung: ${event.armor.name} mit +$armorDefense Verteidigung ausgerüstet")
            }

            is GameEvent.Flee -> {

                val goldVorher = _player.value.gold
                applyEvent(event)
                if (_player.value.gold == goldVorher) {
                    addLog("\uD83D\uDCB8 Nicht genug Gold zum Fliehen!")
                } else {
                    addLog("${_player.value.name} rennt davon, vor dem Kampf!")

                    val nextEnemy = EnemyFactory.createRandomEnemy(_player.value.level)
                    _enemy.value = nextEnemy

                    addLog("\uD83D\uDC79 Neuer Gegner erscheint: ${nextEnemy.name} mit ${nextEnemy.hp} HP!")
                }
            }

            is GameEvent.GainXp -> {
                val levelVorher = _player.value.level
                applyEvent(event)
                if (_player.value.level > levelVorher) {
                    addLog("⭐ LEVEL UP! ${_player.value.name} ist jetzt Level ${_player.value.level}!")
                    addLog("💪 Max HP: ${_player.value.maxHp}")
                } else {
                    addLog("🔥 ${_player.value.name} hat ${_player.value.xp}/${_player.value.xpToNextLevel} XP")
                }
            }

            is GameEvent.AttackEnemy -> {
                handleAttackEnemy()
            }
        }
    }

    private fun usePotionWithLogs(
        event: GameEvent,
        itemName: String,
        emptyLogText: String,
        fullHpLogText: String,
        usedLogText: String
    ) {
        val potionAmount = player.value.inventory.items.find { it.name == itemName }?.amount ?: 0

        if (potionAmount <= 0) {
            addLog("🧪 Keine $emptyLogText Tränke mehr!")
            return
        }
        val oldHp = _player.value.hp
        if (_player.value.hp >= _player.value.maxHp) {
            addLog("❤️ $fullHpLogText Heiltrank hat keinen Effekt! HP ist bereits voll")
            return
        }
        applyEvent(event)
        val healAmount = _player.value.hp - oldHp

        addLog("❤️ $usedLogText Heiltrank verwendet! ${_player.value.name} erhält $healAmount HP")
    }

    private fun handleAttackEnemy() {

        val currentEnemy = _enemy.value

        val enemyDodgeLog =
            "💨 ${currentEnemy.name} weicht dem Angriff von ${_player.value.name} aus!"
        if (dodges(dodgeChance = enemyDodgeChance, logMessage = enemyDodgeLog)) return

        val updatedEnemy = playerAttacksEnemy(currentEnemy)

        if (updatedEnemy.hp <= 0) {
            handleEnemyDefeated(currentEnemy)
            return
        }
        enemyAttacksPlayer(updatedEnemy)
    }

    private fun enemyAttacksPlayer(updatedEnemy: Enemy) {

        val playerDodgeLog = "💨 ${_player.value.name} weicht dem Angriff von ${updatedEnemy.name} aus!"
        if (dodges(dodgeChance = playerDodgeChance, logMessage = playerDodgeLog)) return

        val defense = _player.value.equippedArmor?.defense ?: 0
        val baseDamage = (updatedEnemy.attack - defense).coerceAtLeast(0)
        val enemyDamage = calculateDamage(
            baseDamage, enemyCritChance, enemyCritMultiplier
        )
        if (enemyDamage.second) {
            addLog("💥 KRITISCHER TREFFER! 👹 ${updatedEnemy.name} macht ${enemyDamage.first} Schaden!")
        } else {
            addLog(
                "👹 ${updatedEnemy.name} schlägt zurück für ${enemyDamage.first} Schaden!"
            )
        }
        onEvent(
            GameEvent.TakeDamage(enemyDamage.first)
        )
    }

    private fun playerAttacksEnemy(currentEnemy: Enemy): Enemy {

        val weaponBonus = _player.value.equippedWeapon?.damage ?: 0
        val playerDamage = calculateDamage(
            _player.value.attack + weaponBonus, playerCritChance, playerCritMultiplier
        )
        if (playerDamage.second) {
            addLog("💥 KRITISCHER TREFFER! ${_player.value.name} macht ${playerDamage.first} Schaden!")
        } else {
            addLog("⚔️ ${_player.value.name} trifft ${currentEnemy.name} für ${playerDamage.first} Schaden!")
        }
        val updatedEnemy = damageEnemy(
            currentEnemy, playerDamage.first
        )
        _enemy.value = updatedEnemy

        return updatedEnemy
    }

    private fun dodges(
        dodgeChance: Int,
        logMessage: String
    ): Boolean {

        if (chance(dodgeChance)) {
            addLog(logMessage)
            return true
        }
        return false
    }

    private fun handleEnemyDefeated(enemy: Enemy) {
        addLog("🏆 ${enemy.name} wurde besiegt!")

        handlePotionDrops()
        handleEquipmentDrops()
        rewardPlayer(enemy)
        spawnNextEnemy()
    }

    private fun rewardPlayer(enemy: Enemy) {
        val levelVorher = _player.value.level

        applyEvent(GameEvent.AddGold(enemy.goldReward))
        applyEvent(GameEvent.GainXp(enemy.xpReward))

        addLog("💰 +${enemy.goldReward} Gold")
        addLog("🔥 +${enemy.xpReward} XP")

        if (_player.value.level > levelVorher) {
            addLog(
                "⭐ LEVEL UP! ${
                    _player.value.name
                } ist jetzt Level ${_player.value.level}!"
            )
            addLog("💪 Max HP: ${_player.value.maxHp}")
            addLog(
                "❤️ HP vollständig aufgefüllt: ${
                    _player.value.hp
                }/${_player.value.maxHp}"
            )
        } else {
            addLog(
                "🔥 XP: ${_player.value.xp}/${
                    _player.value.xpToNextLevel
                }"
            )
        }
    }

    private fun handleEquipmentDrops() {

        val weaponDrop = when (_player.value.level) {
            in 1..2 -> GameItems.woodWeapon
            in 3..4 -> GameItems.ironWeapon
            in 5..6 -> GameItems.silverWeapon
            in 7..8 -> GameItems.goldenWeapon
            in 9..10 -> GameItems.diamondWeapon
            else -> null
        }

        val armorDrop = when (_player.value.level) {
            in 0..3 -> GameItems.simpleArmor
            in 4..5 -> GameItems.ironArmor
            else -> null
        }

        weaponDrop?.let { item ->
            if (chance(weaponDropChance)) {
                applyDrop(item)
            }
        }
        armorDrop?.let { item ->
            if (chance(armorDropChance)) {
                applyDrop(item)
            }
        }
    }

    private fun handlePotionDrops() {

        if (chance(bigPotionDropChance)) applyDrop(
            GameItems.healBigPotion
        )

        if (chance(potionDropChance)) applyDrop(
            GameItems.healPotion
        )

        if (chance(healDropChance)) healDrop()
    }

    private fun spawnNextEnemy() {

        val nextEnemy = EnemyFactory.createRandomEnemy(_player.value.level)
        _enemy.value = nextEnemy

        addLog("👹 Ein neuer Gegner erscheint: ${nextEnemy.name} (${nextEnemy.hp} HP)!")
    }

    private fun healDrop() {

        val healDropValue = calculateItemHeal(baseHeal, _player.value.level)
        val healedHp = (_player.value.hp + healDropValue).coerceAtMost(_player.value.maxHp)
        _player.value = _player.value.copy(
            hp = healedHp
        )
        addLog(
            "❤️ ${
                _player.value.name
            } erhält $healDropValue HP nach dem Kampf!"
        )
    }

    private fun applyDrop(item: Item) {

        val result = when (item.type) {
            ItemType.WEAPON, ItemType.ARMOR -> DropManager.dropUniqueItem(_player.value, item)
            ItemType.POTION -> DropManager.dropStackableItem(_player.value, item)
        }
        _player.value = result.player

        result.logs.forEach {
            addLog(it)
        }
    }

    private fun addLog(message: String) {
        _log.value = _log.value + message
        Log.d("LOG", "Loggröße: ${_log.value.size} | letzter Eintrag: $message")
    }

    fun fillPreviewLog() {
        repeat(10) {
            addLog("Logeintrag ${it + 1}")
        }
    }

    private fun applyEvent(event: GameEvent) {
        _player.value = handleEvent(_player.value, event)
    }

    fun resetGame() {
        _player.value = initialPlayer.copy(name = _player.value.name)
        _enemy.value = EnemyFactory.createRandomEnemy(_player.value.level)
        _log.value = emptyList()
    }
}
