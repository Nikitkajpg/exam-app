package viewmodel

import ui.screen.Navigator
import ui.screen.Screen

class NavigatorVM(
    private val navigator: Navigator
) {
    fun goToMenu() = navigator.navigateTo(Screen.Menu)
    fun goToQuiz() = navigator.navigateTo(Screen.Quiz)
    fun goToAbout() = navigator.navigateTo(Screen.About)
}