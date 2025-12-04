import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val onExit = ::exitApplication

    Window(
        onCloseRequest = onExit, state = rememberWindowState(placement = WindowPlacement.Maximized), title = "Exam"
    ) {
        App(this.window, onExit)
    }
}