package com.ouazzaniapps.felixquest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ouazzaniapps.felixquest.GameSave
import java.text.DateFormat
import java.util.Date

@Composable
fun SaveGameScreen(
    saveSlots: List<GameSave?>,
    onSaveGame: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF26C6DA))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            16.dp,
            Alignment.CenterVertically
        )
    ) {
        Text(
            text = "Spiel speichern",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 24.sp,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center
        )

        for (slot in 1..3) {
            val gameSave = saveSlots.getOrNull(slot - 1)

            if (gameSave == null) {
                Text("Speicherplatz $slot – Leer")
            } else {

                val savedAt = DateFormat.getDateTimeInstance(
                    DateFormat.SHORT,
                    DateFormat.SHORT
                ).format(Date(gameSave.savedAtMillis))

                Text(
                    "Speicherplatz $slot\n" +
                            "${gameSave.player.name} · Level ${gameSave.player.level}\n" +
                            "Siege: ${gameSave.player.battlesWon}\nGespeichert: $savedAt"
                )
            }
                GameButtonHoch(
                    text = if (gameSave == null) "Speichern" else "Überschreiben",
                    onClick = { onSaveGame(slot) }
                )


        }

        GameButtonHoch(
            text = "Zurück",
            onClick = onBack
        )
    }
}