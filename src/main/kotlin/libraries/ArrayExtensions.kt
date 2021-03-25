package libraries

/**
 * Array extension to delete duplicates elements.
 * Only the right occurrences remain. They keep the original order.
 */
fun LongArray.deleteDuplicates(): LongArray = this.reversed().distinct().reversed().toLongArray()