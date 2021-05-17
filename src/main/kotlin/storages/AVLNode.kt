@file:Suppress("TooManyFunctions")

package storages

import kotlin.math.max

class AVLNode <K : Comparable<K>, V> (
    override val key: K,
    initValue: V,
) : MutableMap.MutableEntry<K, V> {
    companion object { const val BALANCE_COEFFICIENT = 2 }

    private var leftChild: AVLNode<K, V>? = null
    private var rightChild: AVLNode<K, V>? = null
    override var value: V = initValue
        private set

    private var height = 1

    fun size(): Int = getBranchNodeList().count()

    private fun rightHeight() = rightChild?.height ?: 0

    private fun leftHeight() = leftChild?.height ?: 0

    private fun balanceFactor() = rightHeight() - leftHeight()

    private fun updateHeight() { height = max(leftHeight(), rightHeight()) + 1 }

    private fun isLeaf() = leftChild == null && rightChild == null

    private fun hasOneChild() = (leftChild == null && rightChild != null) || (leftChild != null && rightChild == null)

    private fun leftRotation(): AVLNode<K, V> {
        val pivot = rightChild!!

        rightChild = pivot.leftChild
        pivot.leftChild = this

        this.updateHeight()
        pivot.updateHeight()

        return pivot
    }

    private fun rightRotation(): AVLNode<K, V> {
        val pivot = leftChild!!

        leftChild = pivot.rightChild
        pivot.rightChild = this

        this.updateHeight()
        pivot.updateHeight()

        return pivot
    }

    private fun bigRightRotation(): AVLNode<K, V> {
        rightChild ?: return this
        rightChild = rightChild!!.rightRotation()

        return leftRotation()
    }

    private fun bigLeftRotation(): AVLNode<K, V> {
        leftChild ?: return this
        leftChild = leftChild!!.leftRotation()

        return rightRotation()
    }

    private fun balance(): AVLNode<K, V> {
        return when (balanceFactor()) {
            BALANCE_COEFFICIENT -> {
                if (rightChild?.balanceFactor() == -1) {
                    bigRightRotation()
                } else {
                    leftRotation()
                }
            }
            -BALANCE_COEFFICIENT -> {
                if (leftChild?.balanceFactor() == 1) {
                    bigLeftRotation()
                } else {
                    rightRotation()
                }
            }
            else -> this
        }
    }

    fun insertNode(key: K, value: V): AVLNode<K, V> {
       when {
            key < this.key -> leftChild = leftChild?.insertNode(key, value) ?: AVLNode(key, value)
            key > this.key -> rightChild = rightChild?.insertNode(key, value) ?: AVLNode(key, value)
            else -> this.apply { this.value = value }
        }

        updateHeight()
        return balance()
    }

    private fun getMinNodeFromBranch(): AVLNode<K, V> {
        var currentVertex: AVLNode<K, V> = this
        while (currentVertex.leftChild != null) currentVertex = currentVertex.leftChild!!

        return currentVertex
    }

    fun removeNode(key: K): AVLNode<K, V>? = when {
        key < this.key -> {
            leftChild = leftChild?.removeNode(key)

            updateHeight()
            this.balance()
        }
        key > this.key -> {
            rightChild = rightChild?.removeNode(key)

            updateHeight()
            this.balance()
        }
        else -> when {
            isLeaf() -> null
            hasOneChild() -> leftChild ?: rightChild
            else -> {
                val swapNode = rightChild!!.getMinNodeFromBranch()
                rightChild!!.removeNode(swapNode.key)

                if (swapNode != rightChild) swapNode.rightChild = rightChild
                swapNode.leftChild = leftChild
                swapNode.updateHeight()
                swapNode.balance()
            }
        }
    }

    fun getNode(key: K): AVLNode<K, V>? = when {
            key < this.key -> leftChild?.getNode(key)
            key > this.key -> rightChild?.getNode(key)
            else -> this
    }

    fun getBranchNodeList(): List<AVLNode<K, V>> = when {
        isLeaf() -> listOf(this)
        hasOneChild() -> if (leftChild != null) listOf(this).plus(leftChild!!.getBranchNodeList())
            else listOf(this).plus(rightChild!!.getBranchNodeList())
        else -> leftChild!!.getBranchNodeList().plus(this).plus(rightChild!!.getBranchNodeList())
    }

    fun getWrittenInDirectOrder(): String {
        val nodeElement = "($key -> $value) "
        return when {
            isLeaf() -> nodeElement
            hasOneChild() ->
                if (leftChild != null) "$nodeElement${leftChild!!.getWrittenInDirectOrder()}"
                else "$nodeElement${rightChild!!.getWrittenInDirectOrder()}"
            else -> "$nodeElement${leftChild!!.getWrittenInDirectOrder()}${rightChild!!.getWrittenInDirectOrder()}"
        }
    }

    override fun setValue(newValue: V): V = value.also { value = newValue }
}
