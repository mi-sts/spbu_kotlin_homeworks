package homework_1

import libraries.Input
import storages.EndInsertAction
import storages.MoveAction
import storages.PerformedCommandStorage
import storages.StartInsertAction

enum class UserOption(val value: Char) {
    START_INSERTION('1'), END_INSERTION('2'), MOVEMENT('3'),
    UNDO('4'), PRINT('5'), EXIT('6');

    companion object {
        fun getOptionsIndices(): List<Int> = values().map { it.value.toString().toInt() }
    }
}

object UserInterface {
    private val OPTIONS_RANGE = UserOption.getOptionsIndices()

    fun initialize(storage: PerformedCommandStorage) {
        showStartMessage()
        enableInterface(storage)
    }

    private fun enableInterface(storage: PerformedCommandStorage) {
        var input = ' '

        while (input != UserOption.EXIT.value) {
            showActionsMessage()
            input = getInput()

            when (input) {
                UserOption.START_INSERTION.value -> {
                    showElementInputMessage()
                    val number = Input.getNumber("", Input.NumberType.INTEGER, true).toInt()
                    storage.apply(StartInsertAction(number))
                }
                UserOption.END_INSERTION.value -> {
                    showElementInputMessage()
                    val number = Input.getNumber("", Input.NumberType.INTEGER, true).toInt()
                    storage.apply(EndInsertAction(number))
                }
                UserOption.MOVEMENT.value -> {
                    showIndexesInputMessage()
                    val fromIndex = Input.getNumber("", Input.NumberType.POSITIVE, true).toInt()
                    val toIndex = Input.getNumber("", Input.NumberType.POSITIVE, true).toInt()
                    storage.apply(MoveAction(fromIndex, toIndex))
                }
                UserOption.UNDO.value -> storage.undo()
                UserOption.PRINT.value -> storage.print()
            }
        }
    }

    private fun getInput(): Char {
        var input = readLine()?.toIntOrNull() ?: 0
        while (input !in OPTIONS_RANGE) {
            showIncorrectInputMessage()
            input = readLine()?.toIntOrNull() ?: 0
        }

        return input.toString().first()
    }

    private fun showStartMessage() {
        println("Interface for interacting with storage. Enter a number to select a option:")
    }

    private fun showActionsMessage() {
        println("1 - Insert an element to the beginning.")
        println("2 - Insert an element to the end.")
        println("3 - Move an element to a position.")
        println("4 - Undo the last action.")
        println("5 - Print the storage.")
        println("6 - Exit.")
    }

    private fun showIncorrectInputMessage() = println("The input must be a number between 1 and 6!")

    private fun showElementInputMessage() = println("Enter an element: ")

    private fun showIndexesInputMessage() {
        println("Enter an index of the moving element and then index where it will be placed:")
    }
}

fun main() {
    val storage = PerformedCommandStorage()
    UserInterface.initialize(storage)
}
