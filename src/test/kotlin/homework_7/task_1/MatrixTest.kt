package homework_7.task_1

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
            ),
            Arguments.of(
                Matrix(arrayOf(
                    doubleArrayOf(-1.5, -3.0),
                    doubleArrayOf(-4.5, -6.0)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(-1.0, 3.0),
                    doubleArrayOf(5.0, -7.0)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(-13.5, 16.5),
                    doubleArrayOf(-25.5, 28.5)
                ))
            ),
            Arguments.of(
                Matrix(arrayOf(
                    doubleArrayOf(-1.5, 0.0),
                    doubleArrayOf(0.0, 2.0)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(1.0, 2.0),
                    doubleArrayOf(-3.0, 0.0)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(-1.5, -3.0),
                    doubleArrayOf(-6.0, 0.0)
                ))
            ),
            Arguments.of(
                Matrix(arrayOf(
                    doubleArrayOf(-4.5, 1.0, 0.0, 2.5),
                    doubleArrayOf(1.0, 3.0, -4.0, 0.5),
                    doubleArrayOf(0.0, 8.5, 2.0, 2.0),
                    doubleArrayOf(-0.5, 1.0, 6.5, 0.0),
                    doubleArrayOf(10.5, 2.5, 3.5, 3.0)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(1.5, -9.5, 3.5, 2.0, 1.0, 0.5, -0.5),
                    doubleArrayOf(4.0, 9.0, 7.0, 8.5, -8.0, 0.0, 6.0),
                    doubleArrayOf(-4.5, 1.0, 10.5, 11.0, 6.0, 2.0, -9.0),
                    doubleArrayOf(3.0, -8.0, 7.5, -3.5, 12.0, 2.0, 8.5)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(4.75,	31.75, 10.0, -9.25, 17.5, 2.75, 29.5),
                    doubleArrayOf(33.0, 9.5, -13.75, -18.25, -41.0, -6.5, 57.75),
                    doubleArrayOf(31.0, 62.5, 95.5, 87.25, -32.0, 8.0, 50.0),
                    doubleArrayOf(-26.0, 20.25, 73.5, 79.0, 30.5, 12.75, -52.25),
                    doubleArrayOf(19.0, -97.75,	113.5, 70.25, 47.5, 18.25, 3.75)
                ))
            ),
            Arguments.of(
                Matrix(arrayOf(
                    doubleArrayOf(1.5, 2.0, -1.0, 0.0, 10.5, -11.0, 6.5),
                    doubleArrayOf(7.0, -8.5, 9.0, 0.0, -1.5, 1.0, 0.5)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(1.0, 7.0, 9.0),
                    doubleArrayOf(7.5, 4.0, -1.5),
                    doubleArrayOf(5.5, -9.0, 4.5),
                    doubleArrayOf(0.0, -0.5, 0.5),
                    doubleArrayOf(-11.0, 10.0, 3.5),
                    doubleArrayOf(4.0, 3.0, 2.0),
                    doubleArrayOf(8.0, 9.0, -12.0)
                )),
                Matrix(arrayOf(
                    doubleArrayOf(-96.5, 158.0, -57.25),
                    doubleArrayOf(17.25, -73.5, 107.0)
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
