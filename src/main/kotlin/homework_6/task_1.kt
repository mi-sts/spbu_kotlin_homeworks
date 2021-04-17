package homework_6

import util.sorts.MultithreadedMergeSort.multithreadedMergeSorted
import java.lang.IllegalArgumentException
import kotlin.random.Random
import kotlin.system.measureNanoTime

fun getSortStaticsDependingOnThreads(numberOfElements: Int, threadsRange: IntRange, threadsStep: Int,
                                     numberOfRepetitions: Int): Map<Int, Double> {
    if (threadsRange.first <= 0) throw IllegalArgumentException("The number of threads should be positive!")
    if (numberOfElements <= 0) throw IllegalArgumentException("The number of sorting elements should be positive!")

    val generalTimeData: MutableMap<Int, Long> = mutableMapOf()
    repeat(numberOfRepetitions) {
        for (i in threadsRange step threadsStep) {
            val elements = List(numberOfElements) { Random.nextInt() }
            val elapsedTime = measureNanoTime {
                elements.multithreadedMergeSorted(i)
            }
            generalTimeData[i] = generalTimeData[i] ?: 0 + elapsedTime
        }
    }

    return generalTimeData.mapValues { it.value / numberOfRepetitions.toDouble() }
}

fun getSortStaticsDependingOnElementsNumber(numberOfThreads: Int, elementsRange: IntRange, elementsStep: Int,
                                            numberOfRepetitions: Int): Map<Int, Double> {
    if (elementsRange.first <= 0) throw IllegalArgumentException("The number of elements should be positive!")
    if (numberOfThreads <= 0) throw IllegalArgumentException("The number of threads should be positive!")

    val generalTimeData: MutableMap<Int, Long> = mutableMapOf()
    repeat(numberOfRepetitions) {
        for (i in elementsRange step elementsStep) {
            val elements = List(i) { Random.nextInt() }
            val elapsedTime = measureNanoTime {
                elements.multithreadedMergeSorted(numberOfThreads)
            }
            generalTimeData[i] = generalTimeData[i] ?: 0 + elapsedTime
        }
    }

    return generalTimeData.mapValues { it.value / numberOfRepetitions.toDouble() }
}
