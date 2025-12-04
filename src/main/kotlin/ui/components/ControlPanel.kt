package ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.NavigatorVM
import viewmodel.UiState
import viewmodel.ViewModel
import java.awt.Window

@Composable
fun ControlPanel(window: Window?, state: UiState, viewModel: ViewModel, navigatorVM: NavigatorVM) {
    Row {
        Button(onClick = { navigatorVM.goToMenu() }) {
            Text("Меню")
        }
        Spacer(Modifier.width(8.dp))
        HelpButton(state)
        Spacer(Modifier.width(8.dp))
        LoadExcelButton(window) { path -> viewModel.loadFile(path) }
    }

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