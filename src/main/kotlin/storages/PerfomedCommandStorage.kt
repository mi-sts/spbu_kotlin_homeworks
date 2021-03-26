package storages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.io.File
import java.io.FileNotFoundException

/**
 * Defines the storage action.
 */
interface Action {
    /**
     * Applies some action to the storage.
     * @param[storage] the storage for which the action is applied.
     */
    fun apply(storage: MutableList<Int>)
    /**
     * Undo the current action that was applied to the storage.
     * @param[storage] the storage for which the action is undo.
     */
    fun undo(storage: MutableList<Int>)
}

/**
 * Defines the action for adding the number to the starting position of the storage.
 * @property[number] the number that is being added.
 */
@Serializable
@SerialName("startInsertion")
class StartInsertAction(private val number: Int) : Action {
    override fun apply(storage: MutableList<Int>) = storage.add(0, number)

    override fun undo(storage: MutableList<Int>) { storage.removeFirst() }
}

/**
 * Defines the action for adding the number to the end position of the storage.
 * @property[number] the number that is being added.
 */
@Serializable
@SerialName("endInsertion")
class EndInsertAction(private val number: Int) : Action {
    override fun apply(storage: MutableList<Int>) { storage.add(number) }

    override fun undo(storage: MutableList<Int>) { storage.removeLast() }
}

/**
 * Defines the action to move the number from one position to another in the storage.
 * @property[fromIndex] the index from which the number is moved.
 * @property[toIndex] the index where the number is moved to.
 */
@Serializable
@SerialName("moving")
class MoveAction(private val fromIndex: Int, private val toIndex: Int) : Action {
    /**
     * Checks the existence of indexes of the storage.
     * @param[storage] the storage for which the existence of indexes is checked.
     * @throws[IndexOutOfBoundsException] if index of storage does not exist.
     */
    private fun checkIndexBounds(storage: MutableList<Int>) {
        if (fromIndex !in storage.indices || toIndex !in storage.indices) {
            throw IndexOutOfBoundsException("Index does not exist!")
        }
    }

    /**
     * Moves the number from one position to another in the storage.
     * @param[fromIndex] the index from which the number is moved.
     * @param[toIndex] the index where the number is moved to.
     * @param[storage] the storage where the numbers are moved.
     */
    private fun move(fromIndex: Int, toIndex: Int, storage: MutableList<Int>) {
        if (toIndex >= fromIndex) {
            storage.add(toIndex + 1, storage[fromIndex])
            storage.removeAt(fromIndex)
        } else {
            storage.add(toIndex, storage[fromIndex])
            storage.removeAt(fromIndex + 1)
        }
    }

    override fun apply(storage: MutableList<Int>) {
        checkIndexBounds(storage)

        move(fromIndex, toIndex, storage)
    }

    override fun undo(storage: MutableList<Int>) {
        checkIndexBounds(storage)

        move(toIndex, fromIndex, storage)
    }
}

/**
 * Defines the storage with some operations.
 * Among them are:
 * -adding a number to the staring position.
 * -adding a number to the end position.
 * -moving a number from one position to another.
 * -undo the last applied action.
 */
class PerformedCommandStorage {
    private val module = SerializersModule {
        polymorphic(Action::class) {
            subclass(StartInsertAction::class)
            subclass(EndInsertAction::class)
            subclass(MoveAction::class)
        }
    }

    private val format = Json { serializersModule = module }

    private var actions: MutableList<Action> = mutableListOf()
    private var storage: MutableList<Int> = mutableListOf()

    /**
     * Applies some action.
     * @param[action] the action to apply.
     */
    fun apply(action: Action) {
        actions.add(action)
        action.apply(storage)
    }

    /**
     * Undo last action.
     */
    fun undo() {
        if (actions.size == 0) {
            println("There are no actions to undo.")
            return
        }

        actions.last().undo(storage)
        actions.removeLast()
    }

    fun print() = println(storage.toString())

    /**
     * Saves the actions to a file.
     * @param[filePath] the path to the file.
     */
    fun save(filePath: String) {
        val actionsString = format.encodeToString(actions.toList())
        File(filePath).writeText(actionsString)
    }

    private fun showFileNotExistMessage() = println("The saved file was not detected!")

    /**
     * Loads the actions from file and applies them to storage.
     * @param[filePath] the path to the file.
     */
    fun load(filePath: String) {
        try {
            val fileText = File(filePath).readText()
            if (fileText.isEmpty()) return

            val loadedActionsList: Array<Action> = format.decodeFromString(fileText)
            actions.addAll(loadedActionsList.map { it.apply { apply(storage) } })
        } catch (e: FileNotFoundException) {
            showFileNotExistMessage()
        }
    }
}
