package util.sorts

object MergeSort {
    fun <T : Comparable<T>> merge(leftList: List<T>?, rightList: List<T>?): List<T> {
        if (leftList == null || rightList == null) return emptyList()

        var mergedList: MutableList<T> = mutableListOf()

        var leftCounter = 0
        var rightCounter = 0
        while (leftCounter < leftList.count() && rightCounter < rightList.count()) {
            if (leftList[leftCounter] < rightList[rightCounter]) {
                mergedList.add(leftList[leftCounter])
                ++leftCounter
            } else {
                mergedList.add(rightList[rightCounter])
                ++rightCounter
            }
        }

        if (leftCounter < leftList.count()) mergedList = mergedList
            .plus(leftList.subList(leftCounter, leftList.count())).toMutableList()
        if (rightCounter < rightList.count()) mergedList = mergedList
            .plus(rightList.subList(rightCounter, rightList.count())).toMutableList()

        return mergedList
    }

    fun <T : Comparable<T>> List<T>.mergeSorted(): List<T> {
        return if (count() < 2) this else {
            val mid = count() / 2
            val leftList = slice(0 until mid)
            val rightList = slice(mid..lastIndex)

            merge(leftList.mergeSorted(), rightList.mergeSorted())
        }
    }
}
