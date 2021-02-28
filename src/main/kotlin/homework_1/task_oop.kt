package homework_1

import libraries.Input

enum class ActionType {
    StartInsertion, EndInsertion, Movement
}

abstract class Action(actionType: ActionType) {
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
        if (fromIndex < 0 || toIndex < 0 || fromIndex > storage.count() || toIndex > storage.count())
            throw Exception("Index does not exist!")

        if (toIndex >= fromIndex) {
            storage.add(toIndex + 1, storage[fromIndex])
            storage.removeAt(fromIndex)
        }
        else {
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

class UserInterface private constructor() {
    companion object {
        fun initialize() {
            showStartMessage()
            enableInterface()
        }

        fun enableInterface() {
            var input = 0
            var number: Int
            var fromIndex: Int
            var toIndex: Int

            while (input != 6) {
                showActionsMessage()
                input = getInput()

                when (input) {
                    1 -> {
                        showElementInputMessage()
                        number = Input.getNumber(false)
                        StorageManager.insertFirst(number)
                    }
                    2 -> {
                        showElementInputMessage()
                        number = Input.getNumber(false)
                        StorageManager.insertLast(number)
                    }
                    3 -> {
                        showIndexesInputMessage()
                        fromIndex = Input.getNonNegativeNumber(false)
                        toIndex = Input.getNonNegativeNumber(false)
                        StorageManager.insert(fromIndex, toIndex)
                    }
                    4 -> StorageManager.undo()
                    5 -> StorageManager.print()
                }
            }
        }

        fun getInput(): Int {
            var input: Int = readLine()?.toIntOrNull() ?: 0
            while (input <= 0 || input > 6) {
                showIncorrectInputMessage()
                input = readLine()?.toInt() ?: 0
            }

            return input
        }

        fun showStartMessage() {
            println("Interface for interacting with storage. Enter a number to select a option:")
        }

        fun showActionsMessage() {
            println("1 - Insert an element to the beginning.")
            println("2 - Insert an element to the end.")
            println("3 - Move an element to a position.")
            println("4 - Undo the last action.")
            println("5 - Print the storage.")
            println("6 - Exit.")
        }

        fun showIncorrectInputMessage() {
            println("The input must be a number between 1 and 6!")
        }

        fun showElementInputMessage() {
            println("Enter an element: ")
        }

        fun showIndexesInputMessage() {
            println("Enter an index of the moving element and then index where it will be placed:")
        }
    }
}

class StorageManager {
    companion object {
        var storage: PerformedCommandStorage? = null

        fun insertFirst(number: Int) {
            storage?.insertFirst(number) ?: showNullStorageMessage()
        }

        fun insertLast(number: Int) {
            storage?.insertLast(number) ?: showNullStorageMessage()
        }

        fun insert(fromIndex: Int, toIndex: Int) {
            storage?.move(fromIndex, toIndex) ?: showNullStorageMessage()
        }

        fun undo() {
            storage?.undo() ?: showNullStorageMessage()
        }

        fun print() {
            storage?.print() ?: showNullStorageMessage()
        }

        fun showNullStorageMessage() {
            println("An instance of the storage does not exist!")
        }
    }
}
fun main() {
    val storage = PerformedCommandStorage()
    StorageManager.storage = storage
    UserInterface.initialize()
}
