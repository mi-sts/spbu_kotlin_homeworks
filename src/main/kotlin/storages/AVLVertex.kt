package storages

import kotlin.math.max

class AVLVertex<K, V> (
    override val key: K,
    override var value: V,
    var height: Int,
    var leftVertex: AVLVertex<K, V>?,
    var rightVertex: AVLVertex<K, V>?
) : MutableMap.MutableEntry<K, V> {
    fun getBalanceFactor() = (rightVertex?.height ?: 0) - (leftVertex?.height ?: 0)

    fun updateVertexHeight() {
        val leftVertexHeight = leftVertex?.height ?: 0
        val rightVertexHeight = rightVertex?.height ?: 0

        height = max(leftVertexHeight, rightVertexHeight) + 1
    }

    fun isLeaf() = leftVertex == null && rightVertex == null

    fun hasOneChild() = isLeaf() && (leftVertex != null || rightVertex != null)

    fun setChild(child: AVLVertex<K, V>?, direction: AVLTree.Direction) {
        if (direction == AVLTree.Direction.LEFT) leftVertex = child
        else if (direction == AVLTree.Direction.RIGHT) rightVertex = child
    }

    fun getChild(direction: AVLTree.Direction): AVLVertex<K, V>? = when (direction) {
        AVLTree.Direction.LEFT -> leftVertex
        AVLTree.Direction.RIGHT -> rightVertex
        else -> null
    }

    override fun setValue(newValue: V): V = value.also { value = newValue }
}
