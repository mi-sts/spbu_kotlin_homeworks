package libraries

object Input {
    enum class NumberType { INTEGER, POSITIVE, NEGATIVE }

    private fun isNumberOccur(number: Long, type: NumberType, includingZero: Boolean): Boolean {
        if (!includingZero && number == 0L) return false

        return when (type) {
            NumberType.INTEGER -> true
            NumberType.NEGATIVE -> number < 0L
            NumberType.POSITIVE -> number > 0L
        }
    }

    fun getNumber(preInputMessage: String = "", type: NumberType, includingZero: Boolean = true): Long {
        var numberTypeStr = ""

        when (type) {
            NumberType.INTEGER -> {
                numberTypeStr = "an integer"
                if (!includingZero) numberTypeStr += "(excluding number)"
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
}
