package viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import core.QuizEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import models.ExcelRepository
import models.SettingsRepository

class ViewModel(
    private val scope: CoroutineScope,
    private val excelRepo: ExcelRepository = ExcelRepository(),
    private val settingsRepo: SettingsRepository = SettingsRepository(),
    private val quizEngine: QuizEngine = QuizEngine()
) {
    var uiState by mutableStateOf(UiState())
        private set

    private var allAnswersPool: List<String> = emptyList()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val lastPath = settingsRepo.loadFilePath()
        val lastIndex = settingsRepo.loadLastQuestionIndex()
        val errorIndices = settingsRepo.loadErrorQuestions()

        uiState = uiState.copy(
            currentQuestionIndex = lastIndex ?: 0,
            errorQuestionIndices = errorIndices,
            errorsCount = errorIndices.size,
            status = if (lastPath != null) "Последний файл: $lastPath." else "Выберите Excel-файл."
        )

        if (lastPath != null) loadFile(lastPath)
    }

    fun loadFile(path: String) {
        uiState = uiState.copy(status = "Загрузка")
        scope.launch(Dispatchers.IO) {
            try {
                val questions = excelRepo.readExcel(path)
                allAnswersPool = questions.map { it.answer }

                settingsRepo.saveFilePath(path)

                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        questions = questions,
                        currentQuestionIndex = 0,
                        status = "Загружено строк: ${questions.size}.",
                        isDataLoaded = true
                    )
                    generateQuestion(uiState.currentQuestionIndex)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(status = "Ошибка: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun generateQuestion(index: Int) {
        val questions = uiState.questions
        if (index !in questions.indices) {
            uiState = uiState.copy(status = "Вопросы закончились или индекс неверен")
            return
        }

        val questionModel = questions[index]
        val quizState = quizEngine.generateQuestionState(questionModel, allAnswersPool)

        uiState = uiState.copy(
            currentQuiz = quizState, currentQuestionIndex = index, selectedOption = null
        )
    }

    fun nextQuestion() {
        val nextIndex = uiState.currentQuestionIndex + 1
        generateQuestion(nextIndex)
        scope.launch(Dispatchers.IO) { settingsRepo.saveLastQuestionIndex(nextIndex) }
    }

    fun submitAnswer(answer: String) {
        val currentQuiz = uiState.currentQuiz ?: return
        val isCorrect = (answer == currentQuiz.correctAnswer)
        val index = uiState.currentQuestionIndex

        uiState = uiState.copy(selectedOption = answer)

        if (!isCorrect) {
            val newErrors = uiState.errorQuestionIndices + index
            updateErrors(newErrors)
            return
        }

        if (index in uiState.errorQuestionIndices) {
            val newErrors = uiState.errorQuestionIndices - index
            updateErrors(newErrors)
        }
    }

    private fun updateErrors(newErrors: Set<Int>) {
        uiState = uiState.copy(
            errorQuestionIndices = newErrors,
            errorsCount = newErrors.size
        )
        scope.launch(Dispatchers.IO) {
            settingsRepo.saveErrorQuestions(newErrors)
        }
    }

    fun loadMistakeQuestion() {
        val indices = uiState.errorQuestionIndices
        if (indices.isEmpty()) return

        val mistakeIndex = indices.first()
//        val newIndices = indices - mistakeIndex

        uiState = uiState.copy(
//            errorQuestionIndices = newIndices,
//            errorsCount = newIndices.size,
            selectedOption = null
        )

//        scope.launch(Dispatchers.IO) {
//            settingsRepo.saveErrorQuestions(newIndices)
//        }

        generateQuestion(mistakeIndex)
    }
}