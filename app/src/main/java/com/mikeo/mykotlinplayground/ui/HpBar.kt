package com.mikeo.mykotlinplayground.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun HpBar(
    currentHp: Int,
    maxHp: Int,
){
    val progress = (currentHp.toFloat() / maxHp).coerceIn(0f, 1f)

    val barColor = when {
        progress > 0.5f -> Color.Green
        progress > 0.25f -> Color.Yellow
        else -> Color.Red
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(16.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
            .background(Color.LightGray)
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth(progress)
                .height(16.dp)
                .background(barColor)
        )
    }

}
