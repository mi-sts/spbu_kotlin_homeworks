package homework_1

import libraries.Input

private fun findFactorialRecursive(number: Long): Long {
    return if (number in 0..1) 1 else number * findFactorialRecursive(number - 1)
}

fun main() {
    val number = Input.getNumber("Enter a non-negative number: ", Input.NumberType.POSITIVE, true)
    printResult(findFactorialRecursive(number))
}
