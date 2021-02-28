package homework_1
import libraries.Input

private fun printResult(factorial: Long) = println("Found the factorial: $factorial")

private fun findFactorialRecursive(number: Long): Long {
    return if (number == 1L) 1 else number * findFactorialRecursive(number - 1)
}

fun main() {
    val number = Input.getNaturalNumber(true)
    val factorial = findFactorialRecursive(number.toLong())
    printResult(factorial)
}
