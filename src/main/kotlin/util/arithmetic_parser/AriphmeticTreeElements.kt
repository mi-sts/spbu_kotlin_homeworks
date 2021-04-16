package util.arithmetic_parser

interface ArithmeticElement {
    companion object { const val PRINTING_POINTS_COUNT: Int = 4 }
    override fun toString(): String
    fun calculate(): Int
}

class Value(private val value: Int, private val height: Int) : ArithmeticElement {
    fun getValue(): Int = value

    override fun toString(): String = "${".".repeat(ArithmeticElement.PRINTING_POINTS_COUNT * height)}$value\n"

    override fun calculate(): Int = value
}

abstract class Operation(
    val leftOperand: ArithmeticElement,
    val rightOperand: ArithmeticElement,
    val height: Int
) : ArithmeticElement {
    abstract val sign: Char

    override fun toString(): String =
        "${".".repeat(ArithmeticElement.PRINTING_POINTS_COUNT * height)}$sign\n$leftOperand$rightOperand"
}

class Addition(
    leftOperand: ArithmeticElement,
    rightOperand: ArithmeticElement,
    height: Int
) : Operation(leftOperand, rightOperand, height) {
    override val sign: Char = '+'

    override fun calculate(): Int = leftOperand.calculate() + rightOperand.calculate()
}

class Subtraction(
    leftOperand: ArithmeticElement,
    rightOperand: ArithmeticElement,
    height: Int
) : Operation(leftOperand, rightOperand, height) {
    override val sign: Char = '-'

    override fun calculate(): Int = leftOperand.calculate() - rightOperand.calculate()
}

class Multiplication(
    leftOperand: ArithmeticElement,
    rightOperand: ArithmeticElement,
    height: Int
) : Operation(leftOperand, rightOperand, height) {
    override val sign: Char = '*'

    override fun calculate(): Int = leftOperand.calculate() * rightOperand.calculate()
}

class Division(
    leftOperand: ArithmeticElement,
    rightOperand: ArithmeticElement,
    height: Int
) : Operation(leftOperand, rightOperand, height) {
    override val sign: Char = '/'

    override fun calculate(): Int = leftOperand.calculate() / rightOperand.calculate()
}
