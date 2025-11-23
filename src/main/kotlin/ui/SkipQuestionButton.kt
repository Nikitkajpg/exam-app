package ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SkipQuestionButton(onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = Modifier.padding(4.dp)
    ) {
        Text("Не знаю")
    }
}