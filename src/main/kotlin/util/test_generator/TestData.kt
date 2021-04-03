package util.test_generator

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class TestData(
    @SerialName("package name")
    val packageName: String,
    @SerialName("class name")
    val className: String,
    @SerialName("functions")
    val functionsData: List<FunctionData>
) {
    companion object {
        /**
         * Parses the file of the test data.
         * @param[filePath] the path to the file.
         * @return the TestData object.
         */
        fun parseTestData(filePath: String): TestData {
            val file = File(filePath)
            return Yaml.default.decodeFromString(TestData.serializer(), file.readText())
        }
    }
}

@Serializable
data class FunctionData(val name: String)
