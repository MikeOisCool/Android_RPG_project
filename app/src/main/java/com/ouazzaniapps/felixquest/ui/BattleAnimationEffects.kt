package com.ouazzaniapps.felixquest.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun BattleAnimationEffects(
    playerAttacks: Boolean,
    enemyAttacks: Boolean,
    enemyHp: Int = 0,
    onPlayerAttackFinished: () -> Unit,
    onEnemyAttackStarted: () -> Unit,
    onEnemyAttackFinished: () -> Unit
) {
    LaunchedEffect(playerAttacks) {
        if (playerAttacks) {
            delay(200)
            onPlayerAttackFinished()
            if (enemyHp > 0) {
                onEnemyAttackStarted()
            }
        }
    }

    LaunchedEffect(enemyAttacks) {
        if (enemyAttacks) {
            delay(200)
            onEnemyAttackFinished()
        }
    }
}
