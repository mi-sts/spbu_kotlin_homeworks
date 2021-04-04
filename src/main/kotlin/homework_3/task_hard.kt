package homework_3

import util.Input
import util.test_generator.TestGenerator.generateTestBlank

fun main() {
    val dataFilePath = Input.getString("Enter the path of the data file:")
    val savingFileDirPath = Input.getString(
        "Enter the path to the folder to save the generated file:"
    )
    val savingFileName = Input.getString("Enter the name of generated file:")

    generateTestBlank(dataFilePath, savingFileDirPath, savingFileName)
}
