package homework_1
import util.Input
import util.findSubstringCount

/**
 * Prints the number of string occurrences.
 * @param[count] the occurrence count.
 */
fun printResult(count: Int) = println("Number of occurrences of second string in first string: $count")

fun main() {
    val str = Input.getString("Enter the containing string: ")
    val subStr = Input.getString("Enter the substring: ")
    printResult(str.findSubstringCount(subStr))
}
