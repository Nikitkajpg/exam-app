package ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.NavigatorVM
import viewmodel.UiState
import viewmodel.ViewModel

@Composable
fun ControlPanel(state: UiState, viewModel: ViewModel, navigatorVM: NavigatorVM) {
    Button(onClick = { navigatorVM.goToMenu() }) {
        Text("Меню")
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