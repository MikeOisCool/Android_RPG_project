package com.mikeo.mykotlinplayground

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class GameViewModel : ViewModel() {

    private val initialPlayer = Player(
        name = "Felix",
        hp = 100,
        maxHp = 100,
        attack = 10,
        inventory = Inventory(
            items = listOf(
                Item(
                    name = "Heiltrank",
                    description = "Heilt den Spieler",
                    amount = 5
                )
            )
        ),
        gold = 50,
        isDead = false,
        level = 1
    )


    private val _player = MutableStateFlow(initialPlayer)
    private val _log = MutableStateFlow<List<String>>(emptyList())

    private var healingInProgress = false


    val log: StateFlow<List<String>> = _log

    val player: StateFlow<Player> = _player


    private fun createRandomEnemy(playerLevel: Int): Enemy {
        val baseEnemy = listOf(
            Enemy(
                "Goblin",
                hp = 30,
                maxHp = 30,
                level = 1,
                damage = 5,
                goldReward = 20,
                xpReward = 25
            ),
            Enemy(
                "Wolf",
                hp = 40,
                maxHp = 40,
                level = 1,
                damage = 8,
                goldReward = 30,
                xpReward = 35
            ),
            Enemy(
                "Ork",
                hp = 60,
                maxHp = 60,
                level = 1,
                damage = 12,
                goldReward = 50,
                xpReward = 60
            ),
            Enemy(
                "Stier",
                hp = 1000000,
                maxHp = 1000000,
                level = 500,
                damage = 50,
                goldReward = 100,
                xpReward = 100
            )
        ).random()

        return createScaledEnemy(baseEnemy, playerLevel)
    }

    private val _enemy = MutableStateFlow(
        createRandomEnemy(_player.value.level)
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
                }
            }

            is GameEvent.AddGold -> {
                applyEvent(event)
                addLog("\uD83D\uDCB0 ${_player.value.name} hat ${_player.value.gold} Gold")
            }

            is GameEvent.UsePotion -> {
                val potionAmount =
                    player.value.inventory.items.find { it.name == ItemNamen.HEILTRANK }?.amount
                        ?: 0

                if (potionAmount <= 0) {
                    addLog("🧪 Keine Tränke mehr!")
                    return
                }
                val oldHp = _player.value.hp
                if (_player.value.hp >= _player.value.maxHp) {
                    addLog("❤️ Heiltrank hat keinen Effekt! HP ist bereits voll")
                    return
                }
                applyEvent(event)
                val healAmount = _player.value.hp - oldHp

                addLog("❤️ Heiltrank verwendet! ${_player.value.name} erhält $healAmount HP")
            }

            is GameEvent.UseBigPotion -> {
                val potionBigAmount =
                    player.value.inventory.items.find { it.name == ItemNamen.GROSSER_HEILTRANK }?.amount
                        ?: 0

                if (potionBigAmount <= 0) {
                    addLog("🧪 Keine großen Tränke mehr!")
                    return
                }
                val oldHp = _player.value.hp
                if (_player.value.hp >= _player.value.maxHp) {
                    addLog("❤️ Großer Heiltrank hat keinen Effekt! HP ist bereits voll")
                    return
                }
                applyEvent(event)
                val healAmount = _player.value.hp - oldHp

                addLog("❤️ Großer Heiltrank verwendet! ${_player.value.name} erhält $healAmount HP")
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
                addLog("⚔️ Waffe: ${_player.value.equippedWeapon} mit ${_player.value.attack} Attack")
            }


            is GameEvent.Flee -> {

                val goldVorher = _player.value.gold
                applyEvent(event)
                if (_player.value.gold == goldVorher) {
                    addLog("\uD83D\uDCB8 Nicht genug Gold zum Fliehen!")
                } else {
                    addLog("${_player.value.name} rennt davon, vor dem Kampf!")

                    val nextEnemy = createRandomEnemy(_player.value.level)
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


    private fun handleAttackEnemy() {

        val currentEnemy = _enemy.value

        if (enemyDodges(currentEnemy)) return

        val updatedEnemy = playerAttacksEnemy(currentEnemy)

        if (updatedEnemy.hp <= 0) {
            handleEnemyDefeated(currentEnemy)
            return
        }

        enemyAttacksPlayer(updatedEnemy)

    }


    private fun enemyAttacksPlayer(updatedEnemy: Enemy) {
        val dodgeChance = 10
        val enemyCritChance = 15 // 15 Prozent Wahrscheinlichkeit
        val critMultiplier = 2

        if (chance(dodgeChance)) {

            addLog(
                "🌀 ${_player.value.name} weicht dem Angriff von ${updatedEnemy.name} aus!"
            )
            return

        }

        val enemyDamage = calculateDamage(
            updatedEnemy.damage,
            enemyCritChance,
            critMultiplier
        )
        if (enemyDamage.second) {
            addLog("💥 KRITISCHER TREFFER! 👹 ${updatedEnemy.name} macht ${enemyDamage.first} Schaden!")
        } else {
            addLog(
                "👹 ${updatedEnemy.name} schlägt zurück für ${enemyDamage.first} Schaden!"
            )
        }
        applyEvent(
            GameEvent.TakeDamage(enemyDamage.first)
        )
        if (_player.value.isDead) {
            addLog("💀 ${_player.value.name} ist gestorben!")
            return
        }

    }

    private fun playerAttacksEnemy(currentEnemy: Enemy): Enemy {
        val playerCritChance = 20 /* 20 Prozent Wahrscheinlichkeit */
        val critMultiplier = 2

        val woodWeaponbonus =
            if (_player.value.equippedWeapon == ItemNamen.HOLZSCHWERT ) 15 else 0
        val playerDamage = calculateDamage(
            _player.value.attack + woodWeaponbonus,
            playerCritChance,
            critMultiplier
        )



        if (playerDamage.second) {
            addLog("💥 KRITISCHER TREFFER! ${_player.value.name} macht ${playerDamage.first} Schaden!")
        } else {
            addLog("⚔️ ${_player.value.name} trifft ${currentEnemy.name} für ${playerDamage.first} Schaden!")
        }

        val updatedEnemy = damageEnemy(
            currentEnemy,
            playerDamage.first
        )

        _enemy.value = updatedEnemy

        return updatedEnemy
    }

    private fun enemyDodges(enemy: Enemy): Boolean {
        val enemyDodgeChance = 10

        if (chance(enemyDodgeChance)) {
            addLog("💨 ${enemy.name} weicht dem Angriff von ${_player.value.name} aus!")
            return true
        }
        return false
    }

    private fun handleEnemyDefeated(enemy: Enemy) {

        val woodWeaponDropChance = 80
        val healDropChance = 30
        val potionsDropChance = 30
        val bigPotionDropChance = 20
        addLog("🏆 ${enemy.name} wurde besiegt!")

        if (chance(bigPotionDropChance)) dropBigPotion()

        if (chance(potionsDropChance)) dropPotion()

        if (chance(healDropChance)) healDrop()
        if (_player.value.level > 2) if (chance(woodWeaponDropChance)) dropWoodweapon()


        val levelVorher = _player.value.level

        applyEvent(GameEvent.AddGold(enemy.goldReward))
        applyEvent(GameEvent.GainXp(enemy.xpReward))

        addLog("💰 +${enemy.goldReward} Gold")
        addLog("🔥 +${enemy.xpReward} XP")


        if (_player.value.level > levelVorher) {
            addLog(
                "⭐ LEVEL UP! ${
                    _player.value
                        .name
                } ist jetzt Level ${_player.value.level}!"
            )
            addLog("💪 Max HP: ${_player.value.maxHp}")
            addLog(
                "❤️ HP vollständig aufgefüllt: ${
                    _player
                        .value.hp
                }/${_player.value.maxHp}"
            )
        } else {
            addLog(
                "🔥 XP: ${_player.value.xp}/${
                    _player.value
                        .xpToNextLevel
                }"
            )
        }

        spawnNextEnemy()
    }

    private fun spawnNextEnemy() {
        val nextEnemy = createRandomEnemy(_player.value.level)
        _enemy.value = nextEnemy

        addLog("👹 Ein neuer Gegner erscheint: ${nextEnemy.name} (${nextEnemy.hp} HP)!")
    }

    private fun healDrop() {

        val healDropValue = calculatePotionHeal(_player.value.level)
        val healedHp = (_player.value.hp + healDropValue)
            .coerceAtMost(_player.value.maxHp)
        _player.value = _player.value.copy(
            hp = healedHp
        )
        addLog(
            "❤️ ${
                _player.value
                    .name
            } erhält $healDropValue HP nach dem Kampf!"
        )

    }

    private fun dropWoodweapon() {

        val woodWeapon = _player.value.inventory.items.find {
            it.name == ItemNamen.HOLZSCHWERT
        }

        val newWeapon = Item(
            name = ItemNamen.HOLZSCHWERT,
            description = "Schaden + 15",
            amount = 1
        )

        if (woodWeapon != null) {
            addLog("Du hast schon ein Holzschwert! Waffe kann nicht genommen werden")
        } else {
            val newItems = _player.value.inventory.items
                .filter { it.name != ItemNamen.HOLZSCHWERT } +
                    newWeapon

            val updatedInventory =
                _player.value.inventory.copy(
                    items = newItems
                )
            _player.value = _player.value.copy(inventory = updatedInventory)

            addLog(
                "🧪 ${
                    _player.value
                        .name
                } \uD83D\uDDE1\uFE0F hat ein Holzschwert gefunden! Angriff +15"
            )
        }
    }

    private fun dropPotion() {


        val oldPotions =
            _player.value.inventory.items.find { it.name == ItemNamen.HEILTRANK }?.amount ?: 0
        val newPotions = (oldPotions + 1).coerceAtMost(10)

        val newPotion = Item(
            name = ItemNamen.HEILTRANK,
            description = "Heilt den Spieler",
            amount = newPotions
        )

        if (oldPotions >= 10) {
            addLog("🧪 Heiltrank inventar (10) voll, Trank kann nicht genommen werden!")
        } else {
            val newItems = _player.value.inventory.items
                .filter { it.name != ItemNamen.HEILTRANK } +
                    newPotion

            val updatedInventory =
                _player.value.inventory.copy(
                    items = newItems
                )
            _player.value = _player.value.copy(inventory = updatedInventory)

            addLog(
                "🧪 ${
                    _player.value
                        .name
                } erhält einen Heiltrank! ${oldPotions} -> $newPotions"
            )
        }
    }

    private fun dropBigPotion() {

        val oldBigPotions =
            _player.value.inventory.items.find { it.name == ItemNamen.GROSSER_HEILTRANK }?.amount
                ?: 0
        val newBigPotions = (oldBigPotions + 1).coerceAtMost(10)

        val newBigPotion = Item(
            name = ItemNamen.GROSSER_HEILTRANK,
            description = "Heilt den Spieler stark",
            amount = newBigPotions
        )

        if (oldBigPotions >= 10) {
            addLog("🧪 Großer Heiltrank inventar (10) voll, Trank kann nicht genommen werden!")
        } else {
            val newItems = _player.value.inventory.items
                .filter { it.name != ItemNamen.GROSSER_HEILTRANK } +
                    newBigPotion

            val updatedInventory =
                _player.value.inventory.copy(
                    items = newItems
                )
            _player.value = _player.value.copy(inventory = updatedInventory)

            addLog(
                "🧪 ${
                    _player.value
                        .name
                } erhält einen großen Heiltrank! ${oldBigPotions} -> $newBigPotions"
            )
        }
    }

    private fun addLog(message: String) {
        _log.value = _log.value + message
    }

    private fun applyEvent(event: GameEvent) {
        _player.value = handleEvent(_player.value, event)
    }

    fun resetGame() {
        _player.value = initialPlayer.copy(name = _player.value.name)
        _enemy.value = createRandomEnemy(_player.value.level)
        _log.value = emptyList()
    }
}



