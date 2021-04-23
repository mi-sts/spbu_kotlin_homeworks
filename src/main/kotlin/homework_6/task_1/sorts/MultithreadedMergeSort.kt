package homework_6.task_1.sorts

import homework_6.task_1.sorts.MergeSort.merge
import homework_6.task_1.sorts.MergeSort.mergeSorted
import homework_6.task_1.sorts.MultithreadedMergeSort.multithreadedMerge
import kotlin.random.Random
import kotlin.system.measureNanoTime

object MultithreadedMergeSort {
    private class MergeSortThreadRunnable<K : Comparable<K>>(
        private val list: List<K>,
        private val numberOfAvailableThreads: Int
    ) : Runnable {
        private var sortedList: List<K>? = null

        override fun run() {
            val mid = list.count() / 2
            val leftList = list.slice(0 until mid)
            val rightList = list.slice(mid..list.lastIndex)

            if (numberOfAvailableThreads > 1 && list.count() >= 2) {
                val leftAvailableThreads = numberOfAvailableThreads / 2
                val rightAvailableThreads = numberOfAvailableThreads - leftAvailableThreads

                val leftRunnable = MergeSortThreadRunnable(leftList, leftAvailableThreads)
                val rightRunnable = MergeSortThreadRunnable(rightList, rightAvailableThreads)
                val leftThread = Thread(leftRunnable)
                val rightThread = Thread(rightRunnable)
                leftThread.run()
                rightThread.run()
                leftThread.join()
                rightThread.join()

                require(leftRunnable.getSorted() != null && rightRunnable.getSorted() != null) {
                    NullPointerException("The sorted list is null!")
                }
                sortedList = multithreadedMerge(leftRunnable.getSorted(), rightRunnable.getSorted(), 1000)
            } else {
                val leftSortedList = leftList.mergeSorted()
                val rightSortedList = rightList.mergeSorted()
                sortedList = multithreadedMerge(leftSortedList, rightSortedList, 1000)
            }
        }

        fun getSorted() = sortedList
    }

    private class MergeThreadRunnable<K : Comparable<K>>(
        private val leftList: List<K>?,
        private val rightList: List<K>?,
        private val numberOfAvailableThreads: Int
    ) : Runnable {
        private var mergedList: List<K>? = null
        override fun run() {
            require(leftList != null && rightList != null) { "Lists to be merged are null!" }

            val leftAvailableThreads = numberOfAvailableThreads / 2
            val rightAvailableThreads = numberOfAvailableThreads - leftAvailableThreads

            val (maxList, minList) = if (leftList.count() >= rightList.count()) Pair(leftList, rightList) else
                Pair(rightList, leftList)

            if (maxList.count() == 0) mergedList = emptyList()
            else if (numberOfAvailableThreads >= 2) {
                val maxMid = maxList.count() / 2
                val maxMidValue = maxList[maxMid]
                var minMid = minList.binarySearch(maxMidValue)
                if (minMid < 0) minMid = -(minMid + 1)

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
                mergedList = (leftMergedList + maxMidValue + rightMergedList).toList()
            } else mergedList = merge(leftList, rightList)
        }

        fun getMerged() = mergedList
    }

    fun <T : Comparable<T>> List<T>.multithreadedMergeSorted(numberOfThreads: Int): List<T>? {
        val sortRunnable = MergeSortThreadRunnable(this, numberOfThreads)
        val sortThread = Thread(sortRunnable)
        sortThread.run()
        sortThread.join()
        return sortRunnable.getSorted()
    }

    fun <T : Comparable<T>> multithreadedMerge(leftList: List<T>?, rightList: List<T>?, numberOfThreads: Int): List<T>? {
        val mergeRunnable = MergeThreadRunnable(leftList, rightList, numberOfThreads)
        val mergeThread = Thread(mergeRunnable)
        mergeThread.run()
        mergeThread.join()
        return mergeRunnable.getMerged()
    }
}
