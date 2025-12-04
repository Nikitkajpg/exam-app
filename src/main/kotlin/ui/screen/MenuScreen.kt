package ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            Button(onClick = { navigatorVM.goToQuiz() }) {
                Text("Начать тест")
            }
        } else {
            LoadExcelButton(window = window) { path ->
                viewModel.loadFile(path)
            }
        }
        Button(onClick = onExit) {
            Text("Выход")
        }
    }
}