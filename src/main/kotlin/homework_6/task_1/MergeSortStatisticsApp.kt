@file:Suppress("MagicNumber")
package homework_6.task_1

import homework_6.task_1.MergeSortStatistics.Mode
import homework_6.task_1.MergeSortStatistics.getSortStatics
import javafx.geometry.Orientation
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.control.RadioButton
import javafx.scene.control.Slider
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
    companion object {
        private const val ELEMENTS_SLIDER_MAX_VALUE = 100000
        private const val THREADS_SLIDER_MAX_VALUE = 1000
        private const val APPROXIMATION_STEPS_NUMBER = 3
    }

    private lateinit var statisticsChart: LineChart<Number, Number>
    private lateinit var statisticsSeries: XYChart.Series<Number, Number>

    private lateinit var constantVariableLabel: Label
    private lateinit var constantVariableTextField: TextField

    private lateinit var dependentVariableLabel: Label
    private lateinit var dependentVariableSlider: Slider

    private val modeToggleGroup = ToggleGroup()

    private lateinit var elementsModeRadioButton: RadioButton
    private lateinit var threadsModeRadioButton: RadioButton

    private var currentMode = Mode.ELEMENTS_MODE

    private var timeLabel = "Nanoseconds"

    private val constantVariableValue: Int
        get() = constantVariableTextField.text?.toIntOrNull() ?: 1

    private val dependentVariableValue: Int
        get() = dependentVariableSlider.value.toInt()

    private fun clearStatisticsChart() = statisticsSeries.data?.clear()

    private fun changeChartValues(newValues: Map<Int, Double>) =
        newValues.forEach { statisticsSeries.data(it.key, it.value) }

    private fun updateLabelsText() {
        dependentVariableLabel.text = currentMode.modeName
        constantVariableLabel.text = currentMode.oppositeMode.modeName
    }

    private fun updateChart() {
        clearStatisticsChart()
        val dependentRange = 1..dependentVariableValue
        val numberOfSteps = max(dependentRange.count() / 100, 1)

        statisticsSeries.chart?.yAxis?.label = timeLabel
        statisticsSeries.chart?.xAxis?.label = currentMode.modeName

        val statisticsMap =
            getSortStatics(constantVariableValue, dependentRange, numberOfSteps, APPROXIMATION_STEPS_NUMBER,
                currentMode)

        changeChartValues(statisticsMap)
    }

    private fun updateSliderRange() {
        dependentVariableSlider.min = 1.0

        when (currentMode) {
            Mode.ELEMENTS_MODE -> {
                dependentVariableSlider.max = ELEMENTS_SLIDER_MAX_VALUE.toDouble()
                dependentVariableSlider.majorTickUnit = ELEMENTS_SLIDER_MAX_VALUE / 1000.0
            }
            Mode.THREADS_MODE -> {
                dependentVariableSlider.max = THREADS_SLIDER_MAX_VALUE.toDouble()
            }
        }
    }

    private fun onModeChanged() {
        updateLabelsText()
        updateSliderRange()
        updateChart()
    }

    private fun onModeButtonPressed(modeOfButton: Mode) {
        if (currentMode == modeOfButton) return

        currentMode = modeOfButton
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
                threadsModeRadioButton = radiobutton("Threads mode", modeToggleGroup) {
                    action { onModeButtonPressed(Mode.THREADS_MODE) }
                }
                elementsModeRadioButton = radiobutton("Elements mode", modeToggleGroup) {
                    action { onModeButtonPressed(Mode.ELEMENTS_MODE) }
                }
                when (currentMode) {
                    Mode.ELEMENTS_MODE -> modeToggleGroup.selectToggle(elementsModeRadioButton)
                    Mode.THREADS_MODE -> modeToggleGroup.selectToggle(threadsModeRadioButton)
                }
                onModeButtonPressed(currentMode)
            }
            updateLabelsText()
        }
    }
}

fun main(args: Array<String>) {
    launch<MergeSortStatisticsApp>(args)
}
