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
    private val excelRepository: ExcelRepository = ExcelRepository(),
    private val settingsRepository: SettingsRepository = SettingsRepository(),
    private val quizEngine: QuizEngine = QuizEngine()
) {
    var uiState by mutableStateOf(UiState())
        private set

    private var allAnswersPool: List<String> = emptyList()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val lastPath = settingsRepository.loadFilePath()
        val lastIndex = settingsRepository.loadLastQuestionIndex()
        val errorIndices = settingsRepository.loadErrorQuestions()

        uiState = uiState.copy(
            currentQuestionIndex = lastIndex ?: 0,
            errorQuestionIndices = errorIndices,
            errorsCount = errorIndices.size,
            filePath = lastPath
        )

        if (lastPath != null) loadFile(lastPath)
    }

    fun loadFile(path: String) {
        scope.launch(Dispatchers.IO) {
            try {
                val questions = excelRepository.readExcel(path)
                allAnswersPool = questions.map { it.answer }

                settingsRepository.saveFilePath(path)

                withContext(Dispatchers.Main) {
                    uiState = uiState.copy(
                        questions = questions, currentQuestionIndex = 0, isDataLoaded = true
                    )
                    generateQuestion(uiState.currentQuestionIndex)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun generateQuestion(index: Int) {
        val questions = uiState.questions
        if (index !in questions.indices) {
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
        scope.launch(Dispatchers.IO) { settingsRepository.saveLastQuestionIndex(nextIndex) }
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
            errorQuestionIndices = newErrors, errorsCount = newErrors.size
        )
        scope.launch(Dispatchers.IO) {
            settingsRepository.saveErrorQuestions(newErrors)
        }
    }

    fun loadMistakeQuestion() {
        val indices = uiState.errorQuestionIndices
        if (indices.isEmpty()) return

        val mistakeIndex = indices.first()

        uiState = uiState.copy(
            selectedOption = null
        )

        generateQuestion(mistakeIndex)
    }
}