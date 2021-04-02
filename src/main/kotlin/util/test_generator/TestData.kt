package util.test_generator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestData(
    @SerialName("package name")
    val packageName: String,
    @SerialName("class name")
    val className: String,
    @SerialName("functions")
    val functionsData: List<FunctionData>
)

@Serializable
data class FunctionData(val name: String)
