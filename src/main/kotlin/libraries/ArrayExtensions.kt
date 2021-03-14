package libraries

/**
 * Array extension to delete duplicates elements.
 * Only the right occurrences remain. They keep the original order.
 */
inline fun <reified T> Array<T>.deleteDuplicates(): Array<T> = this.reversed().distinct().reversed().toTypedArray()

