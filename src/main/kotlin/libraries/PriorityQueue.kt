package libraries

import java.util.PriorityQueue

class PriorityQueue<E : Any>() {
    class PriorityElement<E : Any>(private val element: E, private val priority: Double) :
        Comparable<PriorityElement<E>> {
        override operator fun compareTo(other: PriorityElement<E>): Int {
            if (this.priority < other.priority) return 1
            if (this.priority > other.priority) return -1
            return 0
        }

        fun getElement(): E = this.element
    }

    private val priorityQueue: PriorityQueue<PriorityElement<E>> = PriorityQueue<PriorityElement<E>>()

    fun enqueue(element: E, priority: Double) = priorityQueue.add(PriorityElement(element, priority))

    fun peek(): E {
        if (priorityQueue.isEmpty()) throw Exception("Priority queue is empty!")
        return priorityQueue.peek().getElement()
    }

    fun remove() {
        if (priorityQueue.isEmpty()) throw Exception("Priority queue is empty!")
        priorityQueue.remove(priorityQueue.peek())
    }

    fun roll(): E {
        val peekEl = peek()
        remove()
        return peekEl
    }
}