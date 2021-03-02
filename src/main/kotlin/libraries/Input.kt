package libraries

object Input {
    fun getString(preInputMessage: String = ""): String {
        print(preInputMessage)

        var str: String? = readLine()
        while (str == null) {
            println("Incorrect input! Enter a string:")
            str = readLine()
        }

        return str
    }
}
