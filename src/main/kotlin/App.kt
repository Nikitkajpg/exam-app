import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import ui.screen.*
import viewmodel.NavigatorVM
import viewmodel.ViewModel
import java.awt.Window

@Composable
fun App(window: Window?, onExit: () -> Unit) {
    val scope = rememberCoroutineScope()
    val viewModel = remember { ViewModel(scope) }
    val state = viewModel.uiState

    val navigator = remember { Navigator() }
    val navigatorVM = remember { NavigatorVM(navigator) }

    MaterialTheme {
        when (navigator.currentScreen) {
            Screen.Menu -> MenuScreen(window, viewModel, navigatorVM, onExit, state)
            Screen.Quiz -> QuizScreen(state, viewModel, navigatorVM)
            Screen.About -> AboutScreen(navigatorVM)
        }
    }
}

