package homework_5

import homework_1.UserOption
import util.hash_Table.HashTable
import util.hash_Table.SimpleStringHashFunction

object UserInterface {
    enum class AvailableActions(val value: Char) {
        ADD('1'), REMOVE('2'), FIND('3'), SHOW_STATISTICS('4'), LOAD('5'),
        CHANGE_HASH_FUNCTION('6'), EXIT('7');

        companion object {
            val actionsIndices: List<Int>
                get() = UserOption.values().map { it.value.toString().toInt() }
        }
    }

    private fun getActionChoice(): Char {
        var input = readLine()?.toIntOrNull() ?: 0
        while (input !in AvailableActions.actionsIndices) {
            println("The input must be a number between 1 and 7!")
            input = readLine()?.toIntOrNull() ?: 0
        }

        return input.toString().first()
    }

    private fun showStartMessage() {
        println("Interface for interacting with hashtable. Enter a number to select an option:")
    }

    private fun showActionsMessage() {
        println("""1 - Add a map element to the hashtable.
                  |2 - Remove a map element from the hashtable.
                  |3 - Find a map element in the hashtable.
                  |4 - Show hashtable statistics.
                  |5 - Load elements from file.
                  |6 - Change the hash function.
                  |7 - Exit.
        """.trimMargin())
    }

    fun initialize(hashTable: HashTable<String, String>) {
        showStartMessage()
        enableInterface(hashTable)
    }

    private fun enableInterface(hashTable: HashTable<String, String>) {
        var input = ' '

        while (input != AvailableActions.EXIT.value) {
            showActionsMessage()
            input = getActionChoice()

            when (input) {
                AvailableActions.ADD.value -> AddAction.apply(hashTable)
                AvailableActions.REMOVE.value -> RemoveAction.apply(hashTable)
                AvailableActions.FIND.value -> FindAction.apply(hashTable)
                AvailableActions.SHOW_STATISTICS.value -> ShowStatisticsAction.apply(hashTable)
                AvailableActions.LOAD.value -> LoadFromFileAction.apply(hashTable)
                AvailableActions.CHANGE_HASH_FUNCTION.value -> ChangeHashFunctionAction.apply(hashTable)
            }
        }
    }
}

fun main() {
    val hashTable = HashTable<String, String>(SimpleStringHashFunction)
    UserInterface.initialize(hashTable)
}