package storages

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class AVLTreeTest {

    companion object {
        @JvmStatic
        fun testAddingElements(): List<Arguments> = listOf(
            Arguments.of(
                mapOf(1 to "a")
            ),
            Arguments.of(
                mapOf(1 to "a", 2 to "b", 3 to "c"),
            )
        )

        @JvmStatic
        fun testAddingAndRemovingElements(): List<Arguments> = listOf(
            Arguments.of(
                emptyMap<Int, String>(),
                emptyList<Int>()
            ),
            Arguments.of(
                emptyMap<Int, String>(),
                listOf(1, 2, 3)
            ),
            Arguments.of(
                mapOf(1 to "a"),
                emptyList<Int>()
            ),
            Arguments.of(
                mapOf(1 to "a"),
                listOf(1)
            ),
            Arguments.of(
                mapOf(2 to "a", 1 to "b", 3 to "c", 4 to "d"),
                listOf(1, 2, 3, 4)
            ),
            Arguments.of(
                mapOf(5 to "a", 6 to "b", 2 to "c", 10 to "d", 11 to "e", 12 to "f", 14 to "g", 15 to "h"),
                listOf(6, 12, 10, 11, 1)
            )
        )

        @JvmStatic
        fun testPrintingElementsInDirectOrder(): List<Arguments> = listOf(
            Arguments.of(
                "(1 -> a) ",
                mapOf(1 to "a")),
            Arguments.of(
                "(2 -> b) (1 -> a) (3 -> c) ",
                mapOf(1 to "a", 2 to "b", 3 to "c")),
            Arguments.of(
                "(2 -> a) (1 -> b) (4 -> c) (3 -> d) (5 -> e) ",
                mapOf(1 to "b", 2 to "a", 3 to "d", 4 to "c", 5 to "e")
            )
        )

        @JvmStatic
        fun testAddingElementsFromMap(): List<Arguments> = listOf(
            Arguments.of(
                emptyMap<Int, String>(),
                mapOf(1 to "a", 2 to "b", 3 to "c")
            ),
            Arguments.of(
                mapOf(1 to "a", 2 to "b", 3 to "c"),
                mapOf(4 to "d", 5 to "e")
            )
        )
    }

    private fun <K : Comparable<K>, V> getConvertedTreeEntriesToMap(tree: AVLTree<K, V>): Map<K, V> {
        val treeElements: MutableMap<K, V> = mutableMapOf()
        tree.entries.forEach { treeElements[it.key] = it.value }

        return treeElements
    }

    @MethodSource("testAddingElements")
    @ParameterizedTest
    fun putTest(elements: Map<Int, String>) {
        val tree = AVLTree<Int, String>()

        elements.forEach { tree[it.key] = it.value }

        assertEquals(elements, getConvertedTreeEntriesToMap(tree))
    }

    private fun <K : Comparable<K>, V> getTreeAfterAdding(elements: Map<K, V>): AVLTree<K, V> {
        val tree = AVLTree<K, V>()

        elements.forEach { tree[it.key] = it.value }

        return tree
    }

    private fun <K : Comparable<K>, V> getTreeAfterAddingAndRemoving(elements: Map<K, V>,
                                                                     removingList: List<K>): AVLTree<K, V> {
        val tree = getTreeAfterAdding(elements)

        removingList.forEach { tree.remove(it) }

        return tree
    }

    @MethodSource("testAddingAndRemovingElements")
    @ParameterizedTest(name = "putAndRemoveTest - {index}")
    fun putAndRemoveTest(elements: Map<Int, String>, removingList: List<Int>) {
        val tree = getTreeAfterAddingAndRemoving(elements, removingList)

        val remainingElements = elements.filter { it.key !in removingList }

        assertEquals(remainingElements, getConvertedTreeEntriesToMap(tree))
    }

    @MethodSource("testAddingElements")
    @ParameterizedTest(name = "getKeysTest - {index}")
    fun getKeysTest(elements: Map<Int, String>) {
        val tree = getTreeAfterAdding(elements)

        assertEquals(elements.keys, tree.keys)
    }

    @MethodSource("testAddingElements")
    @ParameterizedTest(name = "getValuesTest - {index}")
    fun getValuesTest(elements: Map<Int, String>) {
        val tree = getTreeAfterAdding(elements)

        assertEquals(elements.values.toSet(), tree.values.toSet())
    }

    @MethodSource("testPrintingElementsInDirectOrder")
    @ParameterizedTest(name = "getTreeWrittenInDirectOrderTest - {index}")
    fun getTreeWrittenInDirectOrderTest(expectedString: String, elements: Map<Int, String>) {
        val tree = getTreeAfterAdding(elements)

        assertEquals(expectedString, tree.getWrittenInDirectOrder())
    }

    @MethodSource("testAddingAndRemovingElements")
    @ParameterizedTest(name = "getSizeTest - {index}")
    fun getSizeTest(elements: Map<Int, String>, removingList: List<Int>) {
        val tree = getTreeAfterAddingAndRemoving(elements, removingList)

        val expectedSize = elements.count() - removingList.filter { it in elements.keys }.count()

        assertEquals(expectedSize, tree.size)
    }

    @Test
    fun containsKeyTest() {
        val tree = AVLTree<Int, String>()

        tree[1] = "a"
        tree[2] = "b"

        val checkingKeys = listOf(1, 2, 3, 4)

        assertEquals(listOf(true, true, false, false), checkingKeys.map { tree.containsKey(it) })
    }

    @Test
    fun containsValueTest() {
        val tree = AVLTree<Int, String>()

        tree[1] = "a"
        tree[2] = "b"

        val checkingValues = listOf("a", "b", "c", "d")

        assertEquals(listOf(true, true, false, false), checkingValues.map { tree.containsValue(it) })
    }

    @MethodSource("testAddingAndRemovingElements")
    @ParameterizedTest(name = "getTest - {index}")
    fun getTest(elements: Map<Int, String>, removingList: List<Int>) {
        val tree = getTreeAfterAddingAndRemoving(elements, removingList)

        val remainingElements = elements.filter { it.key !in removingList }

        assertEquals(remainingElements.values.toList(), remainingElements.keys.map { tree[it] })
    }

    @MethodSource("testAddingAndRemovingElements")
    @ParameterizedTest(name = "removeTest - {index}")
    fun removeTest(elements: Map<Int, String>, removingList: List<Int>) {
        val tree = getTreeAfterAdding(elements)

        val removedElements = removingList.map { if (it in elements.keys) elements[it] else null }

        assertEquals(removedElements, removingList.map { tree.remove(it)} )
    }

    @MethodSource("testAddingAndRemovingElements")
    @ParameterizedTest(name = "isEmptyTest - {index}")
    fun isEmptyTest(elements: Map<Int, String>, removingList: List<Int>) {
        val tree = getTreeAfterAddingAndRemoving(elements, removingList)

        var isEmpty = true
        elements.keys.forEach { if (it !in removingList) isEmpty = false}

        assertEquals(isEmpty, tree.isEmpty() )
    }

    @MethodSource("testAddingElements")
    @ParameterizedTest(name = "clearTest - {index}")
    fun clearTest(elements: Map<Int, String>) {
        val tree = getTreeAfterAdding(elements)

        tree.clear()

        assertEquals(true, tree.isEmpty())
    }

    @MethodSource("testAddingElementsFromMap")
    @ParameterizedTest(name = "putAllTest - {index}")
    fun putAllTest(addedBeforeElements: Map<Int, String>, mapElements: Map<Int, String>) {
        val tree = getTreeAfterAdding(addedBeforeElements)

        tree.putAll(mapElements)

        assertEquals(addedBeforeElements.plus(mapElements), getConvertedTreeEntriesToMap(tree))
    }
}
