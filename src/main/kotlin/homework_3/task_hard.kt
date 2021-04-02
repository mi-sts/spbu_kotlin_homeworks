package homework_3

import util.test_generator.GenerationParameters
import util.test_generator.generateTestBlank

import util.Input

fun main() {
    val dataFilePath = Input.getString("Enter the path of the data file:")
    val savingFileDirPath = Input.getString(
        "Enter the path to the folder to save the generated file:"
    )
    val savingFileName = Input.getString("Enter the name of generated file:")
    
    val config = GenerationParameters(dataFilePath, savingFileDirPath, savingFileName)

    generateTestBlank(config)
}
