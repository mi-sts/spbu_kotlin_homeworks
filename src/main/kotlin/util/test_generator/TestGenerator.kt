package util.test_generator

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import org.junit.jupiter.api.Test
import util.test_generator.TestData.Companion.parseTestData
import java.io.File

/**
 * Describes methods for test generating.
 */
object TestGenerator {
    /**
     * Creates test functions for the test.
     * @param[functionData] the data describing functions.
     * @return the FunSpec object used to add the function during the generation.
     */
    private fun createTestFunction(functionData: FunctionData): FunSpec {
        return FunSpec.builder(functionData.name)
            .addAnnotation(Test::class)
            .build()
    }

    /**
     * Generates the FileSpec object used to create the test file.
     * @param[testData] the data describing the test.
     * @param[packageName] the name of the file package.
     * @param[fileName] the name of the generating file.
     * @return the FileSpec object used to save the test to the file.
     */
    fun generateTest(testData: TestData, packageName: String, fileName: String): FileSpec {
        val testFunctionsList = testData.functionsData.map { createTestFunction(it) }

        val testClass = TypeSpec.classBuilder(testData.className)
            .addModifiers(KModifier.INTERNAL)
            .addFunctions(testFunctionsList)
            .build()

        return FileSpec.builder(packageName, fileName)
            .addType(testClass).build()
    }

    /**
     * Saves the test to the file from FileSpec object.
     * @param[fileSpec] the FileSpec object describing the file.
     * @param[directoryPath] the path of the saving file.
     */
    fun saveTestFile(fileSpec: FileSpec, directoryPath: String) = fileSpec.writeTo(File(directoryPath))

    /**
     * Generates the test from yaml file and saves it.
     * @param[generationParameters] the parameters of the generating file.
     */
    fun generateTestBlank(dataFilePath: String, directoryPath: String, generatedFileName: String) {
        val testData = parseTestData(dataFilePath)
        val testFileSpec = generateTest(testData, testData.packageName, generatedFileName)
        saveTestFile(testFileSpec, directoryPath)
    }
}
