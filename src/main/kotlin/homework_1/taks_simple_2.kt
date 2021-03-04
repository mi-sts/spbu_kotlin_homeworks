package homework_1
import libraries.Input
import libraries.findSubstringCount

fun printResult(count: Int) = println("Number of occurrences of second string in first string: $count")

fun main() {
    val str = Input.getString("Enter the containing string: ")
    val subStr = Input.getString("Enter the substring: ")
    printResult(str.findSubstringCount(subStr))
}
