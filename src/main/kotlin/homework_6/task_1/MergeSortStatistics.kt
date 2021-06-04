package homework_6.task_1

import homework_6.task_1.sorts.MultithreadedMergeSort.multithreadedMergeSorted
import kotlin.random.Random
import kotlin.system.measureNanoTime

object MergeSortStatistics {
    enum class Mode(val modeName: String) {
        ELEMENTS_MODE("Elements"),
        THREADS_MODE("Threads");

        val oppositeMode: Mode
            get() = if (this == ELEMENTS_MODE) THREADS_MODE else ELEMENTS_MODE
    }

    private fun getRandomNumberList(numbersCount: Int) = MutableList(numbersCount) { Random.nextInt() }

    fun getSortStatics(
        constVarMembersCount: Int,
        dependentVarRange: IntRange,
        rangeStep: Int,
        approximationStepsNumber: Int,
        mode: Mode
    ): Map<Int, Double> {
        require(dependentVarRange.first > 0) { "The values from range should be positive!" }
        require(constVarMembersCount > 0) { "The number of elements and threads should be positive!" }

        val generalTimeData: MutableMap<Int, Long> = mutableMapOf()
        repeat(approximationStepsNumber) {
            for (i in dependentVarRange step rangeStep) {
                val elapsedTime = when (mode) {
                    Mode.ELEMENTS_MODE -> {
                        val elements = getRandomNumberList(i)
                        measureNanoTime { elements.multithreadedMergeSorted(constVarMembersCount) }
                    }
                    Mode.THREADS_MODE -> {
                        val elements = getRandomNumberList(constVarMembersCount)
                        measureNanoTime { elements.multithreadedMergeSorted(i) }
                    }
                }
                generalTimeData[i] = generalTimeData.getOrPut(i) { 0L } + elapsedTime
            }
        }

        return generalTimeData.mapValues { it.value / approximationStepsNumber.toDouble() }
    }
}
