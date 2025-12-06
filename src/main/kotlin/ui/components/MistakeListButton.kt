package ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import viewmodel.ViewModel

@Composable
fun MistakeListButton(
    viewModel: ViewModel, errorCount: Int
) {
    val isEnabled = errorCount > 0

    Button(
        onClick = {
            viewModel.loadMistakeQuestion()
        }, enabled = isEnabled
    ) {
        Text("Следующий вопрос с ошибками (осталось $errorCount)")
    }
}