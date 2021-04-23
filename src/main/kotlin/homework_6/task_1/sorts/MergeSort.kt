package homework_6.task_1.sorts

object MergeSort {
    fun <T : Comparable<T>> merge(leftList: MutableList<T>?, rightList: MutableList<T>?): MutableList<T> {
        if (leftList == null || rightList == null) return mutableListOf()

        val mergedList: MutableList<T> = mutableListOf()

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

        if (leftCounter < leftList.count()) mergedList.addAll(leftList.subList(leftCounter, leftList.count()))
        if (rightCounter < rightList.count()) mergedList.addAll(rightList.subList(rightCounter, rightList.count()))

        return mergedList
    }

    fun <T : Comparable<T>> MutableList<T>.mergeSorted(): MutableList<T> {
        return if (count() < 2) this else {
            val mid = count() / 2
            val leftList = subList(0, mid)
            val rightList = subList(mid, count())

            merge(leftList.mergeSorted(), rightList.mergeSorted())
        }
    }
}
