package libraries

object Input {
    fun getNaturalNumber(preInputMessage: String = ""): Long {
        print(preInputMessage)

        var number = readLine()?.toLongOrNull() ?: 0L
        while (number <= 0) {
            println("Incorrect input! Enter a natural number:")
            number = readLine()?.toLongOrNull() ?: 0L
        }

        return number
    }

    fun getNonNegativeNumber(preInputMessage: String = ""): Long {
        print(preInputMessage)

        var number = readLine()?.toLongOrNull() ?: -1L
        while (number < 0) {
            println("Incorrect input! Enter a non-negative number:")
            number = readLine()?.toLongOrNull() ?: 0L
        }

        return number
    }
}
