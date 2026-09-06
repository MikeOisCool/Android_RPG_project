package com.mikeo.felixquest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikeo.felixquest.Quest

@Composable
fun QuestScreen(
    quests: List<Quest>,
    onBackToGame: () -> Unit,
    onStartQuest: (Quest) -> Unit
) {

    val scrollState = rememberScrollState()

    val availableQuests = quests.filter { it.isWählbar }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color(0xFF26C6DA))
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Quests for you",
            fontSize = 24.sp,
            textDecoration = TextDecoration.Underline
        )
        Spacer(modifier = Modifier.height(16.dp))

        InventorySection(
            title = "Quest",
            emptyText = "Es sind keine Quest verfügbar",
            isEmpty = availableQuests.isEmpty()
        ) {
            availableQuests.forEach { quest ->
                Text(text = "${quest.title}", fontSize = 18.sp)
                Text(text = quest.description)
                Text(text = "Fortschritt: ${quest.currentAmount}/${quest.targetAmount}")
                Text(text = "Belohnung: ${quest.goldReward} Gold und ${quest.xpReward} XP")
                Spacer(modifier = Modifier.height(8.dp))

                ShopButton(
                    text = if (quest.isStarted) "Quest ${quest.title} ist gestartet" else "${quest.title} starten",
                    onClick = { onStartQuest(quest) }
                )
            }


        }

        Spacer(modifier = Modifier.height(8.dp))

        GameButtonHoch(
            text = "Quests Schließen",
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(55.dp),
            onClick = onBackToGame
        )
    }
}