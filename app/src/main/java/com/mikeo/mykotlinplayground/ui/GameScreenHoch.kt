package com.mikeo.mykotlinplayground.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikeo.mykotlinplayground.GameEvent
import com.mikeo.mykotlinplayground.GameViewModel
import com.mikeo.mykotlinplayground.ItemNamen
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.font.FontWeight
import com.mikeo.mykotlinplayground.Player
import com.mikeo.mykotlinplayground.Enemy


@Composable
fun GameScreenHoch(
    viewModel: GameViewModel,
    listState: LazyListState,
    onGameOver: () -> Unit,
    onInventory: () -> Unit,
    onShop: () -> Unit
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

    LaunchedEffect(playerAttacks) {
        if (playerAttacks) {
            delay(200)
            playerAttacks = false
            if (enemy.hp > 0) {
                enemyAttacks = true
            }
        }
    }

    LaunchedEffect(enemyAttacks) {
        if (enemyAttacks) {
            delay(200)
            enemyAttacks = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color(0xFF4CAF50))
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GameLog(log = log, listState = listState, modifier = Modifier.height(120.dp))


            PlayerStatsBlock(player = player)

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                GameActionButtons(
                    canClickAttackButton = canClickAttackButton,
                    onInventory = onInventory,
                    onShop = onShop,
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

                EnemyStatsBlock(enemy = enemy)

                GameButtonHoch(
                    text = "Fliehen", onClick = { viewModel.onEvent(GameEvent.Flee) })

                Spacer(modifier = Modifier.height(16.dp))

                BattleScene(
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
                    onEnemyClick = onAttack
                )
            }
        }
    }
}

@Composable
fun GameActionButtons(
    canClickAttackButton: Boolean,
    onInventory: () -> Unit,
    onShop: () -> Unit,
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
    Row {
        GameButtonHoch(
            text = "Take Damage", onClick = { onTakeDamage() })

        GameButtonHoch(
            text = "Add Gold", onClick = onAddGold
        )
    }
    Row {
        GameButtonHoch(
            text = "Heilen", onClick = onHeal
        )

        GameButtonHoch(
            text = "Big Heal (${potionBigAmount})", onClick = onUseBigPotion
        )
    }

    Row {
        GameButtonHoch(
            text = "XP sammeln", onClick = { onGainXp() })

        GameButtonHoch(
            text = "Shop öffnen", onClick = {
                onShop()
            })
    }

    Row(
        modifier = Modifier.padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        GameButtonHoch(
            text = "Heiltrank (${potionAmount})", onClick = {
                onUsePotion()
            })

        GameButtonHoch(
            text = "Inventar öffnen", onClick = {
                onInventory()
            })
    }
    GameButtonHoch(
        text = "Angreifen",
        fontSize = 24.sp,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(70.dp),
        containerColor = if (canClickAttackButton) Color.Red else Color(0xff9e9e9e),
        onClick = onAttack
    )
}

@Composable
fun EnemyStatsBlock(
    enemy: Enemy
) {
    Text("Gegner: ${enemy.name}", fontSize = 16.sp)
    Text("Level: ${enemy.level}")
    Row {
        Text("ATK:", modifier = Modifier.width(60.dp))
        Text("${enemy.attack}")
    }
    Row {
        Text("DEF:", modifier = Modifier.width(60.dp))
        Text("${enemy.defense}")
    }
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

@Composable
fun BattleScene(
    playerName: String,
    playerHp: Int,
    playerMaxHp: Int,
    enemyName: String,
    enemyHp: Int,
    enemyMaxHp: Int,
    rightBattleText: String? = null,
    leftBattleText: String? = null,
    playerAttacks: Boolean = false,
    enemyAttacks: Boolean = false,
    onEnemyClick: () -> Unit = {}
) {


    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(230.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFF8BC34A))

    ) {
        Box(
            modifier = Modifier
                .offset(y = (10).dp)
                .fillMaxWidth(0.95f)
                .align(Alignment.TopCenter)
                .height(40.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFFD54F))
        )
        Box(
            modifier = Modifier.padding(16.dp)
        ) {

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                BattleGround()

                BattleFighters(
                    enemyName = enemyName,
                    playerAttacks = playerAttacks,
                    enemyAttacks = enemyAttacks,
                    onEnemyClick = onEnemyClick
                )
            }
            BattleFeedbackTexts(
                rightBattleText = rightBattleText, leftBattleText = leftBattleText
            )

            BattleHpHeader(
                playerName = playerName,
                playerHp = playerHp,
                playerMaxHp = playerMaxHp,
                enemyName = enemyName,
                enemyHp = enemyHp,
                enemyMaxHp = enemyMaxHp
            )
        }
    }
}

