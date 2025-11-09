package ui

import ExcelData
import QuizState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt

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

    if (isYesNoAnswer) {
        val otherOption = if (lowerCaseAnswer.startsWith("да")) "Нет" else "Да"
        return listOf(correctAnswer, otherOption).shuffled()
    }

    val containsDigit = correctAnswer.any { it.isDigit() }
    if (containsDigit) {
        val incorrectOptions = mutableSetOf<String>()
        var attempts = 0

        while (incorrectOptions.size < 3 && attempts < 20) {
            val newVariation = generateOneIncorrectNumericAnswer(correctAnswer)

            if (newVariation != correctAnswer) {
                incorrectOptions.add(newVariation)
            }
            attempts++
        }

        return (listOf(correctAnswer) + incorrectOptions).shuffled()
    } else {
        val availableIncorrectOptions = allAnswers.filter {
            val lc = it.lowercase()
            lc != lowerCaseAnswer && !lc.startsWith("да") && !lc.startsWith("нет") && !it.any { c -> c.isDigit() }
        }.shuffled()

        val incorrectOptions = availableIncorrectOptions.take(3)
        return (listOf(correctAnswer) + incorrectOptions).shuffled()
    }
}

private fun generateOneIncorrectNumericAnswer(correctAnswer: String): String {
    val regex = Regex("(\\d+([.,]\\d+)?)")

    return regex.replace(correctAnswer) { matchResult ->
        val oldString = matchResult.value
        val number =
            oldString.replace(',', '.').toDoubleOrNull() ?: return@replace oldString // Ошибка -> вернуть как было

        val delta = getRandomDelta(number)

        var newNumber = if (Math.random() > 0.5) number + delta else number - delta

        if (number > 0 && newNumber <= 0.0) {
            newNumber = number + delta
        }

        formatNewNumber(oldString, newNumber)
    }
}

private fun getRandomDelta(number: Double): Double {
    val num = abs(number)
    val range = when {
        num == 0.0 -> (1..5)
        num < 10 -> (1..10)
        num < 100 -> (10..100)
        num < 1000 -> (100..500)
        else -> (500..1000)
    }
    return range.random().toDouble()
}

private fun formatNewNumber(oldString: String, newNumber: Double): String {
    if (!oldString.contains('.') && !oldString.contains(',')) {
        return newNumber.roundToInt().toString()
    }

    val decimalPart = oldString.split(Regex("[.,]")).getOrNull(1)
    val decimalPlaces = decimalPart?.length ?: 1

    val formatString = "%.${decimalPlaces}f"
    val formatted = String.format(Locale.US, formatString, newNumber)

    return if (oldString.contains(',')) {
        formatted.replace('.', ',')
    } else {
        formatted
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