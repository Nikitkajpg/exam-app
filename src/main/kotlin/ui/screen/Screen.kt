package ui.screen

sealed class Screen {
    data object Menu : Screen()
    data object Quiz : Screen()
    data object About : Screen()
}