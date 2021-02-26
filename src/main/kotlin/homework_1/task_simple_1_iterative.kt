package homework_1
import libraries.Input

private fun printResult(factorial: Long) = println("Found the factorial: $factorial")

private fun findFactorialIterative(number: Long): Long {
    var factorial = 1L
    for (factor in 2L..number) {
        factorial *= factor
    }

    return factorial
}

fun main() {
    val number = Input.getInput()
    val factorial = findFactorialIterative(number.toLong())
    printResult(factorial)
}
