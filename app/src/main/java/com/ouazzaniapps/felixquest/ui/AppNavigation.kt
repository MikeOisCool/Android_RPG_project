package com.ouazzaniapps.felixquest.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ouazzaniapps.felixquest.GameEvent
import com.ouazzaniapps.felixquest.GameViewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import com.ouazzaniapps.felixquest.GameSave
import com.ouazzaniapps.felixquest.GameSaveStorage
import kotlinx.serialization.SerializationException
import java.io.IOException
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.AlertDialog

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = viewModel()
    val topLogState = rememberLazyListState()
    val listState = rememberLazyListState()
    val quests by viewModel.quests.collectAsState()
    val activity = LocalActivity.current
    var debugMode by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val saveStorage = remember(context) {
        GameSaveStorage(context)
    }

    NavHost(
        navController = navController,
        startDestination = "start_screen"
    ) {
        composable("start_screen") {
            val player by viewModel.player.collectAsState()
            StartScreen(
                startName = player.name,
                onNameEntered = { name ->
                    debugMode = name.trim() == "Felixdebug"
                    viewModel.startGame(name)
                    navController.navigate("game_screen")
                },
                onSavedGames = {
                    navController.navigate("saved_games_screen")
                },
                onExitApp = {
                    activity?.finish()
                }
            )
        }

        composable("saved_games_screen") {
            var saveSlots by remember {
                mutableStateOf<List<GameSave?>?>(null)
            }
            var loadError by remember {
                mutableStateOf<String?>(null)
            }

            LaunchedEffect(saveStorage) {
                try {
                    saveSlots = (1..3).map { slot ->
                        saveStorage.load(slot)
                    }
                } catch (e: IOException) {
                    loadError = "Speicherdateien konnten nicht gelesen werden."
                } catch (e: SerializationException) {
                    loadError = "Ein Spielstand enthält ungültige Daten."
                }
            }

            val loadedSlots = saveSlots

            if (loadedSlots != null) {
                SavedGamesScreen(
                    saveSlots = loadedSlots,
                    onLoadGame = { gameSave ->
                        debugMode = gameSave.player.name.trim() == "Felixdebug"
                        viewModel.restoreGame(gameSave)

                        navController.navigate("game_screen") {
                            popUpTo("saved_games_screen") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            } else {
                Column {
                    Text(loadError ?: "Spielstände werden geladen …")
                    TextButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text("Zurück")
                    }
                }
            }
        }

        composable("save_game_screen") {

            var selectedSlot by remember {
                mutableStateOf<Int?>(null)
            }
            var saveSlots by remember {
                mutableStateOf<List<GameSave?>?>(null)
            }
            var saveError by remember {
                mutableStateOf<String?>(null)
            }
            val saveScope = rememberCoroutineScope()

            var isSaving by remember {
                mutableStateOf(false)
            }

            val saveToSlot: (Int) -> Unit = { slot ->
                if (!isSaving) {
                    isSaving = true
                    saveError = null

                    saveScope.launch {
                        try {
                            val gameSave = viewModel.createGameSave()
                            saveStorage.save(slot, gameSave)
                            navController.popBackStack()
                        } catch (e: IOException) {
                            saveError = "Spielstand konnte nicht gespeichert werden."
                        } catch (e: SerializationException) {
                            saveError = "Spielstand konnte nicht umgewandelt werden."
                        } finally {
                            isSaving = false
                        }
                    }
                }
            }

            LaunchedEffect(saveStorage) {
                try {
                    saveSlots = (1..3).map { slot ->
                        saveStorage.load(slot)
                    }
                } catch (e: IOException) {
                    saveError = "Speicherdateien konnten nicht gelesen werden."
                } catch (e: SerializationException) {
                    saveError = "Ein Spielstand enthält ungültige Daten."
                }
            }
            val loadedSlots = saveSlots

            if (loadedSlots != null) {
                SaveGameScreen(
                    saveSlots = loadedSlots,
                    onSaveGame = { slot ->
                        if (!isSaving) {
                            if (loadedSlots[slot - 1] == null) {
                                saveToSlot(slot)
                            } else {
                                selectedSlot = slot
                            }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            } else {
                Column {
                    Text(saveError ?: "Speicherplätze werden geladen …")
                    TextButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text("Zurück")
                    }
                }
            }

            val slot = selectedSlot

            if (slot != null) {
                AlertDialog(
                    onDismissRequest = { selectedSlot = null },
                    title = { Text("Spielstand überschreiben?") },
                    text = {
                        Text("Der bisherige Spielstand auf Platz $slot wird ersetzt.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                selectedSlot = null
                                saveToSlot(slot)
                            }
                        ) {
                            Text("Überschreiben")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { selectedSlot = null }
                        ) {
                            Text("Abbrechen")
                        }
                    }
                )
            }
        }

        composable("game_screen") {
            GameScreen(
                debugMode = debugMode,
                viewModel = viewModel,
                listState = listState,
                topLogState = topLogState,
                onGameOver = {
                    navController.navigate("game_over_screen") {
                        popUpTo("game_screen") {
                            inclusive = true
                        }
                    }
                },
                onInventory = {
                    navController.navigate("inventory_screen")
                },
                onExitGame = {
                    navController.navigate("start_screen") {
                        popUpTo("game_screen") {
                            inclusive = true
                        }
                    }
                },
                onShop = {
                    navController.navigate("shop_screen")
                },
                onSaveGame = {
                    navController.navigate("save_game_screen")
                },
                onQuest = {
                    navController.navigate("quest_screen")
                }

            )
        }

        composable("inventory_screen") {
            InventoryScreen(
                viewModel = viewModel,
                onBackToGame = {
                    navController.popBackStack()
                }
            )
        }
        composable("shop_screen") {
            ShopScreen(
                viewModel = viewModel,
                onBackToGame = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = "quest_screen") {
            QuestScreen(
                quests = quests,
                onBackToGame = {
                    navController.popBackStack()
                },
                onStartQuest = { quest ->
                    viewModel.onEvent(GameEvent.StartQuest(quest))
                }
            )
        }

        composable("game_over_screen") {
            val player by viewModel.player.collectAsState()
            val log by viewModel.log.collectAsState()

            LaunchedEffect(Unit) {
                if (log.isNotEmpty()) {
                    listState.animateScrollToItem(log.size - 1)
                }
            }

            GameOverScreen(
                player = player,
                log = log,
                listState = listState,
                onInventory = {
                    navController.navigate("inventory_screen")
                },
                onRestart = {
                    viewModel.resetGame()
                    navController.navigate("start_screen") {
                        popUpTo("game_over_screen") { inclusive = true }

                    }
                },
                onExitApp = {
                    activity?.finish()
                }
            )
        }
    }
}