@Composable
fun BoxScope.BattleFeedbackTexts(
    rightBattleText: String?, leftBattleText: String?
) {
    val rightBattleTextOffset by animateDpAsState(
        targetValue = if (rightBattleText != null) (-35).dp else (-20).dp,
        label = "rightBattleTextOffset"

    )

    val leftBattleTextOffset by animateDpAsState(
        targetValue = if (leftBattleText != null) (-35).dp else (-20).dp,
        label = "leftBattleTextOffset"

    )

    if (leftBattleText != null) {
        BattleFeedbackText(
            battleText = leftBattleText,
            alignment = Alignment.CenterStart,
            offsetY = leftBattleTextOffset
        )
    }

    if (rightBattleText != null) {
        BattleFeedbackText(
            battleText = rightBattleText,
            alignment = Alignment.CenterEnd,
            offsetY = rightBattleTextOffset
        )
    }
}

@Composable
fun BoxScope.BattleHpHeader(
    playerName: String,
    playerHp: Int,
    playerMaxHp: Int,
    enemyName: String,
    enemyHp: Int,
    enemyMaxHp: Int
) {
    Column(
        modifier = Modifier.align(Alignment.TopStart), horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "$playerName HP: $playerHp/$playerMaxHp", fontSize = 14.sp
        )
        HpBar(
            currentHp = playerHp,
            maxHp = playerMaxHp,
            modifier = Modifier
                .width(120.dp)
                .height(8.dp)
        )
    }

    Column(
        modifier = Modifier.align(Alignment.TopEnd), horizontalAlignment = Alignment.End
    ) {

        Text(
            text = "$enemyName HP: $enemyHp/$enemyMaxHp", fontSize = 14.sp
        )
        HpBar(
            currentHp = enemyHp, maxHp = enemyMaxHp, modifier = Modifier
                .width(120.dp)
                .height(8.dp)
        )
    }
}

@Composable
fun BoxScope.BattleFighters(
    enemyName: String,
    playerAttacks: Boolean = false,
    enemyAttacks: Boolean = false,
    onEnemyClick: () -> Unit = {}
) {

    val playerOffset by animateDpAsState(
        targetValue = if (playerAttacks) 180.dp else 0.dp, label = "playerAttackOffset"
    )

    val enemyOffset by animateDpAsState(
        targetValue = if (enemyAttacks) -180.dp else 0.dp, label = "enemyAttackOffset"
    )

    Text(
        text = "🧙",
        modifier = Modifier
            .align(Alignment.CenterStart)
            .offset(x = playerOffset)
            .offset(y = (30).dp),
        fontSize = 60.sp
    )

    Text(
        text = enemyIcon(enemyName),
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .offset(x = enemyOffset)
            .offset(y = (33).dp)
            .clickable { onEnemyClick() },
        fontSize = 60.sp
    )
}

@Composable
fun BoxScope.BattleGround() {
    Box(
        modifier = Modifier
            .offset(y = (-30).dp)
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(6.dp)
            .background(Color(0xFF2E7D32))

    )
    Box(
        modifier = Modifier
            .offset(y = (-10).dp)
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(20.dp)
            .background(Color(0xFF5D4037))
    )
}

@Composable
fun BoxScope.BattleFeedbackText(
    battleText: String,
    alignment: Alignment,
    offsetY: Dp,
) {

    Text(
        text = battleText,
        modifier = Modifier
            .align(alignment)
            .offset(y = offsetY)
            .background(Color.Black.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        color = if (battleText.contains("KRIT") || battleText.contains("Tod")) Color.Red else Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

}

fun enemyIcon(enemyName: String): String {
    return when (enemyName) {
        "Goblin" -> "👾"
        "Wolf" -> "🐺"
        "Ork" -> "👹"
        "Stier" -> "🐂"
        else -> "👾"
    }
}


@Preview(
    name = "Game Screen Hoch", showBackground = true
)
@Composable
fun GameScreenHochPreview() {
    GameScreenHoch(
        viewModel = GameViewModel(),
        listState = rememberLazyListState(),
        onGameOver = {},
        onInventory = {},
        onShop = {})
}

@Preview(
    name = "Battle Scene", showBackground = true
)
@Composable
fun BattleScenePreview() {
    BattleScene(
        playerName = "Felix",
        playerHp = 80,
        playerMaxHp = 100,
        enemyName = "Wolf",
        enemyHp = 20,
        enemyMaxHp = 30
    )
}