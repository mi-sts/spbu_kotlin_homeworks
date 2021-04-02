package homework_3

import util.test_generator.FunctionData
import util.test_generator.TestData
import util.test_generator.parseTestData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class DataParserTest {

    companion object {
        @JvmStatic
        fun testDataObjects(): List<Arguments> = listOf(
            Arguments.of(
                "storage_test_data.yml",
                TestData("homework_1", "PerformedCommandStorage",
                    listOf(FunctionData("forwardApply"), FunctionData("backwardApply")),
                )
            ),
            Arguments.of(
                "input_test_data.yml",
                TestData("util", "Input",
                    listOf(FunctionData("getString"), FunctionData("getNumber")))
            ),
        )
    }

    @MethodSource("testDataObjects")
    @ParameterizedTest(name = "test{index}, {1}")
    fun parseDataTest(fileName: String, testData: TestData) {
        val filePath = javaClass.getResource("parsing/$fileName/").path
        assertEquals(testData, parseTestData(filePath))
    }
}