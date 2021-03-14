package libraries

fun <T> Array<T>.deleteDuplicates() = this.apply { reverse() }.distinct().apply { reverse() }
