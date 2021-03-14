package libraries

/**
 * Input functions to get the correct data.
 */
object Input {
    /**
     * Receives the string from the user.
     * While given value is null, attempts to enter the number will be provided.
     * @param[preInputMessage] the message that will be printed at the beginning.
     * @return the string received from the user.
     */
    fun getString(preInputMessage: String = ""): String {
        print(preInputMessage)

        var str: String? = readLine()
        while (str == null) {
            println("Incorrect input! Enter a string:")
            str = readLine()
        }

        return str
    }

    /**
     * Types of input numbers.
     */
    enum class NumberType { INTEGER, POSITIVE, NEGATIVE }

    /**
     * Checks the occurrence of a given number in this type of number.
     * @param[number] the number to check.
     * @param[type] the type for which the occurrence is checked.
     * @param[includingZero] is zero is included in the range of the number type.
     * @return is the number in the range(bool).
     */
    private fun isNumberOccur(number: Long, type: NumberType, includingZero: Boolean): Boolean {
        if (number == 0L) return includingZero

        return when (type) {
            NumberType.INTEGER -> true
            NumberType.NEGATIVE -> number < 0L
            NumberType.POSITIVE -> number > 0L
        }
    }

    /**
     * Receives the number of given type from the user.
     * While given value is incorrect, attempts to enter the number will be provided.
     * @param[preInputMessage] the message that will be printed at the beginning.
     * @param[type] the type of received number.
     * @param[includingZero] is zero is included in the range of the number type.
     * @return the number of given type received from the user.
     */
    fun getNumber(preInputMessage: String = "", type: NumberType, includingZero: Boolean = true): Long {
        var numberTypeStr = ""

        when (type) {
            NumberType.INTEGER -> {
                numberTypeStr = "an integer"
                if (!includingZero) numberTypeStr += "(excluding zero)"
            }
            NumberType.NEGATIVE -> {
                numberTypeStr = "a negative"
                if (includingZero) numberTypeStr = "a non-positive"
            }
            NumberType.POSITIVE -> {
                numberTypeStr = "a positive"
                if (includingZero) numberTypeStr = "a non-negative"
            }
        }

        print(preInputMessage)

        var number = readLine()?.toLongOrNull()
        while (number == null || !isNumberOccur(number, type, includingZero)) {
            println("Incorrect input! Enter $numberTypeStr number:")
            number = readLine()?.toLongOrNull()
        }

        return number
    }

    /**
     * Receives the list of integer numbers from the user.
     * They are entered separated by a space.
     * While the number of recognized numbers equals zero, attempts to enter them again will be provided.
     * @param[preInputMessage] the message that will be printed at the beginning.
     * @return the list of integer numbers received from the user.
     */
    fun getNumbersList(preInputMessage: String = ""): List<Long> {
        print(preInputMessage)
        var inputNumbers = getString().parseIntegers()
        while (inputNumbers.count() == 0) {
            println("Incorrect input! Enter the numbers separated by a space:")
            inputNumbers = getString().parseIntegers()
        }

        return inputNumbers
    }
}
