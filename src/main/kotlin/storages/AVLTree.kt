package storages

class AVLTree<K : Comparable<K>, V> : MutableMap<K, V> {
    private var treeRoot: AVLNode<K, V>? = null

    override val values: MutableCollection<V>
        get() = treeRoot?.getBranchNodeList()?.map { it.value }?.toMutableList() ?: mutableListOf()

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = treeRoot?.getBranchNodeList()?.toMutableSet() ?: mutableSetOf()

    override val keys: MutableSet<K>
        get() = treeRoot?.getBranchNodeList()?.map { it.key }?.toMutableSet() ?: mutableSetOf()

    override val size: Int
        get() = treeRoot?.size() ?: 0

    override fun containsKey(key: K): Boolean = key in keys

    override fun containsValue(value: V): Boolean = value in values

    override fun get(key: K): V? = treeRoot?.getNode(key)?.value

    override fun isEmpty(): Boolean = size == 0

    override fun clear() { treeRoot = null }

    override fun put(key: K, value: V): V? {
        val oldValue = treeRoot?.getNode(key)?.value

        treeRoot = if (treeRoot == null) AVLNode(key, value) else treeRoot?.insertNode(key, value)

        return oldValue
    }

    override fun putAll(from: Map<out K, V>) = from.forEach { put(it.key, it.value) }

    override fun remove(key: K): V? {
        val oldValue = get(key)
        treeRoot = treeRoot?.removeNode(key)
        return oldValue
    }

    fun getWrittenInDirectOrder(): String = treeRoot?.getWrittenInDirectOrder() ?: ""
}
