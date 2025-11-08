package ui

import ExcelData
import QuizState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun RandomQuestionButton(
    excelData: ExcelData, onQuizGenerated: (QuizState, newIndex: Int) -> Unit
) {
    Button(
        onClick = {
            val rows = excelData.size
            if (rows <= 1 || excelData.first().size < 3) {
                onQuizGenerated(
                    QuizState(
                        "", "Ошибка: Нет данных или неправильная структура таблицы.", "", emptyList()
                    ), 0
                )
                return@Button
            }

            val randomIndex = (1 until rows).random()
            val quizState = getQuizStateByIndex(excelData, randomIndex)
            onQuizGenerated(quizState, randomIndex)
        }, elevation = androidx.compose.material.ButtonDefaults.elevation(8.dp)
    ) {
        Text("Случайный вопрос")
    }
}

fun generateQuizOptions(correctAnswer: String, allAnswers: Set<String>): List<String> {
    val lowerCaseAnswer = correctAnswer.lowercase()
    val isYesNoAnswer = lowerCaseAnswer.startsWith("да") || lowerCaseAnswer.startsWith("нет")

    return if (isYesNoAnswer) {
        val otherOption = if (lowerCaseAnswer.startsWith("да")) "Нет" else "Да"
        listOf(correctAnswer, otherOption).shuffled()
    } else {
        val availableIncorrectOptions = allAnswers.filter {
            val lc = it.lowercase()
            lc != lowerCaseAnswer && !lc.startsWith("да") && !lc.startsWith("нет")
        }.shuffled()

        val incorrectOptions = availableIncorrectOptions.take(3)
        (listOf(correctAnswer) + incorrectOptions).shuffled()
    }
}

fun getQuizStateByIndex(excelData: ExcelData, index: Int): QuizState {
    val row = excelData.getOrNull(index)

    if (index <= 0 || row.isNullOrEmpty() || row.size < 3) {
        return QuizState("ERR", "Ошибка: Неверный индекс или структура вопроса ($index).", "", emptyList())
    }

    val allAnswers = excelData.drop(1).mapNotNull { it.getOrNull(2)?.trim() }.toSet()

    val questionNumber = row.getOrNull(0) ?: "N/A"
    val question = row.getOrNull(1) ?: "Ошибка: Вопрос не найден (Колонка 1)"
    val correctAnswer = row.getOrNull(2)?.trim() ?: "Ошибка: Ответ не найден (Колонка 2)"

    val options = generateQuizOptions(correctAnswer, allAnswers)

    return QuizState(questionNumber, question, correctAnswer, options)
}