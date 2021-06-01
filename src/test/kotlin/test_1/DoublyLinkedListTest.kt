package test_1

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import tornadofx.doubleProperty

internal class DoublyLinkedListTest {
    companion object {
        @JvmStatic
        fun testAddingElements(): List<Arguments> = listOf(
            Arguments.of(listOf(1, 2, 3)),
            Arguments.of(listOf(-3, -2, 1)),
            Arguments.of(emptyList<Int>()),
            Arguments.of(listOf(1, 2, 3, -3, -2, -1))
        )

        @JvmStatic
        fun testAddingElementsWithPosition(): List<Arguments> = listOf(
            Arguments.of(listOf(1, 2, 3), listOf(0, 1, 1)),
            Arguments.of(listOf(-5, -4, -3, -2, -1), listOf(0, 0, 1, 2, 0)),
            Arguments.of(listOf(-9, -8, -7, -6, -5, -4, -3, -2, -1), listOf(0, 1, 2, 0, 2, 1, 3, 2, 1))
        )

        @JvmStatic
        fun testRemovingElements(): List<Arguments> = listOf(
            Arguments.of(listOf(1, 2, 3), listOf(2)),
            Arguments.of(listOf(-3, -2, 1), listOf(0, 1, 0)),
            Arguments.of(listOf(1), listOf(0)),
            Arguments.of(listOf(-5, -4, -3, -2, -1), listOf(4, 3)),
            Arguments.of(listOf(1, 2, 3, -3, -2, -1), listOf(0, 1, 2))
        )

        @JvmStatic
        fun testGettingFromElementsWithIndex(): List<Arguments> = listOf(
            Arguments.of(listOf(1, 2, 3), 1),
            Arguments.of(listOf(-3, -2, 1), 0),
            Arguments.of(listOf(1, 2, 3, -3, -2, -1), 5)
        )

        @JvmStatic
        fun testGettingFromElements(): List<Arguments> = listOf(
            Arguments.of(listOf(1, 2, 3)),
            Arguments.of(listOf(-3, -2, 1)),
            Arguments.of(listOf(1, 2, 3, -3, -2, -1)),
            Arguments.of(listOf(1))
        )
    }

    @Test
    fun isEmpty() {
        val doublyLinkedList: DoublyLinkedList<Int> = DoublyLinkedList()
        assertEquals(true, doublyLinkedList.isEmpty())

        doublyLinkedList.add(1)
        doublyLinkedList.add(2, 0)
        doublyLinkedList.remove(1)
        doublyLinkedList.remove(0)

        assertEquals(true, doublyLinkedList.isEmpty())
    }

    @MethodSource("testAddingElements")
    @ParameterizedTest(name = "addTest{index}, {1}")
    fun addTest(elements: List<Int>) {
        val doublyLinkedList = DoublyLinkedList<Int>()
        elements.forEach { doublyLinkedList.add(it) }
        assertEquals(elements, doublyLinkedList.toList())
    }

    @MethodSource("testAddingElementsWithPosition")
    @ParameterizedTest(name = "addWithPositionTest{index}, {1}")
    fun addWithPositionTest(elements: List<Int>, positions: List<Int>) {
        val doublyLinkedList = DoublyLinkedList<Int>()
        elements.forEachIndexed { index, it -> doublyLinkedList.add(it, positions[index]) }

        val expectedList = mutableListOf<Int>()
        elements.forEachIndexed { index, it -> expectedList.add(positions[index], it) }
        assertEquals(expectedList, doublyLinkedList.toList())
    }

    @MethodSource("testRemovingElements")
    @ParameterizedTest(name = "removeTest{index}, {1}")
    fun removeTest(elements: List<Int>, positions: List<Int>) {
        val doublyLinkedList = DoublyLinkedList<Int>()
        elements.forEach { doublyLinkedList.add(it) }
        positions.forEach { doublyLinkedList.remove(it) }

        val expectedList = elements.toMutableList()
        positions.forEach { expectedList.removeAt(it) }
        assertEquals(expectedList, doublyLinkedList.toList())
    }

    @MethodSource("testGettingFromElements")
    @ParameterizedTest(name = "getTest{index}, {1}")
    fun getTest(elements: List<Int>) {
        val doublyLinkedList = DoublyLinkedList<Int>()
        elements.forEach { doublyLinkedList.add(it) }
        assertEquals(elements[0], doublyLinkedList.get())
    }

    @MethodSource("testGettingFromElementsWithIndex")
    @ParameterizedTest(name = "getWithIndexTest{index}, {1}")
    fun getWithIndexTest(elements: List<Int>, position: Int) {
        val doublyLinkedList = DoublyLinkedList<Int>()
        elements.forEach { doublyLinkedList.add(it) }
        assertEquals(elements[position], doublyLinkedList.get(position))
    }

    @MethodSource("testAddingElements")
    @ParameterizedTest(name = "toListTest{index}, {1}")
    fun toListTest(elements: List<Int>) {
        val doublyLinkedList = DoublyLinkedList<Int>()
        elements.forEach { doublyLinkedList.add(it) }
        assertEquals(elements, doublyLinkedList.toList())
    }
}