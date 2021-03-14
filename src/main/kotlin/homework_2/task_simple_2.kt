package homework_2

import libraries.Input
import libraries.deleteDuplicates

/**
 * Prints the array without duplicates.
 * @param[arrayWithoutDuplicates] the processed array.
 */
private fun printResult(arrayWithoutDuplicates: Array<Long>) {
    println("The number array without duplicates:")
    println(arrayWithoutDuplicates.contentToString())
}

fun main() {
    val preInputMessage = "Enter the numbers separated be a space."
    val numbersArray: Array<Long> = Input.getNumbersList(preInputMessage).toTypedArray()
    val numberArrayWithoutDuplicated: Array<Long> = numbersArray.deleteDuplicates()

    printResult(numberArrayWithoutDuplicated)
}