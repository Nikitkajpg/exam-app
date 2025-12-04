package ui.components

import QuizState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.ViewModel

@Composable
fun QuizContent(quizState: QuizState, selectedOption: String?, viewModel: ViewModel) {
    Column {
        Text(
            text = "Вопрос ${quizState.questionNumber}: ${quizState.question}", style = MaterialTheme.typography.h6
        )
        Spacer(Modifier.height(16.dp))
        AnswerOptionsGroup(
            quiz = quizState, selected = selectedOption, onOptionSelected = { viewModel.submitAnswer(it) })
    }
}