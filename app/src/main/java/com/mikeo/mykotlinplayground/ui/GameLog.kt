package com.mikeo.mykotlinplayground.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun GameLog(
    log: List<String>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    textColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Black
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()


    ) {
        items(log) { entry ->
            Text(
                text = entry,
                fontSize = 14.sp,
                color = textColor,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}
