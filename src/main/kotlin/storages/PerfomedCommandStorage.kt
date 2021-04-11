package storages

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import java.io.File
import java.io.FileNotFoundException

/**
 * Defines the storage action.
 */
interface Action<T> {
    /**
     * Applies some action to the storage.
     * @param[storage] the storage for which the action is applied.
     */
    fun apply(storage: MutableList<T>)
    /**
     * Undo the current action that was applied to the storage.
     * @param[storage] the storage for which the action is undo.
     */
    fun undo(storage: MutableList<T>)
}

/**
 * Defines the action for adding the element to the starting position of the storage.
 * @property[element] the element that is being added.
 */
@Serializable
@SerialName("startInsertion")
class StartInsertAction<T>(private val element: T) : Action<T> {
    override fun apply(storage: MutableList<T>) = storage.add(0, element)

    override fun undo(storage: MutableList<T>) { storage.removeFirst() }

    override fun equals(other: Any?): Boolean = this.javaClass == other?.javaClass &&
            this.element == (other as StartInsertAction<*>).element

    override fun hashCode(): Int = super.hashCode()
}

/**
 * Defines the action for adding the element to the end position of the storage.
 * @property[element] the element that is being added.
 */
@Serializable
@SerialName("endInsertion")
class EndInsertAction<T>(private val element: T) : Action<T> {
    override fun apply(storage: MutableList<T>) { storage.add(element) }

    override fun undo(storage: MutableList<T>) { storage.removeLast() }

    override fun equals(other: Any?): Boolean = this.javaClass == other?.javaClass &&
            this.element == (other as EndInsertAction<*>).element

    override fun hashCode(): Int = super.hashCode()
}

/**
 * Defines the action to move the element from one position to another in the storage.
 * @property[fromIndex] the index from which the element is moved.
 * @property[toIndex] the index where the element is moved to.
 */
@Serializable
@SerialName("moving")
class MoveAction<T>(private val fromIndex: Int, private val toIndex: Int) : Action<T> {
    /**
     * Checks the existence of indexes of the storage.
     * @param[storage] the storage for which the existence of indexes is checked.
     * @throws[IndexOutOfBoundsException] if index of storage does not exist.
     */
    private fun checkIndexBounds(storage: MutableList<T>) {
        if (fromIndex !in storage.indices || toIndex !in storage.indices) {
            throw IndexOutOfBoundsException("Index does not exist!")
        }
    }

    /**
     * Moves the element from one position to another in the storage.
     * @param[fromIndex] the index from which the element is moved.
     * @param[toIndex] the index where the element is moved to.
     * @param[storage] the storage where the elements are moved.
     */
    private fun move(fromIndex: Int, toIndex: Int, storage: MutableList<T>) {
        if (toIndex >= fromIndex) {
            storage.add(toIndex + 1, storage[fromIndex])
            storage.removeAt(fromIndex)
        } else {
            storage.add(toIndex, storage[fromIndex])
            storage.removeAt(fromIndex + 1)
        }
    }

    override fun apply(storage: MutableList<T>) {
        checkIndexBounds(storage)

        move(fromIndex, toIndex, storage)
    }

    override fun undo(storage: MutableList<T>) {
        checkIndexBounds(storage)

        move(toIndex, fromIndex, storage)
    }

    override fun equals(other: Any?): Boolean = this.javaClass == other?.javaClass &&
            this.fromIndex == (other as MoveAction<*>).fromIndex && this.toIndex == other.toIndex

    override fun hashCode(): Int = super.hashCode()
}

/**
 * Defines the storage with some operations.
 * Among them are:
 * -adding an element to the staring position.
 * -adding an element to the end position.
 * -moving an element from one position to another.
 * -undo the last applied action.
 */
class PerformedCommandStorage<T> {
    companion object StorageSerialization {
        object IntAsObjectSerializer : KSerializer<Int> {
            @Serializable
            @SerialName("Int")
            data class IntSurrogate(val value: Int)

            override val descriptor: SerialDescriptor = IntSurrogate.serializer().descriptor

            override fun serialize(encoder: Encoder, value: Int) =
                IntSurrogate.serializer().serialize(encoder, IntSurrogate(value))

            override fun deserialize(decoder: Decoder) =
                decoder.decodeSerializableValue(IntSurrogate.serializer()).value
        }

        private val module = SerializersModule {
            polymorphic(Any::class) {
                subclass(IntAsObjectSerializer)
            }
            polymorphic(Action::class) {
                subclass(StartInsertAction.serializer(PolymorphicSerializer(Any::class)))
                subclass(EndInsertAction.serializer(PolymorphicSerializer(Any::class)))
                subclass(MoveAction.serializer(PolymorphicSerializer(Any::class)))
            }
        }

        private val format = Json { serializersModule = module }

        /**
         * Saves the actions to a file.
         * @param[filePath] the path to the file.
         */
        fun PerformedCommandStorage<Int>.save(filePath: String) {
            val actionsString = format.encodeToString(actions.toList())
            File(filePath).apply { createNewFile() }.writeText(actionsString)
        }

        private fun showFileNotExistMessage() = println("The saved file was not detected!")

        /**
         * Loads the actions from file and applies them to storage.
         * @param[filePath] the path to the file.
         */
        fun PerformedCommandStorage<Int>.load(filePath: String): Array<Action<Int>>? {
            return try {
                val fileText = File(filePath).readText()
                if (fileText.isEmpty()) return null

                format.decodeFromString(fileText)
            } catch (e: FileNotFoundException) {
                showFileNotExistMessage()
                null
            }
        }
    }

    private var actions: MutableList<Action<T>> = mutableListOf()
    var elements: MutableList<T> = mutableListOf()
        private set

    /**
     * Applies some action.
     * @param[action] the action to apply.
     */
    fun apply(action: Action<T>) {
        actions.add(action)
        action.apply(elements)
    }

    /**
     * Applies actions from list.
     * @param[actions] the list of actions to apply.
     */
    fun applyActions(actions: Array<Action<T>>) = actions.map { this.apply(it) }

    /**
     * Undo last action.
     */
    fun undo() {
        if (actions.size == 0) {
            println("There are no actions to undo.")
            return
        }

        actions.last().undo(elements)
        actions.removeLast()
    }

    fun print() = println(elements.toString())
}
