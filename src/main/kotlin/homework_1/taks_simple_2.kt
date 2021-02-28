package homework_1
import libraries.Input

fun findSubstringCount(str: String, subStr: String): Int {
    var occurrencesCount = 0
    for (i in 0..str.length - subStr.length) {
        var isOсcur = true
        for (j in subStr.indices)
            if (subStr[j] != str[i + j]) {
                isOсcur = false
                break
            }

        if (isOсcur)
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
