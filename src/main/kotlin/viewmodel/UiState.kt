package viewmodel

import QuizState
import models.Question

data class UiState(
    val questions: List<Question> = emptyList(),
    val currentQuiz: QuizState? = null,
    val status: String = "Готов к загрузке",
    val selectedOption: String? = null,
    val errorsCount: Int = 0,
    val isDataLoaded: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val errorQuestionIndices: Set<Int> = emptySet()
)
