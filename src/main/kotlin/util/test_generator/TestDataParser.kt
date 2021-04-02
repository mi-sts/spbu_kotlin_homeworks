package util.test_generator

import com.charleskorn.kaml.Yaml
import java.io.File

/**
 * Parse the file of the test data.
 * @param[filePath] the path to the file.
 * @return the TestData object.
 */
fun parseTestData(filePath: String): TestData {
    val file = File(filePath)
    return Yaml.default.decodeFromString(TestData.serializer(), file.readText())
}
