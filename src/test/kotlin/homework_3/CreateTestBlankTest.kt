package homework_3

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import util.test_generator.TestGenerator.generateTestBlank
import java.nio.file.Path

internal class CreateTestBlankTest {

    companion object {
        @JvmStatic
        fun testBlankData(): List<Arguments> = listOf(
            Arguments.of(
                "storage_test_data.yml", "test_1", "homework_1", "PerformedCommandStorageTest"
            ),
            Arguments.of(
                "input_test_data.yml", "test_2", "util", "InputTest"
            )
        )
    }

    @MethodSource("testBlankData")
    @ParameterizedTest(name = "generateTestBlank - {3}")
    fun generateTestBlankTest(dataFileName: String, testFolderName: String, packageName: String,
                              generatedFileName: String, @TempDir tempDirectory: Path)  {
        val testData = javaClass.getResource("generation/$testFolderName/$dataFileName")
        val expectedFile = javaClass.getResource("generation/$testFolderName/${generatedFileName}_expected.kt")

        generateTestBlank(testData.path, tempDirectory.toString(), generatedFileName)
        
        val tempFile = tempDirectory.resolve("$packageName/$generatedFileName.kt")

        assertEquals(expectedFile.readText().replace("\r\n", "\n"), tempFile.toFile().readText())
    }
}