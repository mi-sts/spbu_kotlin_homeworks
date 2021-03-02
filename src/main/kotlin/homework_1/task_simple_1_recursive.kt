package homework_1

import libraries.Input

private fun findFactorialRecursive(number: Long): Long {
    return if (number == 1L) 1 else number * findFactorialRecursive(number - 1)
}

fun main() {
    val number = Input.getNonNegativeNumber("Enter a non-negative number: ")
    val factorial = findFactorialRecursive(number)
    printResult(factorial)
}
