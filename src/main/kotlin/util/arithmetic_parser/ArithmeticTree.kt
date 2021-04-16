package util.arithmetic_parser

import util.isNumber
import java.lang.IllegalArgumentException

@Suppress("MagicNumber")
object ArithmeticTree {
    private data class OperationMembers(
        val operation: String,
        val firstExpression: String,
        val secondExpression: String
    )

    private fun String.findOperand(): String {
        var endIndex = 0
        var openedBrackets = 0

        for (currentIndex in this.indices) {
            if (this[currentIndex] == '(') openedBrackets++
            else if (this[currentIndex] == ')') openedBrackets--

            if (openedBrackets == 0) {
                endIndex = currentIndex
                break
            }
        }

        return this.substring(0..endIndex)
    }

    private fun String.getFirstNumberString(): String {
        var firstNumberStartIndex = indexOfFirst { it.isDigit() }
        if (firstNumberStartIndex == -1) return ""

        val digitsStartIndex = firstNumberStartIndex

        if (firstNumberStartIndex > 0 && this[firstNumberStartIndex - 1] == '-') firstNumberStartIndex--

        val firstNumberEndIndex = digitsStartIndex +
                this.substring(digitsStartIndex..lastIndex).indexOfFirst { !it.isDigit() }

        return this.substring(firstNumberStartIndex, firstNumberEndIndex)
    }

    private fun getExpressionMembers(expression: String): OperationMembers {
        val operation = expression[1].toString()

        val firstExpression =
            if (expression[3] == '(') {
                expression.substring(3..expression.lastIndex).findOperand()
            } else expression.getFirstNumberString()

        val secondExpressionMemberStartIndex = 3 + firstExpression.length + 1

        val secondExpression = if (expression[secondExpressionMemberStartIndex] == '(') {
            expression.substring(secondExpressionMemberStartIndex, expression.lastIndex).findOperand()
        } else expression.substring(secondExpressionMemberStartIndex..expression.lastIndex).getFirstNumberString()

        return OperationMembers(operation, firstExpression, secondExpression)
    }

    private fun createArithmeticBranch(expression: String, height: Int): ArithmeticElement? {
        val (operation, firstExpression, secondExpression) = getExpressionMembers(expression)

        val firstOperand: ArithmeticElement =
            if (firstExpression.isNumber()) Value(firstExpression.toInt(), height + 1)
            else createArithmeticBranch(firstExpression, height + 1)
                ?: throw IllegalArgumentException("Incorrect expression.")

        val secondOperand: ArithmeticElement =
            if (secondExpression.isNumber()) Value(secondExpression.toInt(), height + 1)
            else createArithmeticBranch(secondExpression, height + 1)
                ?: throw IllegalArgumentException("Incorrect expression.")

        return when (operation) {
            "+" -> Addition(firstOperand, secondOperand, height)
            "-" -> Subtraction(firstOperand, secondOperand, height)
            "*" -> Multiplication(firstOperand, secondOperand, height)
            "/" -> Division(firstOperand, secondOperand, height)
            else -> null
        }
    }

    fun createArithmeticTree(expression: String) =
        if (expression.isNumber()) Value(expression.toInt(), 0) else createArithmeticBranch(expression, 0)
}
