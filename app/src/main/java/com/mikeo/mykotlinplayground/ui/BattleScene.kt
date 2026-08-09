package com.mikeo.mykotlinplayground.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BattleScene(
    hpHeaderBackgroundOffsetY: Int = 10,
    battleHpHeaderOffsetY: Int = 0,
    playerName: String,
    playerOnGroundOffsetY: Int = 0,
    playerAttackMoveX: Int = 0,
    playerHp: Int,
    playerMaxHp: Int,
    enemyName: String,
    enemyOnGroundOffsetY: Int = 0,
    enemyAttackMoveX: Int = 0,
    enemyHp: Int,
    enemyMaxHp: Int,
    rightBattleText: String? = null,
    leftBattleText: String? = null,
    playerAttacks: Boolean = false,
    enemyAttacks: Boolean = false,
    onEnemyClick: () -> Unit = {},
    modifier: Modifier = Modifier
        .width(400.dp)
        .height(230.dp)
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFF8BC34A))

    ) {
        Box(
            modifier = Modifier
                .offset(y = (hpHeaderBackgroundOffsetY).dp)
                .fillMaxWidth(0.95f)
                .align(Alignment.TopCenter)
                .height(40.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFFD54F))
        )
        Box(
            modifier = Modifier.padding(16.dp)
        ) {

            BattleHpHeader(
                battleHpHeaderOffsetY = battleHpHeaderOffsetY,
                playerName = playerName,
                playerHp = playerHp,
                playerMaxHp = playerMaxHp,
                enemyName = enemyName,
                enemyHp = enemyHp,
                enemyMaxHp = enemyMaxHp
            )

            BattleFeedbackTexts(
                rightBattleText = rightBattleText, leftBattleText = leftBattleText
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                BattleGround()

                BattleFighters(
                    enemyName = enemyName,
                    playerAttacks = playerAttacks,
                    enemyAttacks = enemyAttacks,
                    playerOnGroundOffsetY = playerOnGroundOffsetY,
                    playerAttackMoveX = playerAttackMoveX,
                    enemyAttackMoveX = enemyAttackMoveX,
                    enemyOnGroundOffsetY = enemyOnGroundOffsetY,
                    onEnemyClick = onEnemyClick
                )
            }
        }
    }
}

@Composable
fun BoxScope.BattleFeedbackTexts(
    rightBattleText: String?, leftBattleText: String?
) {
    val rightBattleTextOffset by animateDpAsState(
        targetValue = if (rightBattleText != null) (-35).dp else (-20).dp,
        label = "rightBattleTextOffset"

    )

    val leftBattleTextOffset by animateDpAsState(
        targetValue = if (leftBattleText != null) (-35).dp else (-20).dp,
        label = "leftBattleTextOffset"

    )

    if (leftBattleText != null) {
        BattleFeedbackText(
            battleText = leftBattleText,
            alignment = Alignment.CenterStart,
            offsetY = leftBattleTextOffset
        )
    }

    if (rightBattleText != null) {
        BattleFeedbackText(
            battleText = rightBattleText,
            alignment = Alignment.CenterEnd,
            offsetY = rightBattleTextOffset
        )
    }
}

@Composable
fun BoxScope.BattleHpHeader(
    battleHpHeaderOffsetY: Int,
    playerName: String,
    playerHp: Int,
    playerMaxHp: Int,
    enemyName: String,
    enemyHp: Int,
    enemyMaxHp: Int
) {
    Column(
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(y = (battleHpHeaderOffsetY).dp), horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = "$playerName HP: $playerHp/$playerMaxHp", fontSize = 14.sp
        )
        HpBar(
            currentHp = playerHp,
            maxHp = playerMaxHp,
            modifier = Modifier
                .width(120.dp)
                .height(8.dp)
        )
    }

    Column(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .offset(y = (battleHpHeaderOffsetY).dp), horizontalAlignment = Alignment.End
    ) {

        Text(
            text = "$enemyName HP: $enemyHp/$enemyMaxHp", fontSize = 14.sp
        )
        HpBar(
            currentHp = enemyHp, maxHp = enemyMaxHp, modifier = Modifier
                .width(120.dp)
                .height(8.dp)
        )
    }
}

@Composable
fun BoxScope.BattleFighters(
    enemyName: String,
    playerAttacks: Boolean = false,
    enemyAttacks: Boolean = false,
    playerOnGroundOffsetY: Int,
    playerAttackMoveX: Int,
    enemyOnGroundOffsetY: Int,
    enemyAttackMoveX: Int,
    onEnemyClick: () -> Unit = {}
) {

    val playerOffsetX by animateDpAsState(
        targetValue = if (playerAttacks) playerAttackMoveX.dp else 0.dp, label = "playerAttackOffset"
    )

    val enemyOffsetX by animateDpAsState(
        targetValue = if (enemyAttacks) -enemyAttackMoveX.dp else 0.dp, label = "enemyAttackOffset"
    )

    Text(
        text = "🧙",
        modifier = Modifier
            .align(Alignment.CenterStart)
            .offset(x = playerOffsetX)
            .offset(y = playerOnGroundOffsetY.dp),
        fontSize = 60.sp
    )

    Text(
        text = enemyIcon(enemyName),
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .offset(x = enemyOffsetX)
            .offset(y = enemyOnGroundOffsetY.dp)
            .clickable { onEnemyClick() },
        fontSize = 60.sp
    )
}

@Composable
fun BoxScope.BattleGround() {
    Box(
        modifier = Modifier
            .offset(y = (-30).dp)
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(6.dp)
            .background(Color(0xFF2E7D32))

    )
    Box(
        modifier = Modifier
            .offset(y = (-10).dp)
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .height(20.dp)
            .background(Color(0xFF5D4037))
    )
}

@Composable
fun BoxScope.BattleFeedbackText(
    battleText: String,
    alignment: Alignment,
    offsetY: Dp,
) {

    Text(
        text = battleText,
        modifier = Modifier
            .align(alignment)
            .offset(y = offsetY)
            .background(Color.Black.copy(alpha = 0.45f), RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        color = if (battleText.contains("KRIT") || battleText.contains("Tod")) Color.Red else Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

}

fun enemyIcon(enemyName: String): String {
    return when (enemyName) {
        "Goblin" -> "👾"
        "Wolf" -> "🐺"
        "Ork" -> "👹"
        "Stier" -> "🐂"
        else -> "👾"
    }
}
