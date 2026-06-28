package com.mikeo.mykotlinplayground.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mikeo.mykotlinplayground.GameViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: GameViewModel = viewModel()
    val listState = rememberLazyListState()

    NavHost(
        navController = navController,
        startDestination = "start_screen"
    ) {
        composable("start_screen") {
            val player by viewModel.player.collectAsState()
            StartScreen(
                startName = player.name,
                onNameEntered = { name ->
                    viewModel.startGame(name)
                    navController.navigate("game_screen")
                }
            )
        }

        composable("game_screen") {
            GameScreen(
                viewModel = viewModel,
                listState = listState,
                onGameOver = {
                    navController.navigate("game_over_screen")
                },
                onInventory = {
                    navController.navigate("inventory_screen")
                },
                onExitGame = {
                    navController.navigate("start_screen") {
                        popUpTo("start_screen") {
                            inclusive = true
                        }
                    }
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


        composable("game_over_screen") {
            val player by viewModel.player.collectAsState()
            val log by viewModel.log.collectAsState()

            LaunchedEffect(Unit) {
                if(log.isNotEmpty()) {
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
                        popUpTo("start_screen") { inclusive = true }

                    }
                }
            )
        }
    }
}