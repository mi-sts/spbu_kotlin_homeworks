package util.hash_Table

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

internal class HashTableTest {
    @Test
    fun putTest() {
        val hashTable = HashTable<String, Int>(SimpleHashFunction())
        val elements = mapOf("one" to 1, "two" to 2, "tree" to 3)
        elements.forEach { hashTable[it.key] = it.value }
        elements.forEach { assertEquals(it.value, hashTable[it.key]) }
    }

    @Test
    fun containsKeyTest() {
        val hashTable = HashTable<String, Int>(SimpleHashFunction())
        val elements = mapOf("one" to 1, "two" to 2, "tree" to 3)
        elements.forEach { hashTable[it.key] = it.value }

        assertEquals(true, hashTable.containsKey("two"))
        assertEquals(true, hashTable.containsKey("one"))
        assertEquals(false, hashTable.containsKey("five"))
    }

    @Test
    fun removeTest() {
        val hashTable = HashTable<String, Int>(SimpleHashFunction())
        val elements = mapOf("one" to 1, "two" to 2, "tree" to 3)
        elements.forEach { hashTable[it.key] = it.value }

        assertEquals(3, hashTable.remove("tree"))
        assertEquals(null, hashTable.remove("four"))
        assertEquals(null, hashTable.remove("three"))
        assertEquals(2, hashTable.remove("two"))
    }

    @Test
    fun containsValueTest() {
        val hashTable = HashTable<String, Int>(SimpleHashFunction())
        val elements = mapOf("one" to 1, "two" to 2, "tree" to 3)
        elements.forEach { hashTable[it.key] = it.value }

        assertEquals(true, hashTable.containsValue(1))
        assertEquals(true, hashTable.containsValue(3))
        assertEquals(false, hashTable.containsValue(5))
    }

    @Test
    fun isEmptyTest() {
        val hashTable = HashTable<String, Int>(SimpleHashFunction())

        assertEquals(true, hashTable.isEmpty())

        val elements = mapOf("one" to 1, "two" to 2, "tree" to 3)
        elements.forEach { hashTable[it.key] = it.value }
        hashTable.clear()

        assertEquals(true, hashTable.isEmpty())
    }

    @Test
    fun getKeysTest() {
        val hashTable = HashTable<String, Int>(SimpleHashFunction())
        val elements = mapOf("one" to 1, "two" to 2, "tree" to 3)
        elements.forEach { hashTable[it.key] = it.value }

        assertEquals(elements.keys.toSet(), hashTable.keys.toSet())
    }

    @Test
    fun getValuesTest() {
        val hashTable = HashTable<String, Int>(SimpleHashFunction())
        val elements = mapOf("one" to 1, "two" to 2, "tree" to 3)
        elements.forEach { hashTable[it.key] = it.value }

        assertEquals(elements.values.toSet(), hashTable.values.toSet())
    }

    @Test
    fun getMapElementsTest() {
        val hashTable = HashTable<String, Int>(SimpleHashFunction())
        val elements = mapOf("one" to 1, "two" to 2, "tree" to 3)
        elements.forEach { hashTable[it.key] = it.value }

        assertEquals(elements.toSortedMap(), hashTable.mapElements.toSortedMap())
    }

    fun sizeTest() {
        val hashTable = HashTable<String, Int>(SimpleHashFunction())
        val elements = mapOf("one" to 1, "two" to 2, "tree" to 3)
        elements.forEach { hashTable[it.key] = it.value }
        assertEquals(3, hashTable.size)

        hashTable.remove("two")
        assertEquals(2, hashTable.size)

        hashTable["four"] = 4
        assertEquals(3, hashTable.size)
    }

    @Test
    fun clearTest() {
        val hashTable = HashTable<String, Int>(SimpleHashFunction())
        val elements = mapOf("one" to 1, "two" to 2, "tree" to 3)
        elements.forEach { hashTable[it.key] = it.value }

        hashTable.clear()

        assertEquals(true, hashTable.isEmpty())
        assertEquals(0, hashTable.size)
    }
}