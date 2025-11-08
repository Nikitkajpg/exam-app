package ui

import ExcelData
import QuizState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun NextQuestionButton(
    excelData: ExcelData, currentIndex: Int, onQuizGenerated: (QuizState, newIndex: Int) -> Unit
) {
    Button(
        onClick = {
            val rows = excelData.size
            if (rows <= 1) return@Button

            val nextIndex = if (currentIndex == 0 || currentIndex == rows - 1) {
                1
            } else {
                currentIndex + 1
            }

            val quizState = getQuizStateByIndex(excelData, nextIndex)

            onQuizGenerated(quizState, nextIndex)
        }, enabled = excelData.size > 1, elevation = androidx.compose.material.ButtonDefaults.elevation(8.dp)
    ) {
        Text(if (currentIndex == 0) "Начать сначала" else "Следующий вопрос")
    }
}