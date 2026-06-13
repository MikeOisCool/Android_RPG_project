package com.mikeo.mykotlinplayground.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikeo.mykotlinplayground.GameViewModel

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
            fontSize = 24.sp
        )

        items.forEach { item ->
            Text(text = "${item.name} x${item.amount}")
        }

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
