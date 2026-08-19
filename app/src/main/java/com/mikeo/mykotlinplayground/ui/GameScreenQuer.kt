package com.mikeo.mykotlinplayground.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikeo.mykotlinplayground.Enemy
import com.mikeo.mykotlinplayground.GameEvent
import com.mikeo.mykotlinplayground.GameViewModel
import com.mikeo.mykotlinplayground.ItemNamen
import com.mikeo.mykotlinplayground.Player
import kotlinx.coroutines.delay


@Composable
fun GameScreenQuer(
    viewModel: GameViewModel,
    topLogState: LazyListState,
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

    LaunchedEffect(log.size) {
        if (log.isNotEmpty()) {
            topLogState.animateScrollToItem(log.size - 1)
            listState.animateScrollToItem(log.size - 1)
        }
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
        onEnemyAttackFinished = { enemyAttacks = false })

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {

        LandscapeMainPanel(
            player = player,
            enemy = enemy,
            log = log,
            topLogState = topLogState,
            canClickAttackButton = canClickAttackButton,
            onAttack = onAttack,
            onInventory = onInventory,
            onTakeDamage = { viewModel.onEvent(GameEvent.TakeDamage()) },
            onAddGold = { viewModel.onEvent(GameEvent.AddGold()) },
            onHeal = { viewModel.onEvent(GameEvent.Heal()) },
            onGainXp = { viewModel.onEvent(GameEvent.GainXp()) },
            onUsePotion = { viewModel.onEvent(GameEvent.UsePotion()) },
            onUseBigPotion = { viewModel.onEvent(GameEvent.UseBigPotion()) },
            onShop = onShop,
            onFlee = { viewModel.onEvent(GameEvent.Flee) })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clip(RoundedCornerShape(48.dp))
                .background(Color(0xFF4CAF50)),
            horizontalArrangement = Arrangement.End,

            ) {
            GameLog(
                log = log,
                listState = listState,
                modifier = Modifier
                    .weight(1f)
                    .height(400.dp)
                    .padding(start = 25.dp, top = 35.dp, end = 30.dp, bottom = 20.dp)
            )

            BattleScene(
                layoutScene = BattleSceneLayout(
                    hpHeaderBackgroundOffsetY = 24,
                    battleHpHeaderOffsetY = 14,
                    playerOnGroundOffsetY = 85,
                    playerAttackMoveX = 260,
                    enemyOnGroundOffsetY = 85,
                    enemyAttackMoveX = 260
                ),
                layoutSky = BattleSkyLayout(
                    sunOffsetX = -30,
                    sunOffsetY = 98,
                    cloudStartOffsetX = 40,
                    cloudStartOffsetY = 88,
                    cloudCenterOffsetY = 72,
                    cloudCenterOffsetX = 0,
                    sunSize = 50,
                    cloudStartSize = 40,
                    cloudCenterSize = 47
                ),
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
                modifier = Modifier
                    .weight(1f)
                    .height(400.dp)
                    .padding(20.dp)
            )
        }
    }
}

