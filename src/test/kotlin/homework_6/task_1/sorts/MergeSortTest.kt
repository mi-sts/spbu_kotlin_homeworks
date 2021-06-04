package homework_6.task_1.sorts

import homework_6.task_1.sorts.MergeSort.merge
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import homework_6.task_1.sorts.MergeSort.mergeSorted
import homework_6.task_1.sorts.MultithreadedMergeSort.multithreadedMerge
import homework_6.task_1.sorts.MultithreadedMergeSort.multithreadedMergeSorted

internal class MergeSortTest {
    companion object {
        private val threadsNumberRange = (1..1000 step 100).toList()
        private val numbers: List<List<Int>> = listOf(
            mutableListOf(),
            mutableListOf(1),
            mutableListOf(3, 1, 2),
            mutableListOf(-4, 5, 12, 0, 89, 44, 13, 7, 6, 3, 7)
        )

        val sortedNumbers: List<Pair<MutableList<Int>, MutableList<Int>>> = listOf(
            Pair(
                mutableListOf(),
                mutableListOf(1, 2, 3, 4)
            ),
            Pair(
                mutableListOf(1, 2, 3, 4),
                mutableListOf()
            ),
            Pair(
                mutableListOf(1, 3, 5, 7, 9),
                mutableListOf(2, 4, 6, 8, 10)
            ),
            Pair(
                mutableListOf(-5, -3, -1, 1, 3, 5),
                mutableListOf(-4, -2, 4, 6, 10)
            )
        )

        @JvmStatic
        fun numbersList(): List<Arguments> = numbers.map { Arguments.of(it) }

        @JvmStatic
        fun numbersListWithThreads(): List<Arguments> = threadsNumberRange.map {
            threadsNumber -> numbers.map { Arguments.of(it, threadsNumber) }
        }.flatten()

        @JvmStatic
        fun sortedLists(): List<Arguments> = sortedNumbers.map { Arguments.of(it.first, it.second)}

        @JvmStatic
        fun sortedListsWithThreads(): List<Arguments> = threadsNumberRange.map {
            threadsNumber -> sortedNumbers.map { Arguments.of(it.first, it.second, threadsNumber) }
        }.flatten()
    }

    @MethodSource("sortedLists")
    @ParameterizedTest(name = "mergeTest{index}")
    fun mergeTest(firstList: MutableList<Int>, secondList: MutableList<Int>) {
        val mergedList = firstList.plus(secondList).sorted()
        assertEquals(mergedList, merge(firstList, secondList))
    }

    @MethodSource("sortedListsWithThreads")
    @ParameterizedTest(name = "multithreadedMergeTest{index}")
    fun multithreadedMergeTest(firstList: MutableList<Int>, secondList: MutableList<Int>, numberOfThreads: Int) {
        val mergedList = firstList.plus(secondList).sorted()
        assertEquals(mergedList, multithreadedMerge(firstList, secondList, numberOfThreads))
    }

    @MethodSource("numbersList")
    @ParameterizedTest(name = "mergeSortTest{index}")
    fun mergeSortTest(list: MutableList<Int>) {
        assertEquals(list.sorted(), list.mergeSorted())
    }

    @MethodSource("numbersListWithThreads")
    @ParameterizedTest(name = "multithreadedMergeSortTest{index}")
    fun multithreadedMergeSortTest(list: MutableList<Int>, numberOfThreads: Int) {
        assertEquals(list.sorted(), list.multithreadedMergeSorted(numberOfThreads))
    }
}