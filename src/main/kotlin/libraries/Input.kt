package libraries

object Input {
    fun getNaturalNumber(preInputMessage: String = ""): Int {
        println(preInputMessage)

        var number = readLine()?.toInt() ?: 0
        while (number <= 0) {
            println("Incorrect input! Enter a natural number:")
            number = readLine()?.toInt() ?: 0
        }

        return number
    }

    fun getNonNegativeNumber(preInputMessage: String = ""): Int {
        println(preInputMessage)

        var number = readLine()?.toIntOrNull() ?: -1
        while (number < 0) {
            println("Incorrect input! Enter a non-negative number:")
            number = readLine()?.toIntOrNull() ?: 0
        }

        return number
    }
}
