package ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.UiState
import viewmodel.ViewModel

@Composable
fun ControlPanel(state: UiState, viewModel: ViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (state.isDataLoaded) {
            Button(onClick = { viewModel.nextQuestion() }) {
                Text("Следующий вопрос")
            }
            Spacer(Modifier.width(8.dp))
        }
        MistakeListButton(viewModel, state.errorsCount)
    }
}