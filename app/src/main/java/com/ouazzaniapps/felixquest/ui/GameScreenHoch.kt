package com.ouazzaniapps.felixquest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ouazzaniapps.felixquest.Enemy
import com.ouazzaniapps.felixquest.GameEvent
import com.ouazzaniapps.felixquest.GameViewModel
import com.ouazzaniapps.felixquest.ItemNamen
import com.ouazzaniapps.felixquest.Player
import kotlinx.coroutines.delay

@Composable
fun GameScreenHoch(
    debugMode: Boolean,
    viewModel: GameViewModel,
    listState: LazyListState,
    onGameOver: () -> Unit,
    onInventory: () -> Unit,
    onShop: () -> Unit,
    onQuest: () -> Unit
) {
    val player by viewModel.player.collectAsState()
    val log by viewModel.log.collectAsState()
    val enemy by viewModel.enemy.collectAsState()
    val scrollState = rememberScrollState()
    var playerAttacks by remember { mutableStateOf(false) }
    var enemyAttacks by remember { mutableStateOf(false) }
    val rightBattleText by viewModel.rightBattleText.collectAsState()
    val leftBattleText by viewModel.leftBattleText.collectAsState()
    val attackInProgress by viewModel.attackInProgress.collectAsState()
    val canClickAttackButton = !attackInProgress && enemy.hp > 0 && !player.isDead
    val onAttack = {
        if (canClickAttackButton) {
            playerAttacks = true
            viewModel.onEvent(GameEvent.AttackEnemy)
        }
    }
    val onFlee = {
        if (canClickAttackButton) {
            viewModel.onEvent(GameEvent.Flee)
        }
    }

    val potionBigAmount = player.inventory.items.find {
        it.name == ItemNamen.GROSSER_HEILTRANK
    }?.amount ?: 0

    val potionAmount = player.inventory.items.find {
        it.name == ItemNamen.HEILTRANK
    }?.amount ?: 0

    LaunchedEffect(log.size) {
        if (log.isNotEmpty()) listState.animateScrollToItem(log.size - 1)
    }

    LaunchedEffect(player.isDead) {
        if (player.isDead) {
            delay(1200)
            onGameOver()
        }
    }

    BattleAnimationEffects(
        playerAttacks = playerAttacks,
        enemyAttacks = enemyAttacks,
        enemyHp = enemy.hp,
        onPlayerAttackFinished = { playerAttacks = false },
        onEnemyAttackStarted = { enemyAttacks = true },
        onEnemyAttackFinished = { enemyAttacks = false }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color(0xFF4CAF50))
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            GameLog(log = log, listState = listState, modifier = Modifier.height(110.dp))

            PlayerStatsBlock(player = player)

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                GameActionButtons(
                    debugMode = debugMode,
                    canClickAttackButton = canClickAttackButton,
                    onInventory = onInventory,
                    onShop = onShop,
                    onQuest = onQuest,
                    onAttack = onAttack,
                    onTakeDamage = { viewModel.onEvent(GameEvent.TakeDamage()) },
                    onAddGold = { viewModel.onEvent(GameEvent.AddGold()) },
                    onHeal = { viewModel.onEvent(GameEvent.Heal()) },
                    onGainXp = { viewModel.onEvent(GameEvent.GainXp()) },
                    onUsePotion = { viewModel.onEvent(GameEvent.UsePotion()) },
                    onUseBigPotion = { viewModel.onEvent(GameEvent.UseBigPotion()) },
                    potionBigAmount = potionBigAmount,
                    potionAmount = potionAmount
                )

                EnemyStatsBlockHoch(enemy = enemy)

                GameButtonHoch(
                    text = "Fliehen",
                    fontSize = 24.sp,
                    onClick = onFlee
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BattleScene(
            layoutScene = BattleSceneLayout(
                playerOnGroundOffsetY = 10,
                playerAttackMoveX = 180,
                enemyOnGroundOffsetY = 10,
                enemyAttackMoveX = 180
            ),
            layoutSky = BattleSkyLayout(),
            playerName = player.name,
            playerHp = player.hp,
            playerMaxHp = player.maxHp,
            enemyName = enemy.name,
            enemyHp = enemy.hp,
            enemyMaxHp = enemy.maxHp,
            playerAttacks = playerAttacks,
            enemyAttacks = enemyAttacks,
            rightBattleText = rightBattleText,
            leftBattleText = leftBattleText,
            onEnemyClick = onAttack,
            onSunClick = onFlee,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(230.dp)
                .padding(bottom = 16.dp)
        )
    }
}


