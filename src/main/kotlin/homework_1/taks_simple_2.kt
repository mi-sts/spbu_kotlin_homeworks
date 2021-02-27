package homework_1

import libraries.Input

fun findSubstringCount(str: String, subStr: String): Int {
    val strOccurrences = Regex(subStr).findAll(str)
    return strOccurrences.count()
}

fun printResult(count: Int) {
    println("Number of occurrences of second string in firs string: $count")
}

fun main() {
    val str = Input.getString()
    val subStr = Input.getString()
    val count = findSubstringCount(str, subStr)
    printResult(count)
}