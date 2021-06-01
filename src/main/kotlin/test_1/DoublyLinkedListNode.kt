package test_1

data class DoublyLinkedListNode <T> (
    val value: T,
    var previous: DoublyLinkedListNode<T>?,
    var next: DoublyLinkedListNode<T>?
)
