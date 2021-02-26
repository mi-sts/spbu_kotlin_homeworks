package libraries

class Input {
    companion object {
        fun getInput(): Int {
            println("Enter a natural number:")
            var number = readLine()?.toInt() ?: 0
            while (number <= 0) {
                println("Incorrect input! Enter a natural number:")
                number = readLine()?.toInt() ?: 0
            }

            return number;
        }
    }
}
