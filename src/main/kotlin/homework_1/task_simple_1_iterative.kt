package homework_1

import libraries.Input

/**
 * Calculates the factorial iteratively.
 * @param[number] the value used to calculate the factorial.
 * @return the factorial of the number.
**/
private fun findFactorialIterative(number: Long): Long {
    var factorial = 1L
    for (factor in 2L..number) {
        factorial *= factor
    }

    return factorial
}

fun main() {
    val number = Input.getNumber("Enter a non-negative number: ", Input.NumberType.POSITIVE, true)
    printResult(findFactorialIterative(number))
}