@Composable
fun LandscapeMainPanel(
    player: Player,
    enemy: Enemy,
    log: List<String>,
    topLogState: LazyListState,
    canClickAttackButton: Boolean,
    onTakeDamage: () -> Unit,
    onAddGold: () -> Unit,
    onHeal: () -> Unit,
    onGainXp: () -> Unit,
    onUsePotion: () -> Unit,
    onUseBigPotion: () -> Unit,
    onAttack: () -> Unit,
    onFlee: () -> Unit,
    onShop: () -> Unit,
    onInventory: () -> Unit,

    ) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color(0xFF4CAF50))
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                TopHpBarsQuer(
                    player = player, enemy = enemy
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 30.dp)
                ) {
                    val weaponBonus = player.equippedWeapon?.damage ?: 0
                    val armorDefense = player.equippedArmor?.defense ?: 0
                    val equippedWeapon = player.equippedWeapon?.name ?: "-"
                    val equippedArmor = player.equippedArmor?.name ?: "-"

                    PlayerStatsBlockQuer(
                        player = player,
                        weaponBonus = weaponBonus,
                        armorDefense = armorDefense,
                        equippedWeapon = equippedWeapon,
                        equippedArmor = equippedArmor
                    )

                    GameLog(
                        log = log,
                        listState = topLogState,
                        modifier = Modifier
                            .height(200.dp)
                            .padding(start = 0.dp, top = 5.dp, end = 0.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 30.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        EnemyStatsBlockQuer(enemy = enemy)
                    }

                    Column(modifier = Modifier.weight(3f)) {
                        val potionAmount =
                            player.inventory.items.find { it.name == ItemNamen.HEILTRANK }?.amount
                                ?: 0
                        val potionBigAmount =
                            player.inventory.items.find { it.name == ItemNamen.GROSSER_HEILTRANK }?.amount
                                ?: 0

                        GameActionButtonsQuer(
                            potionAmount = potionAmount,
                            potionBigAmount = potionBigAmount,
                            canClickAttackButton = canClickAttackButton,
                            onTakeDamage = onTakeDamage,
                            onAddGold = { onAddGold() },
                            onHeal = { onHeal() },
                            onGainXp = { onGainXp() },
                            onUsePotion = { onUsePotion() },
                            onUseBigPotion = { onUseBigPotion() },
                            onAttack = onAttack,
                            onFlee = onFlee,
                            onShop = onShop,
                            onInventory = {
                                onInventory()
                            })
                    }
                }

            }
        }
    }
}


