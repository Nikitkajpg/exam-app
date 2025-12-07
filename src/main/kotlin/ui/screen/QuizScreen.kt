package ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.ControlPanel
import ui.components.QuizContent
import viewmodel.NavigatorVM
import viewmodel.UiState
import viewmodel.ViewModel

@Composable
fun QuizScreen(state: UiState, viewModel: ViewModel, navigatorVM: NavigatorVM) {
    Column(modifier = Modifier.padding(16.dp)) {
        ControlPanel(state, viewModel)

        if (state.isDataLoaded && state.currentQuiz != null) {
            QuizContent(state.currentQuiz, state.selectedOption, viewModel)
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Загрузите файл для начала")
            }
        }

        Spacer(Modifier.height(100.dp))
        Button(
            onClick = { navigatorVM.goToMenu() },
            modifier = Modifier.fillMaxWidth(0.6f).align(Alignment.CenterHorizontally)
        ) {
            Text("Меню")
        }
    }
}