package util.ariphmetic_parser

abstract class ArithmeticElement(protected val height: Int) {
    companion object { const val PRINTING_POINTS_COUNT: Int = 4 }
    abstract fun getWritten(): String
    abstract fun calculate(): Int
}

class Value(private val value: Int, height: Int) : ArithmeticElement(height) {
    fun getValue(): Int = value

    override fun getWritten(): String = "${".".repeat(PRINTING_POINTS_COUNT * height)}$value\n"

    override fun calculate(): Int = value
}

abstract class Operation(
    val leftOperand: ArithmeticElement,
    val rightOperand: ArithmeticElement,
    height: Int
) : ArithmeticElement(height)

class Plus(
    leftOperand: ArithmeticElement,
    rightOperand: ArithmeticElement,
    height: Int
) : Operation(leftOperand, rightOperand, height) {
    override fun getWritten(): String =
        "${".".repeat(PRINTING_POINTS_COUNT * height)}+\n${leftOperand.getWritten()}${rightOperand.getWritten()}"

    override fun calculate(): Int = leftOperand.calculate() + rightOperand.calculate()
}

class Minus(
    leftOperand: ArithmeticElement,
    rightOperand: ArithmeticElement,
    height: Int
) : Operation(leftOperand, rightOperand, height) {
    override fun getWritten(): String =
        "${".".repeat(PRINTING_POINTS_COUNT * height)}-\n${leftOperand.getWritten()}${rightOperand.getWritten()}"

    override fun calculate(): Int = leftOperand.calculate() - rightOperand.calculate()
}

class Multiply(
    leftOperand: ArithmeticElement,
    rightOperand: ArithmeticElement,
    height: Int
) : Operation(leftOperand, rightOperand, height) {
    override fun getWritten(): String =
        "${".".repeat(PRINTING_POINTS_COUNT * height)}*\n${leftOperand.getWritten()}${rightOperand.getWritten()}"

    override fun calculate(): Int = leftOperand.calculate() * rightOperand.calculate()
}

class Divide(
    leftOperand: ArithmeticElement,
    rightOperand: ArithmeticElement,
    height: Int
) : Operation(leftOperand, rightOperand, height) {
    override fun getWritten(): String =
        "${".".repeat(PRINTING_POINTS_COUNT * height)}/\n${leftOperand.getWritten()}${rightOperand.getWritten()}"

    override fun calculate(): Int = leftOperand.calculate() / rightOperand.calculate()
}
