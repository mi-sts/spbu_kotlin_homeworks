package storages

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class PerformedCommandStorageTest {
    @Test
    fun addActionTest() {
        val storage = PerformedCommandStorage<String>()

        val actions = listOf(StartInsertAction<String>(" "), EndInsertAction<String>("World"),
            StartInsertAction<String>("Hello"), EndInsertAction<String>("!"))

        actions.forEach { storage.apply(it) }

        assertEquals(listOf("Hello", " ", "World", "!"), storage.elements)
    }

    @Test
    fun undoActionTest() {
        val storage = PerformedCommandStorage<String>()

        val actions: List<Action<String>?> = listOf(StartInsertAction<String>(" "), EndInsertAction<String>("World"),
            null, StartInsertAction<String>("Hello"), EndInsertAction<String>("!"), null)

        actions.forEach { if (it != null) storage.apply(it) else storage.undo() }

        assertEquals(listOf("Hello", " "), storage.elements)
    }

    @Test
    fun addActionsTest() {
        val storage = PerformedCommandStorage<String>()

        val actions = arrayOf(StartInsertAction<String>(" "), EndInsertAction<String>("World"),
            StartInsertAction<String>("Hello"), EndInsertAction<String>("!"))

        storage.applyActions(actions)

        assertEquals(listOf("Hello", " ", "World", "!"), storage.elements)
    }
}