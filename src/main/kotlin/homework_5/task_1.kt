package homework_5

import util.arithmetic_parser.ArithmeticElement
import java.io.File
import util.arithmetic_parser.ArithmeticTree.createArithmeticTree

private object FileManager {
    private const val FILENAME = "arithmeticExpression.txt"

    val filePath = javaClass.getResource(FILENAME).path

    fun readArithmeticExpressionFromFile(): String = File(filePath).readText()
}

private fun printIncorrectInputMessage() = println("Incorrect arithmetic expression!")

private fun printArithmeticTreeWithResult(arithmeticTree: ArithmeticElement?) {
    arithmeticTree ?: printIncorrectInputMessage().also { return }

    print(arithmeticTree)
    print("Result: ${arithmeticTree?.calculate()}")
}

fun main() {
    val arithmeticTree = createArithmeticTree(FileManager.readArithmeticExpressionFromFile())
    printArithmeticTreeWithResult(arithmeticTree)
}
