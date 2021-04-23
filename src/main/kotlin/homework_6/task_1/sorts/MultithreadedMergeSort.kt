package homework_6.task_1.sorts

import homework_6.task_1.sorts.MergeSort.merge
import homework_6.task_1.sorts.MergeSort.mergeSorted

object MultithreadedMergeSort {
    private class MergeSortThreadRunnable<K : Comparable<K>>(
        private val list: MutableList<K>,
        private val numberOfAvailableThreads: Int
    ) : Runnable {
        companion object { private const val MERGE_THREADS_COEFFICIENT = 0.1 }
        private var sortedList: MutableList<K>? = null

        override fun run() {
            val availableThreads = numberOfAvailableThreads - 1
            val mid = list.count() / 2
            val leftList = list.subList(0, mid)
            val rightList = list.subList(mid, list.count())

            if (availableThreads > 1 && list.count() >= 2) {
                val leftAvailableThreads = availableThreads / 2
                val rightAvailableThreads = availableThreads - leftAvailableThreads

                val leftRunnable = MergeSortThreadRunnable(leftList, leftAvailableThreads)
                val rightRunnable = MergeSortThreadRunnable(rightList, rightAvailableThreads)
                val leftThread = Thread(leftRunnable)
                val rightThread = Thread(rightRunnable)
                leftThread.run()
                rightThread.run()
                leftThread.join()
                rightThread.join()

                require(leftRunnable.getSorted() != null && rightRunnable.getSorted() != null) {
                    "The sorted list is null!"
                }
                sortedList = multithreadedMerge(leftRunnable.getSorted(), rightRunnable.getSorted(),
                    (numberOfAvailableThreads * MERGE_THREADS_COEFFICIENT).toInt())
            } else {
                val leftSortedList = leftList.mergeSorted()
                val rightSortedList = rightList.mergeSorted()
                sortedList = multithreadedMerge(leftSortedList, rightSortedList,
                    (numberOfAvailableThreads * MERGE_THREADS_COEFFICIENT).toInt())
            }
        }

        fun getSorted() = sortedList
    }

    private class MergeThreadRunnable<K : Comparable<K>>(
        private val leftList: MutableList<K>?,
        private val rightList: MutableList<K>?,
        private val numberOfAvailableThreads: Int
    ) : Runnable {
        private var mergedList: MutableList<K>? = null

        private fun <T : Comparable<T>> MutableList<T>.findMidIndex(midValue: T) =
            binarySearch(midValue).let { if (it < 0) it.inv() else it }

        override fun run() {
            val availableThreads = numberOfAvailableThreads - 1
            require(leftList != null && rightList != null) { "Lists to be merged are null!" }

            val leftAvailableThreads = availableThreads / 2
            val rightAvailableThreads = availableThreads - leftAvailableThreads

            val maxList = if (leftList.count() >= rightList.count()) leftList else rightList
            val minList = if (leftList.count() >= rightList.count()) rightList else leftList

            mergedList = mutableListOf()
            when {
                maxList.isNotEmpty() && numberOfAvailableThreads >= 2 -> {
                    val maxMid = maxList.count() / 2
                    val maxMidValue = maxList[maxMid]
                    val minMid = minList.findMidIndex(maxMidValue)

                    val maxListLeft = maxList.subList(0, maxMid)
                    val maxListRight = maxList.subList(maxMid + 1, maxList.count())

                    val minListLeft = minList.subList(0, minMid)
                    val minListRight = minList.subList(minMid, minList.count())

                    val leftRunnable = MergeThreadRunnable(minListLeft, maxListLeft, leftAvailableThreads)
                    val rightRunnable = MergeThreadRunnable(minListRight, maxListRight, rightAvailableThreads)
                    val leftThread = Thread(leftRunnable)
                    val rightThread = Thread(rightRunnable)
                    leftThread.run()
                    rightThread.run()
                    leftThread.join()
                    rightThread.join()

                    val leftMergedList = leftRunnable.getMerged()
                    val rightMergedList = rightRunnable.getMerged()

                    require(leftMergedList != null && rightMergedList != null) { "The merged list is null!" }
                    mergedList?.addAll(leftMergedList)
                    mergedList?.add(maxMidValue)
                    mergedList?.addAll(rightMergedList)
                }
                maxList.isNotEmpty() && numberOfAvailableThreads < 2 -> mergedList = merge(leftList, rightList)
            }
        }

        fun getMerged() = mergedList
    }

    fun <T : Comparable<T>> MutableList<T>.multithreadedMergeSorted(numberOfThreads: Int): MutableList<T>? {
        val sortRunnable = MergeSortThreadRunnable(this, numberOfThreads)
        val sortThread = Thread(sortRunnable)
        sortThread.run()
        sortThread.join()
        return sortRunnable.getSorted()
    }

    fun <T : Comparable<T>> multithreadedMerge(leftList: MutableList<T>?, rightList: MutableList<T>?,
                                               numberOfThreads: Int): MutableList<T>? {
        val mergeRunnable = MergeThreadRunnable(leftList, rightList, numberOfThreads)
        val mergeThread = Thread(mergeRunnable)
        mergeThread.run()
        mergeThread.join()
        return mergeRunnable.getMerged()
    }
}
