package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InstructionsAndStats(totalQuestions: Int, errors: Int) {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Таблица Excel должна быть в формате:", style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = "Строка 1: Заголовки (игнорируются)\nКолонка 1: Номер вопроса\nКолонка 2: Вопрос\nКолонка 3: Ответ",
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Вопросов пройдено: $totalQuestions", style = MaterialTheme.typography.body1
            )
            Spacer(Modifier.width(20.dp))
            Text(
                text = "Ошибки: $errors",
                color = if (errors > 0) MaterialTheme.colors.error else LocalContentColor.current,
                style = MaterialTheme.typography.body1
            )
        }
    }
    Divider(Modifier.padding(top = 8.dp))
}