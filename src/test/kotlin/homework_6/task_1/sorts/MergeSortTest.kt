package homework_6.task_1.sorts

import homework_6.task_1.sorts.MergeSort.merge
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import homework_6.task_1.sorts.MergeSort.mergeSorted
import homework_6.task_1.sorts.MultithreadedMergeSort.multithreadedMerge

internal class MergeSortTest {
    companion object {
        @JvmStatic
        fun numbersList(): List<Arguments> = listOf(
            Arguments.of(emptyList<Int>()),
            Arguments.of(listOf(1)),
            Arguments.of(listOf(3, 1, 2)),
            Arguments.of(listOf(-4, 5, 12, 0, 89, 44, 13, 7, 6, 3, 7))
        )

        @JvmStatic
        fun sortedLists(): List<Arguments> = listOf(
            Arguments.of(
                emptyList<Int>(),
                listOf(1, 2, 3, 4)
            ),
            Arguments.of(
                listOf(1, 2, 3, 4),
                emptyList<Int>()
            ),
            Arguments.of(
                listOf(1, 3, 5, 7, 9),
                listOf(2, 4, 6, 8, 10)
            ),
            Arguments.of(
                listOf(-5, -3, -1, 1, 3, 5),
                listOf(-4, -2, 4, 6, 10)
            )
        )
    }

    @MethodSource("sortedLists")
    @ParameterizedTest(name = "mergeTest{index}")
    fun mergeTest(firstList: List<Int>, secondList: List<Int>) {
        val mergedList = firstList.plus(secondList).sorted()
        assertEquals(mergedList, merge(firstList, secondList))
    }

    @MethodSource("sortedLists")
    @ParameterizedTest(name = "multithreadedMergeTest{index}")
    fun multithreadedMergeTest(firstList: List<Int>, secondList: List<Int>) {
        val mergedList = firstList.plus(secondList).sorted()
        assertEquals(mergedList, multithreadedMerge(firstList, secondList))
    }

    @MethodSource("numbersList")
    @ParameterizedTest(name = "mergeSortTest{index}")
    fun mergeSortTest(list: List<Int>) {
        assertEquals(list.sorted(), list.mergeSorted())
    }
}