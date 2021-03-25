@file:Suppress("MayBeConst")
package homework_1

import utils.Input
import storages.EndInsertAction
import storages.MoveAction
import storages.PerformedCommandStorage
import storages.StartInsertAction

/**
 * Defines the set of user options and symbols used to select them.
 */
enum class UserOption(val value: Char) {
    START_INSERTION('1'), END_INSERTION('2'), MOVEMENT('3'), UNDO('4'),
    PRINT('5'), SAVE_ACTIONS('6'), LOAD_ACTIONS('7'), EXIT('8');

    companion object {
        /**
         * @return the list with symbols used to select options.
         */
        fun getOptionsIndices(): List<Int> = values().map { it.value.toString().toInt() }
    }
}

object UserInterface {
    private val dataFilepath = "src/main/resources/actions_data.json"
    private val optionsRange = UserOption.getOptionsIndices()

    /**
     * Initialize the user interface.
     */
    fun initialize(storage: PerformedCommandStorage) {
        showStartMessage()
        enableInterface(storage)
    }

    /**
     * Enable the user interface.
     * It provides the set of options to interact with storage.
     */
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
                UserOption.SAVE_ACTIONS.value -> storage.save(dataFilepath)
                UserOption.LOAD_ACTIONS.value -> storage.load(dataFilepath)
            }
        }
    }

    /**
     * Receives the symbol from user used to select the option.
     * While given value is incorrect, attempts to enter the symbol will be provided.
     * @return the symbol received from user used to select the option.
     */
    private fun getInput(): Char {
        var input = readLine()?.toIntOrNull() ?: 0
        while (input !in optionsRange) {
            showIncorrectInputMessage()
            input = readLine()?.toIntOrNull() ?: 0
        }

        return input.toString().first()
    }

    private fun showStartMessage() {
        println("Interface for interacting with storage. Enter a number to select an option:")
    }

    /**
     * Shows the message with the list of available options.
     */
    private fun showActionsMessage() {
        println("1 - Insert an element to the beginning.")
        println("2 - Insert an element to the end.")
        println("3 - Move an element to a position.")
        println("4 - Undo the last action.")
        println("5 - Print the storage.")
        println("6 - Save actions.")
        println("7 - Load and apply actions.")
        println("8 - Exit.")
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
