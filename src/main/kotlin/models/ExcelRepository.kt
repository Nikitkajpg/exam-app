package models

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File
import java.io.IOException

class ExcelRepository {

    fun readExcel(path: String): List<Question> {
        val file = File(path)
        if (!file.exists()) throw IOException("Файл не найден: $path")

        return try {
            WorkbookFactory.create(file).use { workbook ->
                val sheet = workbook.getSheetAt(0)

                sheet.drop(1).mapNotNull { row ->
                    val qId = readCellAsString(row.getCell(0))
                    val qText = readCellAsString(row.getCell(1))
                    val qAns = readCellAsString(row.getCell(2))

                    if (qText != null && qAns != null) {
                        Question(
                            id = qId ?: "N/A", text = qText, answer = qAns
                        )
                    } else {
                        null
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw IOException("Ошибка чтения Excel")
        }
    }

    private fun readCellAsString(cell: Cell?): String? {
        return when (cell?.cellType) {
            CellType.STRING -> cell.stringCellValue.trim().lowercase()
            CellType.NUMERIC -> formatNumericValue(cell.numericCellValue).lowercase()
            CellType.BOOLEAN -> cell.booleanCellValue.toString().lowercase()
            else -> null
        }
    }

    private fun formatNumericValue(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            "%.1f".format(value)
        }
    }
}