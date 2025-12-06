package ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.components.Instructions
import viewmodel.NavigatorVM

@Composable
fun AboutScreen(navigatorVM: NavigatorVM) {
    Column {
        Button(onClick = { navigatorVM.goToMenu() }) {
            Text("Меню")
        }
        Spacer(Modifier.height(16.dp))
        Instructions()
    }
}