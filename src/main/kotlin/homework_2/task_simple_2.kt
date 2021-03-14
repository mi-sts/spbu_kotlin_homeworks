package homework_2

import libraries.Input
import libraries.deleteDuplicates

private fun printResult(arrayWithoutDuplicates: LongArray) {
    println("The number array without duplicates:")
    println(arrayWithoutDuplicates.contentToString())
}

fun main() {
    val preInputMessage = "Enter the numbers separated be a space:\n"
    val numbersArray = Input.getNumbersList(preInputMessage).toTypedArray()
    val numberArrayWithoutDuplicated = numbersArray.deleteDuplicates()

    printResult(numberArrayWithoutDuplicated.toLongArray())
}
