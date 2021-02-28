package libraries

class Input private constructor() {
    companion object {
        fun create(): Input = Input()

        fun getNonNegativeNumber(enableFirstMessage: Boolean = true): Int {
            if (enableFirstMessage) println("Enter a non-negative number:")

            var number = readLine()?.toIntOrNull() ?: -1
            while (number < 0) {
                println("Incorrect input! Enter a non-negative number:")
                number = readLine()?.toIntOrNull() ?: 0
            }

            return number
        }

        fun getNumber(enableFirstMessage: Boolean = true): Int {
            if (enableFirstMessage) println("Enter a number:")

            var number = readLine()?.toIntOrNull()
            while (number == null) {
                println("Incorrect input! Enter a number:")
                number = readLine()?.toIntOrNull()
            }

            return number
        }
    }
}
