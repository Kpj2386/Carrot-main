package com.example.carrot.Helper

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle


@Composable
fun TextView(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium) {
    Text(
        text = text,
        modifier = modifier,
        style = style
    )
}

@Composable
fun MyTitleLargeText() {
    TextView(text = "This is a large title.", style = MaterialTheme.typography.titleLarge)
}

@Composable
fun MyTitleMediumText() {
    Text(
        text = "This is title medium text.",
        style = MaterialTheme.typography.titleMedium
    )
}
@Composable
fun MyBodySmallText() {
    Text(
        text = "This is body small text.",
        style = MaterialTheme.typography.bodySmall
    )
}