package com.ouazzaniapps.felixquest.ui

import android.content.res.Configuration
import android.speech.tts.TextToSpeech
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ouazzaniapps.felixquest.GameViewModel
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.ouazzaniapps.felixquest.calculateDayPhase
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel(),
    debugMode: Boolean,
    listState: LazyListState,
    topLogState: LazyListState,
    onGameOver: () -> Unit,
    onInventory: () -> Unit,
    onShop: () -> Unit,
    onQuest: () -> Unit,
    onSaveGame: () -> Unit,
    onExitGame: () -> Unit
) {
    val enemy by viewModel.enemy.collectAsState()
    val player by viewModel.player.collectAsState()
    val elapsedSeconds by viewModel.elapsedSeconds.collectAsState()
    val dayPhase = calculateDayPhase(elapsedSeconds)
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasShownBullWarning by rememberSaveable { mutableStateOf(false) }
    var showBullWarning by rememberSaveable { mutableStateOf(false) }

    var hasShownHowToPlay by rememberSaveable { mutableStateOf(false) }
    var showHowToPlay by rememberSaveable { mutableStateOf(false) }

    var lastLevel by rememberSaveable { mutableStateOf(player.level) }

    val context = LocalContext.current
    var textToSpeech by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTextToSpeechReady by remember { mutableStateOf(false) }



    var showExitDialog by remember {
        mutableStateOf(false)
    }

    BackHandler {
        showExitDialog = true
    }
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = {
                showExitDialog = false
            },
            title = {
                Text("Spiel beenden")
            },
            text = {
                Text("Möchtest du das Spiel wirklich beenden?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetGame()
                        onExitGame()
                    }
                ) {
                    Text("Ja")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                    }
                ) {
                    Text("Nein")
                }
            }
        )
    }

    LaunchedEffect(lifecycleOwner, player.isDead) {
        if (!player.isDead) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                while (true) {
                    delay(1000)
                    viewModel.advanceGameTime()
                }
            }
        }
    }

    LaunchedEffect(player.level, isTextToSpeechReady) {
        if (player.level > lastLevel) {
            if (!isTextToSpeechReady) return@LaunchedEffect
            textToSpeech?.speak(
                "Level up",
                TextToSpeech.QUEUE_FLUSH,
                null,
                "level_up"
            )
        }
        lastLevel = player.level
    }

    DisposableEffect(Unit) {

        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = java.util.Locale.GERMAN
                isTextToSpeechReady = true
            }
        }

        onDispose {
            textToSpeech?.shutdown()
        }
    }

    val configuration = LocalConfiguration.current

    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(enemy.name) {
        if (enemy.name == "Stier" && !hasShownBullWarning) {
            showBullWarning = true
            hasShownBullWarning = true
        }
    }

    LaunchedEffect(Unit) {
        if (player.level == 1 && !hasShownHowToPlay) {
            hasShownHowToPlay = true
            showHowToPlay = true
        }
    }

    if (isLandscape) {
        GameScreenQuer(
            debugMode = debugMode,
            viewModel = viewModel,
            topLogState = topLogState,
            listState = listState,
            dayPhase = dayPhase,
            onGameOver = onGameOver,
            onInventory = onInventory,
            onShop = onShop,
            onSaveGame = onSaveGame,
            onQuest = onQuest
        )
    } else {
        GameScreenHoch(
            debugMode = debugMode,
            viewModel = viewModel,
            listState = listState,
            dayPhase = dayPhase,
            onGameOver = onGameOver,
            onInventory = onInventory,
            onShop = onShop,
            onSaveGame = onSaveGame,
            onQuest = onQuest
        )
    }
    if (showHowToPlay) {
        AlertDialog(
            onDismissRequest = { showHowToPlay = false },
            title = { Text("Hallo, ${player.name}!") },
            text = {
                Text("Tippe auf den Gegner, um anzugreifen.\n\n" +
                        "Sonne oder Mond: Fliehen\n" +
                        "Linke Wolke oder linker Stern: Quests öffnen\n" +
                        "Rechte Wolke oder rechter Stern: Inventar öffnen\n\n" +
                        "Tag und Nacht wechseln während des aktiven Spielens.")
            },
            confirmButton = {
                TextButton(onClick = { showHowToPlay = false }) {
                    Text("Verstanden")
                }
            }
        )
    }

    if (showBullWarning) {
        AlertDialog(
            onDismissRequest = { showBullWarning = false },
            title = { Text("Achtung, ${player.name}!") },
            text = {
                Text("Der Stier ist sehr stark. Ohne gute Ausrüstung ist Fliehen oft die beste Wahl. Du kannst dafür auf die Sonne oder den Mond drücken.")
            },
            confirmButton = {
                TextButton(onClick = { showBullWarning = false }) {
                    Text("Verstanden")
                }
            }
        )
    }
}
