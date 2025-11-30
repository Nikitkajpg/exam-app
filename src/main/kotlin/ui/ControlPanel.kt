package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.UiState
import viewmodel.ViewModel
import java.awt.Window

@Composable
fun ControlPanel(window: Window?, state: UiState, viewModel: ViewModel) {
    Row {
        HelpButton(state)
        Spacer(Modifier.width(8.dp))
        LoadExcelButton(window) { path -> viewModel.loadFile(path) }
    }
    Text(state.status, style = MaterialTheme.typography.caption, modifier = Modifier.padding(vertical = 8.dp))

    Row {
        if (state.isDataLoaded) {
            Button(onClick = { viewModel.nextQuestion() }) {
                Text("Следующий")
            }
            Spacer(Modifier.width(8.dp))
        }
        MistakeListButton(viewModel, state.errorsCount)
    }
}