package libraries

import java.util.PriorityQueue

class PriorityQueue<E : Any> {
    class PriorityElement<E : Any>(private val element: E, private val priority: Double) :
        Comparable<PriorityElement<E>> {
        override operator fun compareTo(other: PriorityElement<E>): Int {
            return priority.compareTo(other.priority)
        }

        fun getElement(): E = this.element
    }

    private val queue: PriorityQueue<PriorityElement<E>> = PriorityQueue<PriorityElement<E>>()

    fun enqueue(element: E, priority: Double) = queue.add(PriorityElement(element, priority))

    fun peek(): E {
        if (queue.isEmpty()) throw ArrayIndexOutOfBoundsException()
        return queue.peek().getElement()
    }

    fun remove() {
        if (queue.isEmpty()) throw ArrayIndexOutOfBoundsException()
        queue.remove(queue.peek())
    }

    fun roll(): E {
        val peekEl = peek()
        remove()
        return peekEl
    }
}
