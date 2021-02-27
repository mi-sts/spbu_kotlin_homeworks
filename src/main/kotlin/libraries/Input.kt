package libraries

class Input {
    companion object {
        fun create(): Input = Input()

        fun getString(): String {
            var str: String? = readLine()
            while (str == null) {
                println("Incorrect input! Enter a string:")
                str = readLine()
            }

            return str
        }
    }
}