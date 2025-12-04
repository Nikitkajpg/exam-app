package ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import viewmodel.UiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HelpButton(
    state: UiState
) {
    TooltipArea(
        tooltip = {
            Surface(
                shape = RoundedCornerShape(4.dp), color = Color.LightGray, elevation = 4.dp
            ) {
                InstructionsAndStats(state.questions.size, state.errorsCount)
            }
        }, delayMillis = 0, tooltipPlacement = TooltipPlacement.CursorPoint(offset = DpOffset(8.dp, 8.dp))
    ) {
        Button(onClick = {}) {
            Text("?")
        }
    }
}