package homework_5

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import util.Input
import util.hash_Table.HashTable
import util.hash_Table.SimpleStringHashFunction
import util.hash_Table.PolynomialHashFunction
import util.hash_Table.SimpleHashFunction
import java.io.File

interface Action {
    var name: String
    fun apply(hashTable: HashTable<String, String>)
}

private fun getKeyFromUser() = Input.getString("Enter the key: ")

private fun getValueFromUser() = Input.getString("Enter the value: ")

object AddAction : Action {
    override var name = "Add"
    override fun apply(hashTable: HashTable<String, String>) {
        val oldValue = hashTable.put(getKeyFromUser(), getValueFromUser())
        if (oldValue != null) println("Old value: $oldValue")
    }
}

object RemoveAction : Action {
    override var name = "Remove"
    override fun apply(hashTable: HashTable<String, String>) {
        val value = hashTable.remove(getKeyFromUser())
        if (value != null) println("Removed element value: $value")
        else println("There is no such element.")
    }
}

object FindAction : Action {
    override var name = "Find"
    override fun apply(hashTable: HashTable<String, String>) {
        val value = hashTable[getKeyFromUser()]
        if (value != null) println("Found value: $value")
        else println("There is no such element.")
    }
}

object ShowStatisticsAction : Action {
    override var name = "Show statistics"
    override fun apply(hashTable: HashTable<String, String>) = println("${hashTable.getHashTableStatistics()}\n")
}

object LoadFromFileAction : Action {
    private const val DATA_FILE_NAME = "hashtable_data.json"
    override var name = "Load from file"

    private fun loadMapFromFile(filePath: String): Map<String, String> {
        val fileText = File(filePath).readText()

        return Json.decodeFromString(fileText)
    }

    override fun apply(hashTable: HashTable<String, String>) {
        val filePath = javaClass.getResource(DATA_FILE_NAME).path
        val map = loadMapFromFile(filePath)

        hashTable.clear()
        hashTable.putAll(map)
    }
}

object ChangeHashFunctionAction : Action {
    override var name = "Set hash function"
    private val hashFunctions = listOf(SimpleStringHashFunction, PolynomialHashFunction)

    private fun getHashFunctionsInfo(): String = hashFunctions.mapIndexed {
            index, it -> "${it.name} - ${index + 1}"
    }.joinToString()

    private fun getHashFunctionNumber(): Int {
        var choiceNumber = Input.getNumber(
            "Enter the number of needed function:\n(${getHashFunctionsInfo()})\n",
            Input.NumberType.INTEGER, true
        ).toInt()

        while (choiceNumber !in 1..(hashFunctions.lastIndex + 1)) {
            choiceNumber = Input.getNumber(
                "Enter the number in a given range!", Input.NumberType.INTEGER, true
            ).toInt()
        }
        
        return choiceNumber
    }

    override fun apply(hashTable: HashTable<String, String>) {
        hashTable.changeHashFunction(hashFunction = hashFunctions[getHashFunctionNumber()])
    }
}

