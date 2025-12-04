package ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.ControlPanel
import ui.components.QuizContent
import viewmodel.NavigatorVM
import viewmodel.UiState
import viewmodel.ViewModel
import java.awt.Window

@Composable
fun QuizScreen(window: Window?, state: UiState, viewModel: ViewModel, navigatorVM: NavigatorVM) {
    Column(modifier = Modifier.padding(16.dp)) {
        ControlPanel(window, state, viewModel, navigatorVM)

        if (state.isDataLoaded && state.currentQuiz != null) {
            QuizContent(state.currentQuiz, state.selectedOption, viewModel)
        } else {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Загрузите файл для начала")
            }
        }
    }
}