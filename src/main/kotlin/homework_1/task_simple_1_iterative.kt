package homework_1

import libraries.Input

private fun findFactorialIterative(number: Long): Long {
    var factorial = 1L
    for (factor in 2L..number) {
        factorial *= factor
    }

    return factorial
}

fun main() {
    val number = Input.getNumber("Enter a non-negative number: ", Input.NumberType.POSITIVE, true)
    val factorial = findFactorialIterative(number)
    printResult(factorial)
}
