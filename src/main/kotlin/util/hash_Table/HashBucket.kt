package util.hash_Table

class HashBucket<K, V> {
    val elements: MutableList<HashElement<K, V>> = mutableListOf()

    fun add(hashElement: HashElement<K, V>): HashElement<K, V>? =
        find(hashElement.key).also { elements.add(hashElement) }

    fun remove(key: K): HashElement<K, V>? = find(key).also { if (it != null) elements.remove(it) }

    fun find(key: K): HashElement<K, V>? = elements.find { it.key == key }

    val size: Int
        get() = elements.size
}
