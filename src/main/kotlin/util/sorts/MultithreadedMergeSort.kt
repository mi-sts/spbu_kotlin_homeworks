package util.sorts

import util.sorts.MergeSort.merge
import util.sorts.MergeSort.mergeSorted

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

                if (leftRunnable.getSorted() == null || rightRunnable.getSorted() == null) throw
                NullPointerException("The sorted list is null!")
                sortedList = merge(leftRunnable.getSorted(), rightRunnable.getSorted())
            } else {
                val leftSortedList = leftList.mergeSorted()
                val rightSortedList = rightList.mergeSorted()
                sortedList = merge(leftSortedList, rightSortedList)
            }
        }

        fun getSorted() = sortedList
    }

    fun <T : Comparable<T>> List<T>.multithreadedMergeSorted(numberOfThreads: Int): List<T>? {
        val mainRunnable = MergeSortThreadRunnable(this, numberOfThreads)
        val mainThread = Thread(mainRunnable)
        mainThread.run()
        mainThread.join()
        return mainRunnable.getSorted()
    }
}
