@file:Suppress("MagicNumber")
package homework_6.task_1

import homework_6.getSortStaticsDependingOnElementsNumber
import homework_6.getSortStaticsDependingOnThreads
import javafx.geometry.Orientation
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import javafx.scene.control.Slider
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import tornadofx.App
import tornadofx.View
import tornadofx.data
import tornadofx.vbox
import tornadofx.hbox
import tornadofx.linechart
import tornadofx.series
import tornadofx.button
import tornadofx.action
import tornadofx.label
import tornadofx.textfield
import tornadofx.slider
import tornadofx.radiobutton
import tornadofx.launch

import kotlin.math.max

class MergeSortStatisticsApp : App(StatisticsChartView::class)

class StatisticsChartView : View("Merge Sort Statistics") {
    private val elementsSliderMaxValue = 100000
    private val threadsSliderMaxValue = 1000

    private var statisticsChart: LineChart<Number, Number>? = null
    private var statisticsSeries: XYChart.Series<Number, Number>? = null

    private var isElementsMode = true
    private val modeToggleGroup = ToggleGroup()
    private var isModeChosen = false

    private var elementsLabel = "Elements"
    private var threadsLabel = "Threads"

    private var constantVariableLabel: Label? = null
    private var constantVariableTextField: TextField? = null

    private var dependentVariableLabel: Label? = null
    private var dependentVariableSlider: Slider? = null

    private fun clearStatisticsChart() = statisticsSeries?.data?.clear()

    private val constantVariableValue: Int
        get() = constantVariableTextField?.text?.toIntOrNull() ?: 1

    private val dependentVariableValue: Int
        get() = dependentVariableSlider?.value?.toInt() ?: 1

    private fun changeChartValues(newValues: Map<Int, Double>) =
        newValues.forEach { statisticsSeries?.data(it.key, it.value) }

    private fun updateLabelsText() {
        if (isElementsMode) {
            dependentVariableLabel?.text = elementsLabel
            constantVariableLabel?.text = threadsLabel
        } else {
            dependentVariableLabel?.text = threadsLabel
            constantVariableLabel?.text = elementsLabel
        }
    }

    private fun updateChart() {
        clearStatisticsChart()
        val dependentRange = 1..dependentVariableValue
        val approximationStepsNumber = 3
        val numberOfSteps = max(dependentRange.count() / 100, 1)

        val statisticsMap =
            if (!isElementsMode) {
                statisticsSeries?.chart?.xAxis?.label = "Threads"
                statisticsSeries?.chart?.yAxis?.label = "Nanoseconds"
                getSortStaticsDependingOnThreads(
                    constantVariableValue, dependentRange, numberOfSteps, approximationStepsNumber)
            } else {
                statisticsSeries?.chart?.xAxis?.label = "Elements"
                statisticsSeries?.chart?.yAxis?.label = "Nanoseconds"
                getSortStaticsDependingOnElementsNumber(
                    constantVariableValue, dependentRange, numberOfSteps, approximationStepsNumber)
            }

        changeChartValues(statisticsMap)
    }

    private fun updateSliderRange() {
        if (isElementsMode) {
            dependentVariableSlider?.min = 1.0
            dependentVariableSlider?.max = elementsSliderMaxValue.toDouble()
            dependentVariableSlider?.majorTickUnit = elementsSliderMaxValue / 1000.0
        } else {
            dependentVariableSlider?.min = 1.0
            dependentVariableSlider?.max = threadsSliderMaxValue.toDouble()
        }
    }

    private fun onModeChanged() {
        updateLabelsText()
        updateSliderRange()
        updateChart()
    }

    private fun onModeButtonPressed(isElementsModeButton: Boolean) {
        if (isModeChosen && isElementsMode == isElementsModeButton) return

        isModeChosen = true
        isElementsMode = isElementsModeButton
        onModeChanged()
    }

    override val root = vbox {
        statisticsChart = linechart("Merge sort", NumberAxis(), NumberAxis()) {
            statisticsSeries = series("Multithreaded sort")
        }
        hbox {
            vbox {
                hbox {
                    vbox {
                        constantVariableLabel = label()
                        constantVariableTextField = textfield()
                    }
                    vbox {
                        dependentVariableLabel = label()
                        dependentVariableSlider = slider {
                            Orientation.HORIZONTAL
                            isShowTickLabels = true
                            isShowTickMarks = true
                        }
                    }
                }
                button("Show statistics") { action { updateChart() } }
            }
            hbox {
                updateSliderRange()
                radiobutton("Threads mode", modeToggleGroup) {
                    action { onModeButtonPressed(false) }
                }
                radiobutton("Elements mode", modeToggleGroup) {
                    action { onModeButtonPressed(true) }
                }
            }
            updateLabelsText()
        }
    }
}

fun main(args: Array<String>) {
    launch<MergeSortStatisticsApp>(args)
}
