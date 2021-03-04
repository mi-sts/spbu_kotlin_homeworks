package storages

interface Action {
    fun apply(storage: MutableList<Int>)
    fun undo(storage: MutableList<Int>)
}

class StartInsertAction(private val number: Int) : Action {
    override fun apply(storage: MutableList<Int>) = storage.add(0, number)

    override fun undo(storage: MutableList<Int>) { storage.removeFirst() }
}

class EndInsertAction(private val number: Int) : Action {
    override fun apply(storage: MutableList<Int>) { storage.add(number) }

    override fun undo(storage: MutableList<Int>) { storage.removeLast() }
}

class MoveAction(val fromIndex: Int, val toIndex: Int) : Action {
    private fun checkIndexBounds(storage: MutableList<Int>) {
        if (fromIndex !in 0..storage.count() || toIndex !in 0..storage.count()) {
            throw IndexOutOfBoundsException("Index does not exist!")
        }
    }

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

class PerformedCommandStorage {
    private var actions: MutableList<Action> = mutableListOf()
    private var storage: MutableList<Int> = mutableListOf()

    fun apply(action: Action) {
        actions.add(action)
        action.apply(storage)
    }

    fun undo() {
        if (actions.size == 0) {
            println("There are no actions to undo.")
            return
        }

        actions.last().undo(storage)
        actions.removeLast()
    }

    fun print() = println(storage.toString())
}
