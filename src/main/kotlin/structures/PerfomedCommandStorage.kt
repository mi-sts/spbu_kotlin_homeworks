package structures

enum class ActionType {
    StartInsertion, EndInsertion, Movement
}

open class Action(actionType: ActionType) {
    val actionType: ActionType = actionType
}

class InsertAction(actionType: ActionType) : Action(actionType)

class MoveAction(actionType: ActionType, fromIndex: Int, toIndex: Int) : Action(actionType) {
    val fromIndex = fromIndex
    val toIndex = toIndex
}

class PerformedCommandStorage {
    private var actions: MutableList<Action> = mutableListOf()
    private var storage: MutableList<Int> = mutableListOf()

    fun insertFirst(number: Int) {
        storage.add(0, number)
        actions.add(InsertAction(ActionType.StartInsertion))
    }

    fun insertLast(number: Int) {
        storage.add(number)
        actions.add(InsertAction(ActionType.EndInsertion))
    }

    fun move(fromIndex: Int, toIndex: Int) {
        if (fromIndex !in 0..storage.count() || toIndex !in 0..storage.count()) {
            throw IndexOutOfBoundsException("Index does not exist!")
        }

        if (toIndex >= fromIndex) {
            storage.add(toIndex + 1, storage[fromIndex])
            storage.removeAt(fromIndex)
        } else {
            storage.add(toIndex, storage[fromIndex])
            storage.removeAt(fromIndex + 1)
        }

        actions.add(MoveAction(ActionType.Movement, fromIndex, toIndex))
    }

    fun undo() {
        when (actions.last().actionType) {
            ActionType.StartInsertion -> storage.removeFirst()
            ActionType.EndInsertion -> storage.removeLast()
            ActionType.Movement -> {
                val action = actions.last()
                if (action is MoveAction) {
                    storage.add(action.fromIndex, storage[action.toIndex])
                }
            }
        }
        actions.removeLast()
    }

    fun print() {
        storage.forEach { print("$it ") }
        println()
    }
}
