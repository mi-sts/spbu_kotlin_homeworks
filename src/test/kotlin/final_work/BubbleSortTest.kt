package final_work

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class BubbleSortTest {
    companion object {
        @JvmStatic
        fun intShuffledLists(): List<Arguments> = listOf(
            Arguments.of(listOf(9, 7, 2, 3, 4, 6, 5, 1, 8)),
            Arguments.of(listOf(75, 12, 36, 255, 12, -78, -41, 3, 11, 12)),
            Arguments.of(listOf(1, 1000000, -4564564, 123245, 454984465, 1235132))
        )

        @JvmStatic
        fun doubleShuffledLists(): List<Arguments> = listOf(
            Arguments.of(listOf(78.12, -45.23, 12.02, 0.12, 45.78, 93.122, -78.123, 789.12, 89.106)),
            Arguments.of(listOf(-0.146, 45.987, 12344.12, 255.45786, 12.856, -123.45, -789.123, 3.101, 11.7893, 797.123)),
            Arguments.of(listOf(-123.1238, 7893.1256456, -789321.125, 1245945.12348, 78.45233, 5132.123))
        )

        @JvmStatic
        fun stringLists(): List<Arguments> = listOf(
            Arguments.of(listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")),
            Arguments.of(listOf("Klimovo", "Bryansk", "Saint-Petersburg", "Petergof"))
        )
    }

    @MethodSource("intShuffledLists")
    @ParameterizedTest(name = "intBubbleSortTest{index}, {1}")
    fun intBubbleSortTest(numbersList: List<Int>) {
        assertEquals(numbersList.sorted(), numbersList.bubbleSort(Comparator.naturalOrder()))
    }

    @MethodSource("doubleShuffledLists")
    @ParameterizedTest(name = "doubleBubbleSortTest{index}, {1}")
    fun doubleBubbleSortTest(numbersList: List<Double>) {
        assertEquals(numbersList.sorted(), numbersList.bubbleSort(Comparator.naturalOrder()))
    }

    @Test
    fun iterableTypesBubbleSortTest() {
        val shuffledNumberList = listOf(4, 1, 3, -4, 2, -456, 123, -47, 4, 332)
        assertEquals(shuffledNumberList.sorted(), shuffledNumberList.bubbleSort(Comparator.naturalOrder()))

        val numberSet = setOf(123, 45, -45, 789, -12, 10, 79, 1, 23, 45, 68, -9, 1, 3)
        assertEquals(numberSet.sorted(), numberSet.bubbleSort(Comparator.naturalOrder()))
    }

    @MethodSource("stringLists")
    @ParameterizedTest(name = "stringBubbleSortTest{index}, {1}")
    fun stringBubbleSortTest(stringList: List<String>) {
        assertEquals(stringList.sortedBy { it.length }, stringList.bubbleSort(compareBy { it.length }))
    }
}