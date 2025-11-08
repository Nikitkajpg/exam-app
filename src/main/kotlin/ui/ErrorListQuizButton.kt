package ui

import ExcelData
import QuizState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun ErrorListQuizButton(
    excelData: ExcelData, errorIndices: Set<Int>, onQuizGenerated: (QuizState, newIndex: Int) -> Unit
) {
    Button(
        onClick = {
            val validErrorIndices = errorIndices.filter { it < excelData.size && it >= 1 }

            if (validErrorIndices.isEmpty()) {
                onQuizGenerated(
                    QuizState(
                        "INFO", "Список вопросов с ошибками пуст или не актуален для текущего файла.", "", emptyList()
                    ), 0
                )
                return@Button
            }

            val randomIndex = validErrorIndices.random()

            val quizState = getQuizStateByIndex(excelData, randomIndex)

            onQuizGenerated(quizState, randomIndex)
        }, enabled = excelData.size > 1 && errorIndices.isNotEmpty(), elevation = ButtonDefaults.elevation(8.dp)
    ) {
        Text("Следующий вопрос с ошибками (всего ${errorIndices.size})")
    }
}