package test_1

import libraries.Input
import libraries.PriorityQueue

fun addElement(priorityQueue: PriorityQueue<String>) {
    println("Enter the string element and the priority for him:")
    val str = Input.getString()
    val priority: Double = readLine()?.toDoubleOrNull() ?: 0.0
    priorityQueue.enqueue(str, priority)
}

fun getPeekElement(priorityQueue: PriorityQueue<String>) {
    println("Get the queue peek element:")
    println(priorityQueue.peek())
}

fun removeElement() {
    println("Remove the queue peek element")
}

fun rollElement(priorityQueue: PriorityQueue<String>) {
    println("Roll the queue peek element:")
    println(priorityQueue.roll())
}

fun showPriorityQueueOptions(priorityQueue: PriorityQueue<String>) {
    addElement(priorityQueue)
    addElement(priorityQueue)
    getPeekElement(priorityQueue)
    removeElement()
    getPeekElement(priorityQueue)
    addElement(priorityQueue)
    addElement(priorityQueue)
    rollElement(priorityQueue)
    rollElement(priorityQueue)
}

fun main() {
    val priorityQueue: PriorityQueue<String> = PriorityQueue<String>()
    showPriorityQueueOptions(priorityQueue)
}