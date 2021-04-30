package homework_7

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.joinAll
import java.lang.IllegalArgumentException

data class Matrix(private val matrixArray: Array<DoubleArray>) {
    init {
        require(matrixArray.isNotEmpty() && matrixArray[0].isNotEmpty()) {
            IllegalArgumentException("The matrix size cannot be zero!") }
    }

    private val height = matrixArray.size
    private val width = matrixArray[0].size

    suspend operator fun times(other: Matrix): Matrix {
        require(width == other.height) {
            IllegalArgumentException("Incorrect matrix sizes for the multiplication!")
        }

        val resultMatrixArray: Array<DoubleArray> = Array(height) { DoubleArray(other.width) { 0.0 } }
        val requests = ArrayList<Job>()
        val scope = CoroutineScope(CoroutineName("Matrices multiplication"))

        for (i in matrixArray.indices)
            for (j in other.matrixArray[0].indices)
                for (k in matrixArray[0].indices)
                    requests.add(scope.launch {
                        resultMatrixArray[i][j] += matrixArray[i][k] * other.matrixArray[k][j]
                    })

        requests.joinAll()
        return Matrix(resultMatrixArray)
    }

    override fun equals(other: Any?): Boolean {
        return if (other !is Matrix) false
        else {
            var areMatrixArraysEqual = true
            if (width != other.width || height != other.height) areMatrixArraysEqual = false
            else {
                for (i in matrixArray.indices)
                    if (!matrixArray[i].contentEquals(other.matrixArray[i])) {
                        areMatrixArraysEqual = false
                        break
                    }
            }

            areMatrixArraysEqual
        }
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String = matrixArray.joinToString(separator = "\n") { it.joinToString(separator = " ") }
}
