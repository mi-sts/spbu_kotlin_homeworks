package storages

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import storages.PerformedCommandStorage.StorageSerialization.load
import storages.PerformedCommandStorage.StorageSerialization.save
import util.test_generator.TestGenerator
import java.io.File
import java.nio.file.Path
import kotlin.io.path.readText

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

    @Test
    fun serializationTest(@TempDir tempDirectory: Path)  {
        val storage = PerformedCommandStorage<Int>()

        val actions: List<Action<Int>?> = listOf(StartInsertAction<Int>(1), StartInsertAction<Int>(2),
            EndInsertAction<Int>(3), EndInsertAction<Int>(4), EndInsertAction<Int>(5), null,
            EndInsertAction<Int>(6), EndInsertAction<Int>(7), null)

        actions.forEach { if (it != null) storage.apply(it) else storage.undo() }

        val tempFileName = "test_actions_data_actual.json"
        val tempFile = File("$tempDirectory$tempFileName")

        val expectedFileName = "test_actions_data.json"

        storage.save(tempFile.path)

        val expectedFile = javaClass.getResource(expectedFileName)

        assertEquals(expectedFile.readText(), tempFile.readText())
    }


    @Test
    fun deserializationTest() {
        val storage = PerformedCommandStorage<Int>()

        val expectedActions: List<Action<Int>?> = listOf(StartInsertAction<Int>(1), StartInsertAction<Int>(2),
            EndInsertAction<Int>(3), EndInsertAction<Int>(4), EndInsertAction<Int>(6))

        val fileName = "test_actions_data.json"

        val actualActions = storage.load(javaClass.getResource(fileName).path)!!.toList()

        assertEquals(expectedActions, actualActions)
    }
}