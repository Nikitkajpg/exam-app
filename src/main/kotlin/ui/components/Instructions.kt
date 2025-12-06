package ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun Instructions() {
    val headers = listOf("Номер вопроса", "Текст вопроса", "Ответ")
    val questions = listOf(
        listOf("1", "Вопрос 1", "Ответ 1"),
        listOf("2", "Вопрос 2", "Ответ 2"),
        listOf("3", "Вопрос 3", "Ответ 3"),
        listOf("4", "Вопрос 4", "Ответ 4")
    )
    val columnWeights = listOf(0.1f, 0.5f, 0.4f)
    val borderThickness = 1.dp
    val cellPadding = 8.dp

    @Composable
    fun RowScope.TableExampleCell(
        text: String, weight: Float, isHeader: Boolean = false
    ) {
        val style = if (isHeader) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
        val backgroundColor =
            if (isHeader) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant

        Surface(
            modifier = Modifier.weight(weight).fillMaxHeight()
                .border(borderThickness, MaterialTheme.colorScheme.outlineVariant), color = backgroundColor
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(cellPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = if (isHeader) Alignment.CenterHorizontally else Alignment.Start
            ) {
                Text(
                    text = text,
                    style = style,
                    textAlign = if (isHeader) TextAlign.Center else TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Таблица Excel должна быть в формате:",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(modifier = Modifier.fillMaxWidth().border(borderThickness, MaterialTheme.colorScheme.outlineVariant)) {
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
            ) {
                headers.forEachIndexed { index, item ->
                    TableExampleCell(
                        text = item, weight = columnWeights.getOrElse(index) { 1f }, isHeader = true
                    )
                }
            }

            questions.forEach { rowData ->
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
                ) {
                    rowData.forEachIndexed { index, item ->
                        TableExampleCell(
                            text = item, weight = columnWeights.getOrElse(index) { 1f }, isHeader = false
                        )
                    }
                }
            }
        }
    }
}