package com.mikeo.mykotlinplayground.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikeo.mykotlinplayground.Player


@Composable
fun GameOverScreen(
    player: Player,
    log: List<String>,
    listState: LazyListState,
    onRestart: () -> Unit,
    onInventory: () -> Unit
) {
    val textSize = 24.sp
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color(0xFF8B0000)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "☠ GAME OVER ☠",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "${player.name} ist gefallen!",
            fontSize = 22.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Level: ${player.level}", fontSize = textSize, color = Color(0xFFFFD700))
        Text(
            text = "XP: ${player.xp}/${player.xpToNextLevel}",
            fontSize = textSize,
            color = Color(0xFFFFD700)
        )

        Text(
            text = "Verbleibendes Gold: ${player.gold}",
            fontSize = 22.sp,
            color = Color(0xFFFFD700)

        )

        Spacer(modifier = Modifier.height(24.dp))

        GameButtonHoch(
            text = "Inventar öffnen",
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),

            containerColor = Color.Black,
            onClick = {
                onInventory()
            }
        )

        GameButtonHoch(
            text = "Restart",
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),

            containerColor = Color.Black,
            onClick = onRestart
        )
        GameLog(log = log, listState = listState, textColor = Color.White)
    }
}