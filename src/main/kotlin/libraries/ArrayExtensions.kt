package libraries

/**
 * Array extension to delete duplicates elements.
 * Only the right occurrences remain. They keep the original order.
 */
fun <T> Array<T>.deleteDuplicates() = this.apply { reverse() }.distinct().apply { reverse() }
