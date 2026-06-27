package com.mikeo.mykotlinplayground.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikeo.mykotlinplayground.GameEvent
import com.mikeo.mykotlinplayground.GameViewModel
import com.mikeo.mykotlinplayground.ItemNamen

@Composable
fun InventoryScreen(
    viewModel: GameViewModel,
    onBackToGame: () -> Unit
) {

    val player by viewModel.player.collectAsState()
    val items = player.inventory.items

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color.Cyan),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            text = "Inventar",
            fontSize = 24.sp,
            textDecoration = TextDecoration.Underline
        )
        Spacer( modifier = Modifier.height(16.dp))

        val potions = items.filter {
            it.name == ItemNamen.HEILTRANK ||
                    it.name == ItemNamen.GROSSER_HEILTRANK
        }

        val weapons = items.filter {
            it.name == ItemNamen.HOLZSCHWERT
        }

        Text("Tränke", fontSize = 20.sp)

        if (potions.isEmpty()) {
            Text (
                text = "Es sind keine Tränke im Inventar",
                fontSize = 12.sp
            )
        } else {

            potions.forEach { item ->
                Text(text = "${item.name} x${item.amount}")
            }
        }

        Spacer( modifier = Modifier.height(8.dp) )

        Text("Waffen", fontSize = 20.sp)

        if (weapons.isEmpty()) {
            Text (
                text = "Es sind keine Waffen im Inventar",
                fontSize = 12.sp
            )
        } else {

            weapons.forEach { item ->
                Text(text = "${item.name} x${item.amount}")

                GameButtonHoch(
                    text = if (player.equippedWeapon == item.name) "Ausgerüstet" else "Ausrüsten",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        viewModel.onEvent(GameEvent.EquipWeapon(item.name))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))



        GameButtonHoch(
            text = "Inventar Schließen",
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            onClick = { onBackToGame() }
        )
    }
}
