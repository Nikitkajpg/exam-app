package core

import QuizState
import models.Question

class QuizEngine {
    private val numericOptionGenerator = NumericOptionGenerator()

    fun generateQuestionState(question: Question, allAnswersPool: List<String>): QuizState {
        val options = generateOptions(question.answer, allAnswersPool)

        return QuizState(
            questionNumber = question.id, question = question.text, correctAnswer = question.answer, options = options
        )
    }

    private fun generateOptions(correctAnswer: String, allAnswers: List<String>): List<String> {
        val lowerAnswers = correctAnswer.lowercase()

        return when {
            isYesNoAnswer(lowerAnswers) -> generateYesNoOptions(correctAnswer)
            correctAnswer.any { it.isDigit() } -> generateNumericOptions(correctAnswer)
            else -> generateTextOptions(correctAnswer, lowerAnswers, allAnswers)
        }.shuffled()
    }

    private fun isYesNoAnswer(s: String) = s.startsWith("да") || s.startsWith("нет")

    private fun generateYesNoOptions(correct: String): List<String> {
        val other = if (correct.startsWith("да")) "нет" else "да"
        return listOf(correct, other)
    }

    private fun generateNumericOptions(correct: String): List<String> {
        val options = mutableSetOf<String>()
        var attempts = 0

        while (options.size < 3 && attempts < 20) {
            val variation = numericOptionGenerator.generateOneIncorrectNumericAnswer(correct)
            if (variation != correct) options.add(variation)
            attempts++
        }
        return (options + correct).toList()
    }

    private fun generateTextOptions(
        correctAnswer: String, lowerCorrect: String, allAnswers: List<String>
    ): List<String> {
        val incorrectOptions = allAnswers
            .filter { candidate ->
                val lowerCandidate = candidate.lowercase()
                lowerCandidate != lowerCorrect &&
                        !isYesNoAnswer(lowerCandidate) &&
                        !candidate.any(Char::isDigit)
            }
            .shuffled()
            .take(3)

        return (incorrectOptions + correctAnswer).shuffled()
    }
}