@Composable
fun TopHpBarsQuer(
    player: Player, enemy: Enemy
) {

    Row(modifier = Modifier.fillMaxWidth(1f)) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.offset(x = (30).dp),
            ) {
                Text(text = player.name)
                Text("HP: ${player.hp}/${player.maxHp}")
            }
            HpBar(
                currentHp = player.hp,
                maxHp = player.maxHp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .padding(horizontal = 16.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "Gegner ${enemy.name} HP: ${enemy.hp}/${enemy.maxHp}",
                modifier = Modifier.padding(start = 30.dp)
            )
            HpBar(
                currentHp = enemy.hp,
                maxHp = enemy.maxHp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun EnemyStatsBlockQuer(
    enemy: Enemy
) {
    Text(
        text = enemy.name,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 35.dp)
    )
    StatQuer(label = "Level:", value = "${enemy.level}")
    StatQuer(label = "ATK:", value = "${enemy.attack}")
    StatQuer(label = "DEF:", value = "${enemy.defense}")
}

@Composable
fun PlayerStatsBlockQuer(
    player: Player,
    weaponBonus: Int,
    armorDefense: Int,
    equippedWeapon: String,
    equippedArmor: String
) {

    Column {

        Row {
            StatQuer(label = "Level:", value = "${player.level}")
            StatQuer(label = "Gold:", value = "${player.gold}")
        }

        Row {
            StatQuer(label = "XP:", value = "${player.xp}/${player.xpToNextLevel}")
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row {
            StatQuer(label = "ATK:", value = "${player.attack + weaponBonus}")
            StatQuer(label = "DEF:", value = "$armorDefense")
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row {
            StatQuer(
                label = "Waffe:", value = equippedWeapon, labelWidth = 70
            )
        }

        Row {
            StatQuer(
                label = "Rüstung:", value = equippedArmor, labelWidth = 70
            )
        }
    }
}

@Composable
fun GameActionButtonsQuer(
    potionAmount: Int,
    potionBigAmount: Int,
    canClickAttackButton: Boolean,
    onTakeDamage: () -> Unit,
    onAddGold: () -> Unit,
    onHeal: () -> Unit,
    onGainXp: () -> Unit,
    onUsePotion: () -> Unit,
    onUseBigPotion: () -> Unit,
    onAttack: () -> Unit,
    onFlee: () -> Unit,
    onShop: () -> Unit,
    onInventory: () -> Unit
) {

    DebugActionButtonsQuer(
        onTakeDamage = onTakeDamage, onAddGold = onAddGold, onHeal = onHeal, onGainXp = onGainXp
    )

    MainActionButtonsQuer(
        potionAmount = potionAmount,
        potionBigAmount = potionBigAmount,
        canClickAttackButton = canClickAttackButton,
        onUsePotion = onUsePotion,
        onUseBigPotion = onUseBigPotion,
        onAttack = onAttack,
        onFlee = onFlee,
        onShop = onShop,
        onInventory = onInventory
    )
}

@Composable
fun DebugActionButtonsQuer(
    onTakeDamage: () -> Unit, onAddGold: () -> Unit, onHeal: () -> Unit, onGainXp: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {

        GameButtonQuer(
            text = "Take Damage", onClick = onTakeDamage
        )

        Box(
            modifier = Modifier.padding(end = 25.dp)
        ) {
            GameButtonQuer(
                text = "Add Gold", onClick = onAddGold
            )
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GameButtonQuer(
            text = "Heilen", onClick = onHeal
        )
        Box(
            modifier = Modifier.padding(end = 25.dp)
        ) {
            GameButtonQuer(
                text = "XP sammeln", onClick = onGainXp
            )
        }
    }
}

@Composable
fun MainActionButtonsQuer(
    potionAmount: Int,
    potionBigAmount: Int,
    canClickAttackButton: Boolean,
    onUsePotion: () -> Unit,
    onUseBigPotion: () -> Unit,
    onAttack: () -> Unit,
    onShop: () -> Unit,
    onFlee: () -> Unit,
    onInventory: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        GameButtonQuer(
            text = "Heil (${potionAmount})", onClick = onUsePotion
        )
        Box(
            modifier = Modifier.padding(end = 25.dp)
        ) {
            GameButtonQuer(
                text = "Gr-Heil (${potionBigAmount})", onClick = onUseBigPotion
            )
        }
    }

    Box(
        modifier = Modifier.padding(end = 25.dp)
    ) {
        GameButtonQuer(
            text = "Angreifen",
            modifier = Modifier.fillMaxWidth(),
            containerColor = if (canClickAttackButton) Color.Red else Color(
                0xff9e9e9e
            ),
            onClick = {
                if (!canClickAttackButton) return@GameButtonQuer

                onAttack()
            })
    }
    Column(
        modifier = Modifier.fillMaxWidth(),

        ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.padding(end = 25.dp)
            ) {
                GameButtonQuer(
                    text = "Inventar", onClick = onInventory
                )
            }
            Box(
                modifier = Modifier.padding(end = 25.dp)
            ) {
                GameButtonQuer(
                    text = "Shop", onClick = onShop
                )
            }

        }
        GameButtonQuer(
            text = "Fliehen",
            onClick = onFlee,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 25.dp),
        )
    }
}

@Composable
fun StatQuer(label: String, value: String, labelWidth: Int = 60) {
    Row {
        Text(label, modifier = Modifier.width(labelWidth.dp))
        Text(value, modifier = Modifier.width(labelWidth.dp))
    }
}

@Preview(
    name = "Game Screen Quer", showBackground = true, widthDp = 800, heightDp = 400
)
@Composable
fun GameScreenQuerPreview() {

    val viewModel = GameViewModel()
    viewModel.fillPreviewLog()

    GameScreenQuer(
        viewModel = viewModel,
        topLogState = rememberLazyListState(),
        listState = rememberLazyListState(),
        onInventory = {},
        onShop = {},
        onGameOver = {})
}

@Preview(
    name = "Battle Scene", showBackground = true
)
@Composable
fun BattleSceneQuerPreview() {
    BattleScene(
        layoutScene = BattleSceneLayout(
            enemyOnGroundOffsetY = 17,
            playerOnGroundOffsetY = 17
        ),
        playerName = "Felix",
        playerHp = 80,
        playerMaxHp = 100,
        enemyName = "Wolf",
        enemyHp = 20,
        enemyMaxHp = 30
    )
}