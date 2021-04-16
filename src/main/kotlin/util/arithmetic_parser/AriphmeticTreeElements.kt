@file:Suppress("MagicNumber")
package util.arithmetic_parser

interface ArithmeticElement {
    override fun toString(): String
    fun calculate(): Int
}

class Value(private val value: Int, private val height: Int) : ArithmeticElement {
    fun getValue(): Int = value

    override fun toString(): String = "${".".repeat(4 * height)}$value\n"

    override fun calculate(): Int = value
}

abstract class Operation(
    val leftOperand: ArithmeticElement,
    val rightOperand: ArithmeticElement,
    val height: Int
) : ArithmeticElement {
    abstract val sign: Char

    override fun toString(): String =
        "${".".repeat(4 * height)}$sign\n$leftOperand$rightOperand"
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
