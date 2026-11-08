package com.ouazzaniapps.felixquest.ui

fun isAttackOrFleeAllowed(
    attackInProgress: Boolean,
    playerAttacks: Boolean,
    enemyAttacks: Boolean,
    playerDead: Boolean,
    enemyHpZero: Boolean
): Boolean {
    return !(attackInProgress || playerAttacks || enemyAttacks || playerDead || enemyHpZero)
}

fun handleAttackClick(
    canAttackOrFlee: Boolean,
    tryAttackEnemy: () -> Boolean,
    onPlayerAttackStarted: () -> Unit,
) {
    if (!canAttackOrFlee) return

    if (tryAttackEnemy()) {
        onPlayerAttackStarted()
    }
}

fun handleFleeClick(
    canAttackOrFlee: Boolean,
    fleeButtonClick: () -> Unit,
) {
    if (!canAttackOrFlee) return
    fleeButtonClick()
}

fun handleSaveGameClick(
    attackInProgress: Boolean,
    playerDead: Boolean,
    onSaveGame: () -> Unit,
) {
    if (attackInProgress || playerDead) return
    onSaveGame()
}
