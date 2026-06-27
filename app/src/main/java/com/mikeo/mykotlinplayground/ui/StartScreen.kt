package com.mikeo.mykotlinplayground.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun StartScreen(
    startName: String,
    onNameEntered: (String) -> Unit) {

    var name by remember(startName) { mutableStateOf(startName)}


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .clip(RoundedCornerShape(48.dp))
            .background(Color(0xFF26C6DA)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){
        Text(
            text = "Wer bist du?",
            fontSize = 24.sp
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                if (it.length <= 12) name = it },
            label = { Text("Name")},
            modifier = Modifier.padding(16.dp)
        )

        GameButtonHoch(
            text = "Spielen!",
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(70.dp),
            onClick = { onNameEntered(name)}
        )
    }
}