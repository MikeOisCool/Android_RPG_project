package com.ouazzaniapps.felixquest.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun GameOverEffect(
    isPlayerDead: Boolean,
    onGameOver: () -> Unit
) {
    LaunchedEffect(isPlayerDead) {
        if (isPlayerDead) {
            delay(1200)
            onGameOver()
        }
    }
}

@Composable
fun LogAutoScrollEffect(
    log: List<String>,
    topLogState: LazyListState,
    secondLogState: LazyListState? = null
) {
    LaunchedEffect(log) {

        if (log.isNotEmpty()) {
            val lastIndex = log.lastIndex
            topLogState.animateScrollToItem(lastIndex)
            secondLogState?.animateScrollToItem(lastIndex)
        }
    }
}