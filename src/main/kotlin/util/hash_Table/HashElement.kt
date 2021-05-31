package util.hash_Table

data class HashElement<K, V>(override val key: K, override var value: V) : MutableMap.MutableEntry<K, V> {
    override fun setValue(newValue: V): V = value.also { value = newValue }
}
