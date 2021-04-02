package libraries.test_generator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestData (
    @SerialName("package name")
    val package_name: String,
    @SerialName("class name")
    val class_name: String,
    @SerialName("functions")
    val functionsData: List<FunctionData>
)

@Serializable
data class FunctionData(val name: String)
