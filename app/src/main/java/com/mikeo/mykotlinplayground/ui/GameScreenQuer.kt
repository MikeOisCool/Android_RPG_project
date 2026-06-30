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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mikeo.mykotlinplayground.GameEvent
import com.mikeo.mykotlinplayground.GameViewModel
import com.mikeo.mykotlinplayground.ItemNamen


@Composable
fun GameScreenQuer(
    viewModel: GameViewModel,
    listState: LazyListState,
    onGameOver: () -> Unit,
    onInventory: () -> Unit
) {
    val player by viewModel.player.collectAsState()


    val log by viewModel.log.collectAsState()

    val enemy by viewModel.enemy.collectAsState()

    LaunchedEffect(log.size) {
        if (log.isNotEmpty())
            listState.animateScrollToItem(log.size - 1)
    }

    if (player.isDead) {
        onGameOver()
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color(0xFF4CAF50))
            .padding(12.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row {
                        Text(
                            text = player.name,
                            modifier = Modifier
                                .padding(start = 30.dp)
                                .width(100.dp)
                        )

                        Text("HP: ${player.hp}/${player.maxHp}")
                    }
                    HpBar(
                        currentHp = player.hp,
                        maxHp = player.maxHp
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Gegner HP: ${enemy.hp}/${enemy.maxHp}",
                        modifier = Modifier.padding(start = 30.dp)
                    )
                    HpBar(
                        currentHp = enemy.hp,
                        maxHp = enemy.maxHp
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Column(
                    modifier = Modifier
                        .weight(5f)
                        .padding(start = 30.dp)
                ) {
                    val weaponBonus = player.equippedWeapon?.damage ?: 0
                    val armorDefense = player.equippedArmor?.defense ?: 0

                    val labelWidth = 65.dp
                    val valueWidth = 65.dp

                    Column {

                        Row {
                            Text("Level:", modifier = Modifier.width(labelWidth))
                            Text("${player.level}", modifier = Modifier.width(valueWidth))

                            Text("Gold:", modifier = Modifier.width(labelWidth))
                            Text("${player.gold}")
                        }

                        Row {
                            Text("XP:", modifier = Modifier.width(labelWidth))
                            Text("${player.xp}/${player.xpToNextLevel}")
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row {
                            Text("ATK:", modifier = Modifier.width(labelWidth))
                            Text("${player.attack + weaponBonus}", modifier = Modifier.width(65.dp))

                            Text("DEF:", modifier = Modifier.width(60.dp))
                            Text("$armorDefense")
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row {
                            Text("Waffe:", modifier = Modifier.width(70.dp))
                            Text(player.equippedWeapon?.name ?: "-")
                        }

                        Row {
                            Text("Rüstung:", modifier = Modifier.width(70.dp))
                            Text(player.equippedArmor?.name ?: "-")
                        }
                    }

                }

                Column(
                    modifier = Modifier
                        .weight(5f)
                        .padding(start = 180.dp)
                ) {


                    Text("Gegner: ${enemy.name}")
                    Text("Level: ${enemy.level}")
                    Text("HP: ${enemy.hp}")
                    Text("ATK: ${enemy.attack}")
                    Text("DEF: ${enemy.defense}")


                }



                Column(
                    modifier = Modifier
                        .weight(5f)
                        .offset(x = (0).dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        GameButtonQuer(
                            text = "Take Damage",
                            onClick = { viewModel.onEvent(GameEvent.TakeDamage()) }
                        )

                        Box(
                            modifier = Modifier.padding(end = 25.dp)
                        ) {
                            GameButtonQuer(
                                text = "Add Gold",
                                onClick = { viewModel.onEvent(GameEvent.AddGold()) }
                            )
                        }

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GameButtonQuer(
                            text = "Heilen",
                            onClick = { viewModel.onEvent(GameEvent.Heal()) }
                        )
                        Box(
                            modifier = Modifier.padding(end = 25.dp)
                        ) {
                            GameButtonQuer(
                                text = "XP sammeln",
                                onClick = { viewModel.onEvent(GameEvent.GainXp()) }
                            )
                        }
                    }

                    val potionAmount =
                        player.inventory.items.find { it.name == ItemNamen.HEILTRANK }
                    val potionBigAmount =
                        player.inventory.items.find { it.name == ItemNamen.GROSSER_HEILTRANK }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GameButtonQuer(
                            text = "Heil (${potionAmount?.amount ?: 0})",
                            onClick = {
                                viewModel.onEvent(GameEvent.UsePotion())
                            }
                        )
                        Box(
                            modifier = Modifier.padding(end = 25.dp)
                        ) {
                            GameButtonQuer(
                                text = "Gr-Heil (${potionBigAmount?.amount ?: 0})",
                                onClick = {
                                    viewModel.onEvent(GameEvent.UseBigPotion())
                                }
                            )
                        }

                    }

                    Box(
                        modifier = Modifier.padding(end = 25.dp)
                    ) {
                        GameButtonQuer(
                            text = "Angreifen",
                            modifier = Modifier
                                .fillMaxWidth(),
                            containerColor = Color.Red,
                            onClick = { viewModel.onEvent(GameEvent.AttackEnemy) }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GameButtonQuer(
                            text = "Fliehen",

                            onClick = { viewModel.onEvent(GameEvent.Flee) }
                        )
                        Box(
                            modifier = Modifier.padding(end = 25.dp)
                        ) {
                            GameButtonQuer(
                                text = "Inventar",

                                onClick = {
                                    onInventory()
                                }
                            )
                        }
                    }

                }


            }

        }
        GameLog(
            log = log,
            listState = listState,
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomStart)
                .fillMaxWidth(0.65f)
                .height(210.dp)
                .padding(start = 25.dp, end = 30.dp, bottom = 20.dp)
        )
    }
}

@Preview(
    name = "Game Screen Quer",
    showBackground = true,
    widthDp = 800,
    heightDp = 400
)
@Composable
fun GameScreenQuerPreview() {

    val viewModel = GameViewModel()
    viewModel.fillPreviewLog()

    GameScreenQuer(
        viewModel = viewModel,
        listState = rememberLazyListState(),
        onGameOver = {},
        onInventory = {}
    )
}

