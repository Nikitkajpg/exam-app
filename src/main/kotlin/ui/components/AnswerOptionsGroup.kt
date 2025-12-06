package ui.components

import QuizState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun AnswerOptionsGroup(
    quiz: QuizState, selected: String?, onOptionSelected: (String) -> Unit
) {
    val answerGiven = selected != null
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.selectableGroup().padding(horizontal = 16.dp).verticalScroll(scrollState)
    ) {
        quiz.options.forEach { text ->

            val backgroundColor = if (answerGiven) {
                when (text) {
                    quiz.correctAnswer -> MaterialTheme.colorScheme.secondary
                    selected -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.surface
                }
            } else {
                MaterialTheme.colorScheme.surface
            }

            val contentColor = if (backgroundColor == MaterialTheme.colorScheme.surface) {
                LocalContentColor.current
            } else {
                MaterialTheme.colorScheme.onPrimary
            }

            Surface(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(Alignment.CenterVertically)
                    .padding(vertical = 4.dp).selectable(
                        selected = (text == selected),
                        enabled = !answerGiven,
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    ), color = backgroundColor, contentColor = contentColor, shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == selected),
                        onClick = null,
                        enabled = !answerGiven,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = if (answerGiven) contentColor else MaterialTheme.colorScheme.primary,
                            unselectedColor = contentColor
                        )
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(text = text, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}