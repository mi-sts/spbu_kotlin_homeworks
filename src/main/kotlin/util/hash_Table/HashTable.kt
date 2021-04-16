package util.hash_Table

@Suppress("TooManyFunctions")
class HashTable<K : Any, V>(private var hashFunction: HashFunction<K>) : MutableMap<K, V> {
    companion object { private const val MAX_LOAD_FACTOR = 0.7f }
    private var buckets = Array<HashBucket<K, V>>(1) { HashBucket() }

    private fun expandTable() {
        val resizedBuckets = Array<HashBucket<K, V>>(buckets.count() * 2) { HashBucket() }
        val mapElements = mapElements
        buckets = resizedBuckets
        putAll(mapElements)
    }

    fun changeHashFunction(hashFunction: HashFunction<K>) {
        if (this.hashFunction == hashFunction) return

        this.hashFunction = hashFunction
        val mapElements = mapElements

        clear()
        putAll(mapElements)
    }

    private fun getHashIndex(key: K) = hashFunction.hashOf(key) % buckets.count()

    override fun put(key: K, value: V): V? {
        if (loadFactor >= MAX_LOAD_FACTOR) expandTable()

        val hashIndex = getHashIndex(key)
        val oldElement = buckets[hashIndex].add(HashElement(key, value))

        return oldElement?.value
    }

    override fun putAll(from: Map<out K, V>) = from.forEach { put(it.key, it.value) }

    override fun remove(key: K): V? {
        val hashIndex = getHashIndex(key)
        val removedElement = buckets[hashIndex].remove(key)

        return removedElement?.value
    }

    override fun containsKey(key: K): Boolean = key in keys

    override fun containsValue(value: V): Boolean = value in values

    override fun get(key: K): V? {
        val hashIndex = getHashIndex(key)
        val element = buckets[hashIndex].find(key)

        return element?.value
    }

    override fun isEmpty(): Boolean = size == 0

    override fun clear() { buckets = Array(1) { HashBucket() } }

    fun getHashTableStatistics(): String =
        """Total number of buckets: ${buckets.count()}
          |The number of elements: $size
          |Load factor: $loadFactor
          |The number of conflict buckets: $conflictBucketCount
          |Max conflict bucket size: $maxConflictBucketSize
        """.trimMargin()

    override val size: Int
        get() = buckets.map { it.size }.sum()

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = buckets.flatMap { it.elements }.toMutableSet()

    override val keys: MutableSet<K>
        get() = buckets.flatMap { bucket -> bucket.elements.map { it.key } }.toMutableSet()

    override val values: MutableCollection<V>
        get() = buckets.flatMap { bucket -> bucket.elements.map { it.value } }.toMutableSet()

    val mapElements: Map<K, V>
        get() = keys.zip(values).toMap()

    private val loadFactor: Float
        get() = size.toFloat() / buckets.count()

    private val conflictBucketCount: Int
        get() = buckets.count { it.size > 1 }

    private val maxConflictBucketSize: Int
        get() = buckets.map { it.size }.maxOrNull() ?: 0
}
