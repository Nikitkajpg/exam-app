import java.io.File

const val ERROR_LIST_FILENAME = "error_questions.txt"
const val FILE_PATH_FILENAME = "last_file_path.txt"
const val LAST_INDEX_FILENAME = "last_question_index.txt"

fun saveErrorQuestions(indices: Set<Int>) {
    try {
        File(ERROR_LIST_FILENAME).writeText(indices.joinToString(","))
    } catch (e: Exception) {
        println("Ошибка при сохранении списка ошибок: ${e.message}")
    }
}

fun loadErrorQuestions(): Set<Int> {
    val file = File(ERROR_LIST_FILENAME)
    if (!file.exists()) return emptySet()

    return try {
        file.readText().split(",").filter { it.isNotBlank() }.mapNotNull { it.trim().toIntOrNull() }.toSet()
    } catch (e: Exception) {
        println("Ошибка при загрузке списка ошибок: ${e.message}")
        emptySet()
    }
}

fun saveFilePath(path: String) {
    try {
        File(FILE_PATH_FILENAME).writeText(path)
    } catch (e: Exception) {
        println("Ошибка при сохранении пути к файлу: ${e.message}")
    }
}

fun loadFilePath(): String? {
    val file = File(FILE_PATH_FILENAME)
    return if (file.exists()) {
        try {
            file.readText().trim().takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            println("Ошибка при загрузке пути к файлу: ${e.message}")
            null
        }
    } else {
        null
    }
}

fun saveLastQuestionIndex(index: Int) {
    try {
        File(LAST_INDEX_FILENAME).writeText(index.toString())
    } catch (e: Exception) {
        println("Ошибка при сохранении последнего индекса: ${e.message}")
    }
}

fun loadLastQuestionIndex(): Int? {
    val file = File(LAST_INDEX_FILENAME)
    if (!file.exists()) return null

    return try {
        file.readText().trim().toIntOrNull()
    } catch (e: Exception) {
        println("Ошибка при загрузке последнего индекса: ${e.message}")
        null
    }
}