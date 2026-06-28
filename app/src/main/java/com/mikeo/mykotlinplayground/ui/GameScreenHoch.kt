package com.mikeo.mykotlinplayground.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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


@Composable
fun GameScreenHoch(
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color(0xFF4CAF50)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GameLog(log = log, listState = listState, modifier = Modifier.height(120.dp))





            Text(
                text = "Name: ${player.name} Level: ${player.level}",
                fontSize = 24.sp
            )
            Text(text = "HP: ${player.hp}/${player.maxHp} Gold: ${player.gold}", fontSize = 20.sp)


            HpBar(
                currentHp = player.hp,
                maxHp = player.maxHp
            )

            Text(
                text = "XP: ${player.xp}/${player.xpToNextLevel}",
                fontSize = 18.sp
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

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row {
                GameButtonHoch(
                    text = "Take Damage",
                    onClick = { viewModel.onEvent(GameEvent.TakeDamage()) }
                )

                GameButtonHoch(
                    text = "Add Gold",
                    onClick = { viewModel.onEvent(GameEvent.AddGold()) }
                )
            }
            Row {
                GameButtonHoch(
                    text = "Heilen",
                    onClick = { viewModel.onEvent(GameEvent.Heal()) }
                )
                val potionBigAmount =
                    player.inventory.items.find { it.name == ItemNamen.GROSSER_HEILTRANK }?.amount
                        ?: 0
                GameButtonHoch(
                    text = "Big Heal (${potionBigAmount})",
                    onClick = { viewModel.onEvent(GameEvent.UseBigPotion()) }
                )
            }


            GameButtonHoch(
                text = "XP sammeln",
                onClick = { viewModel.onEvent(GameEvent.GainXp()) }
            )


            Row(
                modifier = Modifier.padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                val potionAmount =
                    player.inventory.items.find { it.name == ItemNamen.HEILTRANK }?.amount ?: 0
                GameButtonHoch(
                    text = "Heiltrank (${potionAmount})",
                    onClick = {
                        viewModel.onEvent(GameEvent.UsePotion())
                    }
                )

                GameButtonHoch(
                    text = "Inventar öffnen",
                    onClick = {
                        onInventory()
                    }
                )
            }
            GameButtonHoch(
                text = "Angreifen",
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(70.dp),
                containerColor = Color.Red,
                onClick = { viewModel.onEvent(GameEvent.AttackEnemy) }
            )
            Text("Gegner: ${enemy.name}")
            Text("Level: ${enemy.level}")
            Text("HP: ${enemy.hp}")

            HpBar(
                currentHp = enemy.hp,
                maxHp = enemy.maxHp
            )
            GameButtonHoch(
                text = "Fliehen",
                onClick = { viewModel.onEvent(GameEvent.Flee) }
            )
        }
    }
}

@Preview(
    name = "Game Screen Hoch",
    showBackground = true
)
@Composable
fun GameScreenHochPreview() {
    GameScreenHoch(
        viewModel = GameViewModel(),
        listState = rememberLazyListState(),
        onGameOver = {},
        onInventory = {}
    )
}