@Composable
fun GameActionButtons(
    debugMode: Boolean,
    canClickAttackButton: Boolean,
    onInventory: () -> Unit,
    onShop: () -> Unit,
    onQuest: () -> Unit,
    onAttack: () -> Unit,
    onTakeDamage: () -> Unit,
    onAddGold: () -> Unit,
    onHeal: () -> Unit,
    onGainXp: () -> Unit,
    onUsePotion: () -> Unit,
    onUseBigPotion: () -> Unit,
    potionBigAmount: Int,
    potionAmount: Int
) {

    val attackButtonHeight = if (debugMode) 70.dp else 85.dp
    val normalButtonFontSize = if (debugMode) 18.sp else 20.sp
    val attackButtonFontSize = if (debugMode) 24.sp else 24.sp

    if (debugMode) {
        Row {
            GameButtonHoch(
                text = "Take Damage",
                onClick = { onTakeDamage() })

            GameButtonHoch(
                text = "Add Gold", onClick = onAddGold
            )
        }
    }
    Row {
        GameButtonHoch(
            text = "Quest",
            fontSize = normalButtonFontSize,
            onClick = onQuest
        )
        if (debugMode) {
            GameButtonHoch(
                text = "Heilen",
                onClick = onHeal
            )
        }
        GameButtonHoch(
            text = "Big Heal (${potionBigAmount})",
            fontSize = normalButtonFontSize,
            onClick = onUseBigPotion
        )
    }

    Row {
        if (debugMode) {
            GameButtonHoch(
                text = "XP sammeln", onClick = { onGainXp() })
        }
        GameButtonHoch(
            text = "Shop öffnen",
            fontSize = normalButtonFontSize,
            onClick = {
                onShop()
            })
    }

    Row(
        modifier = Modifier.padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        GameButtonHoch(
            text = "Heiltrank (${potionAmount})",
            fontSize = normalButtonFontSize,
            onClick = {
                onUsePotion()
            })

        GameButtonHoch(
            text = "Inventar öffnen",
            fontSize = normalButtonFontSize,
            onClick = {
                onInventory()
            })
    }
    GameButtonHoch(
        text = "Angreifen",
        fontSize = attackButtonFontSize,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(attackButtonHeight),
        containerColor = if (canClickAttackButton) Color.Red else Color(0xff9e9e9e),
        onClick = onAttack
    )
}

@Composable
fun EnemyStatsBlockHoch(
    enemy: Enemy
) {
    Text("Gegner: ${enemy.name}", fontSize = 16.sp)
    Text("Level: ${enemy.level}")
    StatRow(label = "ATK:", value = "${enemy.attack}")
    StatRow(label = "DEF:", value = "${enemy.defense}")

    Text("HP: ${enemy.hp}")

    HpBar(
        currentHp = enemy.hp,
        maxHp = enemy.maxHp,
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun StatRow(label: String, value: String, labelWidth: Dp = 60.dp) {
    Row {
        Text(label, modifier = Modifier.width(labelWidth))
        Text(value)
    }
}

@Composable
fun PlayerStatsBlock(
    player: Player
) {

    Text(
        text = "Name: ${player.name} Level: ${player.level}", fontSize = 24.sp
    )
    Text(text = "HP: ${player.hp}/${player.maxHp} Gold: ${player.gold}", fontSize = 20.sp)

    HpBar(
        currentHp = player.hp,
        maxHp = player.maxHp,
        modifier = Modifier
            .fillMaxWidth()
            .height(16.dp)
            .padding(horizontal = 16.dp)
    )

    Text(
        text = "XP: ${player.xp}/${player.xpToNextLevel}", fontSize = 18.sp
    )

    Column {
        val weaponBonus = player.equippedWeapon?.damage ?: 0
        val armorDefense = player.equippedArmor?.defense ?: 0
        Row {
            Text("Angriff:", modifier = Modifier.width(120.dp))
            Text("${player.attack + weaponBonus}")
        }
        Row {
            Text("Verteidigung:", modifier = Modifier.width(120.dp))
            Text("$armorDefense")
        }
        Row {
            Text("Waffe:", modifier = Modifier.width(120.dp))
            Text(player.equippedWeapon?.name ?: "Keine")
        }
        Row {
            Text("Rüstung:", modifier = Modifier.width(120.dp))
            Text(player.equippedArmor?.name ?: "Keine")
        }
    }
}


@Preview(
    name = "Game Screen Hoch", showBackground = true
)
@Composable
fun GameScreenHochPreview() {
    GameScreenHoch(
        debugMode = true,
        viewModel = GameViewModel(),
        listState = rememberLazyListState(),
        onGameOver = {},
        onInventory = {},
        onShop = {},
        onQuest = {}
    )
}

@Preview(
    name = "Battle Scene", showBackground = true
)
@Composable
fun BattleScenePreview() {
    BattleScene(
        layoutScene = BattleSceneLayout(
            enemyOnGroundOffsetY = 17, playerOnGroundOffsetY = 17
        ),
        playerName = "Felix",
        playerHp = 80,
        playerMaxHp = 100,
        enemyName = "Wolf",
        enemyHp = 20,
        enemyMaxHp = 30
    )
}
