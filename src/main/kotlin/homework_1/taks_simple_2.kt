package homework_1
import libraries.Input
import kotlin.math.min

fun findSubstringCount(str: String, subStr: String): Int {
    var occurrencesCount = 0

    str.forEachIndexed {
            index, _ -> if (str.substring(index, min(str.length, index + subStr.length)) == subStr)
                occurrencesCount += 1
    }

    return occurrencesCount
}

fun printResult(count: Int) {
    println("Number of occurrences of second string in first string: $count")
}

fun main() {
    val str = Input.getString()
    val subStr = Input.getString()
    val count = findSubstringCount(str, subStr)
    printResult(count)
}
