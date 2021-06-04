package util.arithmetic_parser

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import util.arithmetic_parser.ArithmeticTree.createArithmeticTree

internal class ArithmeticTreeTest {
    companion object {
        @JvmStatic
        fun expressionsWritten(): List<Arguments> = listOf(
            Arguments.of(
                "(+ 1 1)",
                """+
                  |....1
                  |....1
                  |""".trimMargin()
            ),
            Arguments.of(
                "(* 2 (+ 2 2))",
                """*
                  |....2
                  |....+
                  |........2
                  |........2
                  |""".trimMargin()
            ),
            Arguments.of (
                "(/ 6 (* 3 2))",
                """/
                  |....6
                  |....*
                  |........3
                  |........2
                  |""".trimMargin()
            ),
            Arguments.of (
                "(* (- 2 (- 2 1)) (- 3 2))",
                """*
                  |....-
                  |........2
                  |........-
                  |............2
                  |............1
                  |....-
                  |........3
                  |........2
                  |""".trimMargin(),
            )
        )

        @JvmStatic
        fun expressionsCalculated(): List<Arguments> = listOf(
            Arguments.of("(+ 1 2)", 3),
            Arguments.of("(- 4 3)", 1),
            Arguments.of("(* 2 4)", 8),
            Arguments.of("(/ 12 3)", 4),
            Arguments.of("(+ 2 -5)", -3),
            Arguments.of("(- 7 9)", -2),
            Arguments.of("(- 4 -3)", 7),
            Arguments.of("(- -4 4)", -8),
            Arguments.of("(* 5 -3)", -15),
            Arguments.of("(/ -10, 2)", -5),
            Arguments.of("(* (- 2 1) (- 3 2))", 1),
            Arguments.of("(/ (* (+ 2 2) 3)) 4)", 3),
            Arguments.of("(* (+ (/ 6 2) (- 3 4)) 2)", 4)
        )
    }

    @MethodSource("expressionsWritten")
    @ParameterizedTest(name = "getWrittenTest{index}")
    fun getWrittenTest(expression: String, expectedWrittenExpression: String) {
        val arithmeticTree = createArithmeticTree(expression)

        assertEquals(expectedWrittenExpression, arithmeticTree?.toString())
    }

    @MethodSource("expressionsCalculated")
    @ParameterizedTest(name = "calculateTest{index}")
    fun calculateTest(expression: String, expectedCalculation: Int) {
        val arithmeticTree = createArithmeticTree(expression)

        assertEquals(expectedCalculation, arithmeticTree?.calculate())
    }
}