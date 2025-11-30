package core

import java.util.*
import kotlin.math.roundToInt

class NumericOptionGenerator {
    private val deltas = listOf(0.5, 1.0, 2.0, 5.0, 10.0, 15.0, 20.0, 25.0, 50.0, 75.0, 100.0, 150.0, 200.0)

    fun generateOneIncorrectNumericAnswer(correctAnswer: String): String {
        val fractionRegex = Regex("(\\d+)(\\s*/\\s*)(\\d+)")

        if (fractionRegex.containsMatchIn(correctAnswer)) {
            return fractionRegex.replace(correctAnswer) { matchResult ->
                val numerator = matchResult.groupValues[1]
                val separator = matchResult.groupValues[2]
                val oldDenominatorStr = matchResult.groupValues[3]

                val denominator = oldDenominatorStr.toDoubleOrNull() ?: return@replace matchResult.value

                val delta = getRandomDelta()
                var newDenominator = if (Math.random() > 0.5) denominator + delta else denominator - delta

                if (denominator > 0 && newDenominator <= 0.0) {
                    newDenominator = denominator + delta
                }

                val newDenominatorStr = formatNewNumber(oldDenominatorStr, newDenominator)
                "$numerator$separator$newDenominatorStr"
            }
        }

        val regex = Regex("(\\d+([.,]\\d+)?)")

        return regex.replace(correctAnswer) { matchResult ->
            val oldString = matchResult.value
            val number = oldString.replace(',', '.').toDoubleOrNull() ?: return@replace oldString

            val delta = getRandomDelta()
            var newNumber = if (Math.random() > 0.5) number + delta else number - delta

            if (number > 0 && newNumber <= 0.0) {
                newNumber = number + delta
            }

            formatNewNumber(oldString, newNumber)
        }
    }

    private fun getRandomDelta(): Double {
        return deltas.random()
    }

    private fun formatNewNumber(oldString: String, newNumber: Double): String {
        if (!oldString.contains('.') && !oldString.contains(',')) {
            return newNumber.roundToInt().toString()
        }

        val decimalPart = oldString.split(Regex("[.,]")).getOrNull(1)
        val decimalPlaces = decimalPart?.length ?: 1

        val formatString = "%.${decimalPlaces}f"
        val formatted = String.format(Locale.US, formatString, newNumber)

        return if (oldString.contains(',')) {
            formatted.replace('.', ',')
        } else {
            formatted
        }
    }
}