import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.*
import viewmodel.ViewModel
import java.awt.Window

@Composable
fun App(window: Window?) {
    val scope = rememberCoroutineScope()
    val viewModel = remember { ViewModel(scope) }
    val state = viewModel.uiState

    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ControlPanel(window, state, viewModel)

            if (state.isDataLoaded && state.currentQuiz != null) {
                QuizContent(state.currentQuiz, state.selectedOption, viewModel)
            } else {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Загрузите файл для начала")
                }
            }
        }
    }
}

