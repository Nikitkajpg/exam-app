package ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Navigator {
    var currentScreen by mutableStateOf<Screen>(Screen.Menu)
        private set

    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }
}