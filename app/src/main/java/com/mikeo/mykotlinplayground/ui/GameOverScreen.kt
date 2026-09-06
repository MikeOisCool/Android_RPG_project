package com.mikeo.mykotlinplayground.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikeo.mykotlinplayground.Inventory
import com.mikeo.mykotlinplayground.Player


@Composable
fun GameOverScreen(
    player: Player,
    log: List<String>,
    listState: LazyListState,
    onRestart: () -> Unit,
    onInventory: () -> Unit,
    onExitApp: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val titleSize = if (isLandscape) 28.sp else 36.sp
    val textSize = if (isLandscape) 18.sp else 24.sp
    val buttonHeight = if (isLandscape) 48.dp else 60.dp
    val logHeight = if (isLandscape) 140.dp else 300.dp
    val bigSpacer = if (isLandscape) 8.dp else 24.dp

    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 12.dp,
                end = 12.dp,
                top = if (isLandscape) 6.dp else 12.dp,
                bottom = 12.dp
            )
            .clip(RoundedCornerShape(40.dp))
            .background(Color(0xFF8B0000))
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "☠ GAME OVER ☠",
            fontSize = titleSize,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(bigSpacer))

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

        Spacer(modifier = Modifier.height(bigSpacer))

        GameButtonHoch(
            text = "Inventar öffnen",
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight),

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
                .height(buttonHeight),

            containerColor = Color.Black,
            onClick = onRestart
        )
        GameButtonHoch(
            text = "Spiel verlassen",
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .height(buttonHeight),
            containerColor = Color.Black,
            onClick = onExitApp
        )
        GameLog(
            log = log,
            listState = listState,
            textColor = Color.White,
            modifier = Modifier.height(logHeight)
        )
    }
}


@Preview(
    name = "Game Over Screen",
    showBackground = true,
    widthDp = 400,
    heightDp = 800
)
@Composable
fun GameOverScreenPreview() {
    GameOverScreen(
        player = Player(
            name = "Felix",
            hp = 0,
            maxHp = 100,
            attack = 10,
            inventory = Inventory(emptyList()),
            gold = 120,
            isDead = true,
            level = 3
        ),
        log = listOf(
            "👹 Ork schlägt zurück für 18 Schaden!",
            "💀 Felix hat 0 HP übrig",
            "💀 Felix ist gestorben",
            "Felix hat das Level 3 erreicht und hat 40 XP! Sein Gold: 120"
        ),
        listState = rememberLazyListState(),
        onRestart = {},
        onInventory = {},
        onExitApp = {}
    )
}