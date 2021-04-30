package homework_7

import kotlinx.coroutines.runBlocking

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class MatrixTest {

    companion object {
        @JvmStatic
        fun multiplicationMatrices(): List<Arguments> = listOf(
            Arguments.of(
                Matrix(arrayOf(
                    doubleArrayOf(1.0, 2.0),
                    doubleArrayOf(3.0, 4.0)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(1.0, 2.0),
                    doubleArrayOf(3.0, 4.0)
                    )),
                Matrix(arrayOf(
                    doubleArrayOf(7.0, 10.0),
                    doubleArrayOf(15.0, 22.0)
                    ))
            ),
            Arguments.of(
                Matrix(arrayOf(
                    doubleArrayOf(1.0, 3.0, 5.0),
                    doubleArrayOf(2.0, 4.0, 6.0)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(1.0, 2.0, 3.0, 4.0),
                    doubleArrayOf(5.0, 6.0, 7.0, 8.0),
                    doubleArrayOf(9.0, 10.0, 11.0, 12.0),
                )),
                Matrix(arrayOf(
                    doubleArrayOf(61.0, 70.0, 79.0, 88.0),
                    doubleArrayOf(76.0, 88.0, 100.0, 112.0)
                ))
            ),
            Arguments.of(
                Matrix(arrayOf(doubleArrayOf(2.0))),
                Matrix(arrayOf(doubleArrayOf(3.0))),
                Matrix(arrayOf(doubleArrayOf(6.0)))
            ),
            Arguments.of(
                Matrix(arrayOf(
                    doubleArrayOf(0.5, 1.0),
                    doubleArrayOf(1.5, 2.0)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(1.0, 2.0, 3.0),
                    doubleArrayOf(4.0, 5.0, 6.0)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(4.5, 6.0, 7.5),
                    doubleArrayOf(9.5, 13.0, 16.5)
                ))
            )
        )
    }

    @MethodSource("multiplicationMatrices")
    @ParameterizedTest(name = "multiplicationTest{index}")
    fun timesTest(firstMatrix: Matrix, secondMatrix: Matrix, resultMatrix: Matrix) {
        assertEquals(resultMatrix, runBlocking { firstMatrix * secondMatrix })
    }
}
