package ui

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import java.awt.FileDialog
import java.awt.Window

@Composable
fun LoadExcelButton(
    window: Window?, onPathSelected: (String) -> Unit
) {
    Button(onClick = {
        val filePath = openFileDialog(window, "Открыть .xlsx файл", ".xlsx")
        if (filePath != null) {
            onPathSelected(filePath)
        }
    }) {
        Text("Загрузить Excel")
    }
}

fun openFileDialog(window: Window?, title: String, allowedExtension: String): String? {
    val dialog = FileDialog(
        window as? java.awt.Frame, title, FileDialog.LOAD
    ).apply {
        setFilenameFilter { _, name -> name.endsWith(allowedExtension) }
        isVisible = true
    }
    return if (dialog.file != null) dialog.directory + dialog.file else null
}