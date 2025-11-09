import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ui.*
import java.io.File

@Composable
fun App(window: java.awt.Window?) {
    var excelData by remember { mutableStateOf<ExcelData>(emptyList()) }
    var status by remember { mutableStateOf("Нажмите 'Загрузить', чтобы выбрать файл.") }
    var currentQuiz by remember { mutableStateOf<QuizState?>(null) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var totalQuestions by remember { mutableStateOf(0) }
    var errors by remember { mutableStateOf(0) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var errorQuestionIndices by remember { mutableStateOf<Set<Int>>(emptySet()) }
    val errorListStatus by remember { mutableStateOf("") }
    var loadedLastIndex by remember { mutableStateOf<Int?>(null) }

    val scope = rememberCoroutineScope()

    fun saveErrors() {
        saveErrorQuestions(errorQuestionIndices)
    }

    fun saveLastIndex() {
        if (excelData.isNotEmpty() && currentQuestionIndex > 0) {
            saveLastQuestionIndex(currentQuestionIndex)
        }
    }

    LaunchedEffect(Unit) {
        val loadedIndices = loadErrorQuestions()
        errorQuestionIndices = loadedIndices

        loadedLastIndex = loadLastQuestionIndex()

        val savedPath = loadFilePath()
        if (savedPath != null) {
            val file = File(savedPath)
            if (file.exists()) {
                status = "Автоматическая загрузка последнего файла..."
                try {
                    val data = withContext(Dispatchers.IO) {
                        readExcel(savedPath)
                    }

                    status = "Файл: ${file.name}\nЗагружено строк: ${data.size} (Автозагрузка)"
                    excelData = data

                    loadedLastIndex?.let { index ->
                        if (index in 1 until data.size) {
                            currentQuestionIndex = index
                            currentQuiz = getQuizStateByIndex(data, index)
                        }
                    }
                } catch (e: Exception) {
                    status = "Ошибка автозагрузки файла '${file.name}': ${e.message}"
                }
            } else {
                status = "Последний файл не найден по пути: ${savedPath}"
            }
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            InstructionsAndStats(totalQuestions, errors)

            Spacer(Modifier.height(16.dp))

            LoadExcelButton(window, scope) { newStatus, data ->
                status = newStatus
                excelData = data
                currentQuiz = null
                selectedOption = null
                currentQuestionIndex = 0
            }

            Spacer(Modifier.height(16.dp))
            Text(status)

            if (excelData.isNotEmpty()) {
                Spacer(Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    RandomQuestionButton(excelData) { quizState, newIndex ->
                        currentQuiz = quizState
                        selectedOption = null
                        currentQuestionIndex = newIndex
                    }

                    Spacer(Modifier.width(16.dp))

                    NextQuestionButton(excelData, currentQuestionIndex) { quizState, newIndex ->
                        currentQuiz = quizState
                        selectedOption = null
                        currentQuestionIndex = newIndex
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    ErrorListQuizButton(excelData, errorQuestionIndices) { quizState, newIndex ->
                        currentQuiz = quizState
                        selectedOption = null
                        currentQuestionIndex = newIndex
                    }

                    Spacer(Modifier.width(16.dp))

                    Text(
                        text = errorListStatus,
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }
            }

            currentQuiz?.let { quiz ->
                Spacer(Modifier.height(24.dp))

                Text(
                    text = "${quiz.questionNumber}: ${quiz.question}", style = MaterialTheme.typography.h6
                )

                Spacer(Modifier.height(16.dp))

                AnswerOptionsGroup(
                    quiz = quiz, selected = selectedOption, onOptionSelected = { option ->
                        if (selectedOption == null) {
                            selectedOption = option

                            totalQuestions++
                            if (option != quiz.correctAnswer) {
                                errors++

                                if (currentQuestionIndex > 0) {
                                    errorQuestionIndices = errorQuestionIndices + currentQuestionIndex
                                    saveErrors()
                                }
                            } else {
                                if (errorQuestionIndices.contains(currentQuestionIndex)) {
                                    errorQuestionIndices = errorQuestionIndices - currentQuestionIndex
                                    saveErrors()
                                }
                            }

                            saveLastIndex()
                        }
                    })
            }
        }
    }
}