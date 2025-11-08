package ui

import ExcelData
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import openFileDialog
import readExcel
import saveFilePath
import java.awt.Window
import java.io.File

@Composable
fun LoadExcelButton(
    window: Window?, scope: CoroutineScope, onDataLoaded: (String, ExcelData) -> Unit
) {
    Button(onClick = {
        val filePath = openFileDialog(window, "Открыть .xlsx файл", ".xlsx")
        if (filePath != null) {
            saveFilePath(filePath)
            onDataLoaded("Загрузка...", emptyList())
            scope.launch {
                try {
                    val data = withContext(Dispatchers.IO) {
                        readExcel(filePath)
                    }

                    onDataLoaded(
                        "Файл: ${File(filePath).name}\nЗагружено строк: ${data.size}", data
                    )

                } catch (e: Exception) {
                    onDataLoaded("Ошибка чтения: ${e.message}", emptyList())
                }
            }
        }
    }) {
        Text("Загрузить Excel")
    }
}