import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream

fun readExcel(filePath: String): ExcelData {
    val data = mutableListOf<List<String>>()
    val file = FileInputStream(filePath)
    val workbook = XSSFWorkbook(file)
    val sheet = workbook.getSheetAt(0)

    for (row in sheet) {
        val rowData = mutableListOf<String>()
        for (cell in row) {
            when (cell.cellType) {
                CellType.STRING -> rowData.add(cell.stringCellValue)
                CellType.NUMERIC -> {
                    val numericValue = cell.numericCellValue
                    if (numericValue == numericValue.toInt().toDouble()) {
                        rowData.add(numericValue.toInt().toString())
                    } else {
                        rowData.add(numericValue.toString())
                    }
                }

                CellType.BOOLEAN -> rowData.add(cell.booleanCellValue.toString())
                else -> rowData.add("")
            }
        }
        data.add(rowData)
    }

    workbook.close()
    file.close()
    return data
}