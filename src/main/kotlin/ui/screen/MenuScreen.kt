package ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.LoadExcelButton
import viewmodel.NavigatorVM
import viewmodel.UiState
import viewmodel.ViewModel
import java.awt.Window
import java.io.File

@Composable
fun MenuScreen(window: Window?, viewModel: ViewModel, navigatorVM: NavigatorVM, onExit: () -> Unit, state: UiState) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isDataLoaded) {
            Text("Загружен файл: ${File(state.filePath!!).name}")
            Row {
                if (state.currentQuestionIndex != 0) {
                    Button(onClick = {
                        viewModel.changeCurrentQuestionIndex(state.currentQuestionIndex)
                        navigatorVM.goToQuiz()
                    }) {
                        Text("Продолжить с ${state.currentQuestionIndex} вопроса")
                    }
                    Spacer(Modifier.width(16.dp))
                }
                Button(onClick = {
                    viewModel.changeCurrentQuestionIndex(0)
                    navigatorVM.goToQuiz()
                }) {
                    Text("Начать тест сначала")
                }
            }
        } else {
            LoadExcelButton(window = window) { path ->
                viewModel.loadFile(path)
            }
        }
        Button(onClick = { navigatorVM.goToAbout() }) {
            Text("О программе")
        }
        Button(onClick = onExit) {
            Text("Выход")
        }
    }
}