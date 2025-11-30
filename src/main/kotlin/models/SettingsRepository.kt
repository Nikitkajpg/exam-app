package models

import java.io.File

class SettingsRepository {
    private val errorListFile = File("error_questions.txt")
    private val filePathFile = File("last_file_path.txt")
    private val lastIndexFile = File("last_question_index.txt")

    fun saveErrorQuestions(indices: Set<Int>) {
        try {
            if (indices.isEmpty()) {
                if (errorListFile.exists()) errorListFile.delete()
            } else {
                errorListFile.writeText(indices.joinToString(","))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadErrorQuestions(): Set<Int> {
        if (!errorListFile.exists()) return emptySet()

        return try {
            errorListFile.readText().split(",").filter { it.isNotBlank() }.mapNotNull { it.trim().toIntOrNull() }
                .toSet()
        } catch (e: Exception) {
            e.printStackTrace()
            emptySet()
        }
    }

    fun saveFilePath(path: String) {
        try {
            filePathFile.writeText(path)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadFilePath(): String? {
        if (!filePathFile.exists()) return null
        return try {
            filePathFile.readText().trim().takeIf { it.isNotEmpty() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveLastQuestionIndex(index: Int) {
        try {
            lastIndexFile.writeText(index.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadLastQuestionIndex(): Int? {
        if (!lastIndexFile.exists()) return null
        return try {
            lastIndexFile.readText().trim().toIntOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}