package com.mikeo.mykotlinplayground.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mikeo.mykotlinplayground.GameViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun GameScreen(
    viewModel: GameViewModel = viewModel(),
    listState: LazyListState,
    onGameOver: () -> Unit,
    onInventory: () -> Unit,
    onExitGame: () -> Unit
) {

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
    val configuration = LocalConfiguration.current

    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        GameScreenQuer(
            viewModel = viewModel,
            listState = listState,
            onGameOver = onGameOver,
            onInventory = onInventory
        )
    } else {
        GameScreenHoch(
            viewModel = viewModel,
            listState = listState,
            onGameOver = onGameOver,
            onInventory = onInventory

        )
    }
}
