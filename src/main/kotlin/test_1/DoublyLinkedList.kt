package test_1

import java.lang.NullPointerException

class DoublyLinkedList <T> {
    private var listRoot: DoublyLinkedListNode<T>? = null
    private var firstNode: DoublyLinkedListNode<T>? = null
    private var lastNode: DoublyLinkedListNode<T>? = null
    
    fun isEmpty() = listRoot == null

    private fun getNode(position: Int): DoublyLinkedListNode<T>? {
        if (isEmpty()) return null
        require(position >= 0) { "The position can not be less than zero!" }

        var currentNode: DoublyLinkedListNode<T>? = firstNode
        var currentPosition = 0
        while (currentPosition < position && currentNode?.next != null) {
            currentNode = currentNode.next
            ++currentPosition
        }

        require(currentPosition == position) { IndexOutOfBoundsException("The position does not exist!") }

        return currentNode
    }

    fun add(data: T) {
        if (isEmpty()) {
            listRoot = DoublyLinkedListNode(data, null, null)
            firstNode = listRoot
            lastNode = listRoot
        }
        else {
            val newLastNode = DoublyLinkedListNode(data, lastNode, null)
            lastNode?.next = newLastNode
            lastNode = newLastNode
        }
    }

    fun add(data: T, position: Int) {
        if (isEmpty() && position == 0) add(data)
        else {
            if (position == 0) {
                val newLastNode = DoublyLinkedListNode(data, null, firstNode)
                firstNode?.previous = newLastNode
                firstNode = newLastNode
            } else {
                val addedPreviousNode = getNode(position - 1)
                val addedNextNode = addedPreviousNode?.next
                val addedNode = DoublyLinkedListNode(data, addedPreviousNode, addedNextNode)

                if (addedPreviousNode == lastNode) lastNode = addedNode

                addedPreviousNode?.next = addedNode
                addedNextNode?.previous = addedNode
            }
        }
    }

    fun remove(position: Int) {
        require(!isEmpty()) { IndexOutOfBoundsException("Can not remove the element, the list is empty!") }
        require(position >= 0) { "The position can not be less than zero!" }

        if (position == 0) {
            if (firstNode == listRoot) listRoot = firstNode?.next
            firstNode = firstNode?.next
        } else {
            val removedPreviousNode = getNode(position - 1)
            val removedNode = removedPreviousNode?.next
            if (removedNode == listRoot) listRoot = removedPreviousNode
            val removedNextNode = removedNode?.next
            removedPreviousNode?.next = removedNextNode
            removedNextNode?.previous = removedPreviousNode
        }
    }

    fun get(): T {
        require(!isEmpty()) { "Can not get the first element, the list is empty!" }
        val firstElement = firstNode?.value
        firstElement ?: throw NullPointerException("The first element is null!")

        return firstElement
    }

    fun get(position: Int) = getNode(position)?.value

    fun toList(): List<T> {
        var currentElement = firstNode
        val values = mutableListOf<T>()
        while (currentElement != null) {
            values.add(currentElement.value)
            currentElement = currentElement.next
        }

        return values
    }
}

fun main() {
    val elements = listOf(1, 2, 3)
    val list: DoublyLinkedList<Int> = DoublyLinkedList()
    val positions = listOf(0, 1, 0)

    elements.forEachIndexed { index, it -> list.add(it, positions[index]) }

    print(list.toList())
}
