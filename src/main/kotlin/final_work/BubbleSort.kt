package final_work

import java.lang.ArithmeticException

private fun <T> arithmeticExceptionMessage(values: Iterable<T>) {
    println("Arithmetic error. Given iterable values can be wrong.")
    println(values)
}

/**
 * Sorts the iterable object with the bubble sort.
 * @param [comparator] the given comparator which compares the elements.
 * @return the sorted array with elements from the iterable object.
 */
fun <T> Iterable<T>.bubbleSort(comparator: Comparator<T>): List<T> {
    try {
        val sortedList = this.toMutableList()

        var swap = true
        while (swap) {
            swap = false
            for (i in 0 until sortedList.lastIndex) {
                if (comparator.compare(sortedList[i], sortedList[i + 1]) > 0) {
                    sortedList[i] = sortedList[i + 1].also { sortedList[i + 1] = sortedList[i] }
                    swap = true
                }
            }
        }
        return sortedList
    } catch (e: ArithmeticException) {
        arithmeticExceptionMessage(this)
        return emptyList()
    }
}
