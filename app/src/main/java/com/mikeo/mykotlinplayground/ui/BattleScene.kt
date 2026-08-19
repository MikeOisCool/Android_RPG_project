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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class BattleSceneLayout(
    val hpHeaderBackgroundOffsetY: Int = 10,
    val battleHpHeaderOffsetY: Int = 0,
    val playerOnGroundOffsetY: Int = 0,
    val playerAttackMoveX: Int = 0,
    val enemyOnGroundOffsetY: Int = 0,
    val enemyAttackMoveX: Int = 0
)

data class BattleSkyLayout(
    val sunOffsetX: Int = -18,
    val sunOffsetY: Int = 38,
    val cloudStartOffsetX: Int = 40,
    val cloudStartOffsetY: Int = 58,
    val cloudCenterOffsetY: Int = 72,
    val cloudCenterOffsetX: Int = 0,
    val sunSize: Int = 20,
    val cloudStartSize: Int = 20,
    val cloudCenterSize: Int = 27
)

@Composable
fun BattleScene(
    layoutScene: BattleSceneLayout = BattleSceneLayout(),
    layoutSky: BattleSkyLayout = BattleSkyLayout(),
    playerName: String,
    playerHp: Int,
    playerMaxHp: Int,
    enemyName: String,
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
            .background(Color(0xFF81D4FA))

    ) {
        Box(
            modifier = Modifier
                .offset(y = (layoutScene.hpHeaderBackgroundOffsetY).dp)
                .fillMaxWidth(0.95f)
                .align(Alignment.TopCenter)
                .height(40.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFFFD54F))
        )
        Box(
            modifier = Modifier.padding(16.dp)
        ) {

            BattleSkyDecorations(
                sunOffsetX = layoutSky.sunOffsetX,
                sunOffsetY = layoutSky.sunOffsetY,
                cloudStartOffsetX = layoutSky.cloudStartOffsetX,
                cloudStartOffsetY = layoutSky.cloudStartOffsetY,
                cloudCenterOffsetY = layoutSky.cloudCenterOffsetY,
                cloudCenterOffsetX = layoutSky.cloudCenterOffsetX,
                sunSize = layoutSky.sunSize,
                cloudStartSize = layoutSky.cloudStartSize,
                cloudCenterSize = layoutSky.cloudCenterSize
            )

            BattleHpHeader(
                battleHpHeaderOffsetY = layoutScene.battleHpHeaderOffsetY,
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
                    playerOnGroundOffsetY = layoutScene.playerOnGroundOffsetY,
                    playerAttackMoveX = layoutScene.playerAttackMoveX,
                    enemyAttackMoveX = layoutScene.enemyAttackMoveX,
                    enemyOnGroundOffsetY = layoutScene.enemyOnGroundOffsetY,
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
fun BoxScope.BattleSkyDecorations(
    sunOffsetX: Int,
    sunOffsetY: Int,
    cloudStartOffsetX: Int,
    cloudStartOffsetY: Int,
    cloudCenterOffsetY: Int,
    cloudCenterOffsetX: Int,
    sunSize: Int,
    cloudStartSize: Int,
    cloudCenterSize: Int
) {
    // Sonne
    Text(
        text = "☀️",
        modifier = Modifier
            .align(Alignment.TopEnd)
            .offset(x = sunOffsetX.dp, y = sunOffsetY.dp),
        fontSize = sunSize.sp
    )

    // Wolke links
    Text(
        text = "☁️",
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = cloudStartOffsetX.dp, y = cloudStartOffsetY.dp),
        fontSize = cloudStartSize.sp
    )

    // Wolke mittig
    Text(
        text = "☁️",
        modifier = Modifier
            .align(Alignment.TopCenter)
            .offset(x = cloudCenterOffsetX.dp, y = cloudCenterOffsetY.dp),
        fontSize = cloudCenterSize.sp
    )
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
            .offset(y = (battleHpHeaderOffsetY).dp),
        horizontalAlignment = Alignment.Start
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
            .offset(y = (battleHpHeaderOffsetY).dp),
        horizontalAlignment = Alignment.End
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
        targetValue = if (playerAttacks) playerAttackMoveX.dp else 0.dp,
        label = "playerAttackOffset"
    )

    val enemyOffsetX by animateDpAsState(
        targetValue = if (enemyAttacks) -enemyAttackMoveX.dp else 0.dp, label = "enemyAttackOffset"
    )
    Box(
        modifier = Modifier
            .align(Alignment.CenterStart)
            .offset(x = playerOffsetX)
            .offset(y = (playerOnGroundOffsetY + 35).dp)
            .width(55.dp)
            .height(10.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(Color.Black.copy(alpha = 0.25f))
    )
    Text(
        text = "🧙",
        modifier = Modifier
            .align(Alignment.CenterStart)
            .offset(x = playerOffsetX)
            .offset(y = playerOnGroundOffsetY.dp),
        fontSize = 60.sp
    )

    Box(
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .offset(x = enemyOffsetX)
            .offset(y = (enemyOnGroundOffsetY + 35).dp)
            .width(55.dp)
            .height(10.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(Color.Black.copy(alpha = 0.25f))
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
    //Gras
    Box(
        modifier = Modifier
            .offset(y = (-32).dp)
            .align(Alignment.BottomCenter)
            .fillMaxWidth(0.92f)
            .height(10.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF43A047))

    )

    // Erde
    Box(
        modifier = Modifier
            .offset(y = (-10).dp)
            .align(Alignment.BottomCenter)
            .fillMaxWidth(0.92f)
            .height(28.dp)
            .clip(RoundedCornerShape(6.dp))
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

@Preview(
    name = "Battle Scene", showBackground = true
)
@Composable
fun BattleScenePreview1() {
    BattleScene(
        layoutScene = BattleSceneLayout(
            enemyOnGroundOffsetY = 17,
            playerOnGroundOffsetY = 17
        ),
        layoutSky = BattleSkyLayout(),
        playerName = "Felix",
        playerHp = 80,
        playerMaxHp = 100,
        enemyName = "Wolf",
        enemyHp = 20,
        enemyMaxHp = 30
    )
}