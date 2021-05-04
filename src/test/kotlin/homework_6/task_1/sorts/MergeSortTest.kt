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
        @JvmStatic
        fun numbersList(): List<Arguments> = listOf(
            Arguments.of(mutableListOf<Int>()),
            Arguments.of(mutableListOf(1)),
            Arguments.of(mutableListOf(3, 1, 2)),
            Arguments.of(mutableListOf(-4, 5, 12, 0, 89, 44, 13, 7, 6, 3, 7))
        )

        @JvmStatic
        fun sortedLists(): List<Arguments> = listOf(
            Arguments.of(
                mutableListOf<Int>(),
                mutableListOf(1, 2, 3, 4)
            ),
            Arguments.of(
                mutableListOf(1, 2, 3, 4),
                mutableListOf<Int>()
            ),
            Arguments.of(
                mutableListOf(1, 3, 5, 7, 9),
                mutableListOf(2, 4, 6, 8, 10)
            ),
            Arguments.of(
                mutableListOf(-5, -3, -1, 1, 3, 5),
                mutableListOf(-4, -2, 4, 6, 10)
            )
        )
    }

    @MethodSource("sortedLists")
    @ParameterizedTest(name = "mergeTest{index}")
    fun mergeTest(firstList: MutableList<Int>, secondList: MutableList<Int>) {
        val mergedList = firstList.plus(secondList).sorted()
        assertEquals(mergedList, merge(firstList, secondList))
    }

    @MethodSource("sortedLists")
    @ParameterizedTest(name = "multithreadedMergeTest{index}")
    fun multithreadedMergeTest(firstList: MutableList<Int>, secondList: MutableList<Int>) {
        val mergedList = firstList.plus(secondList).sorted()
        for (i in 1..1000 step 100) {
            assertEquals(mergedList, multithreadedMerge(firstList, secondList, i))
        }
    }

    @MethodSource("numbersList")
    @ParameterizedTest(name = "mergeSortTest{index}")
    fun mergeSortTest(list: MutableList<Int>) {
        assertEquals(list.sorted(), list.mergeSorted())
    }

    @MethodSource("numbersList")
    @ParameterizedTest(name = "multithreadedMergeSortTest{index}")
    fun multithreadedMergeSortTest(list: MutableList<Int>) {
        for (i in 1..1000 step 100) {
            assertEquals(list.sorted(), list.multithreadedMergeSorted(i))
        }
    }
}