@file:Suppress("TooManyFunctions")

package storages

import kotlin.math.max

data class AVLNode <K : Comparable<K>, V> (
    override var key: K,
    override var value: V,
    var leftChild: AVLNode<K, V>? = null,
    var rightChild: AVLNode<K, V>? = null
) : MutableMap.MutableEntry<K, V> {
    var height = 1
        private set

    fun size(): Int = getBranchNodeList().count()

    fun setLeft(node: AVLNode<K, V>?) { leftChild = node }

    fun setRight(node: AVLNode<K, V>?) { rightChild = node }

    fun rightHeight() = rightChild?.height ?: 0

    fun leftHeight() = leftChild?.height ?: 0

    fun balanceFactor() = rightHeight() - leftHeight()

    fun updateHeight() { height = max(leftHeight(), rightHeight()) + 1 }

    fun isLeaf() = leftChild == null && rightChild == null

    fun hasOneChild() = (leftChild == null && rightChild != null) || (leftChild != null && rightChild == null)

    private fun rotateLeft(): AVLNode<K, V> {
        val pivot = rightChild!!

        rightChild = pivot.leftChild
        pivot.leftChild = this

        this.updateHeight()
        pivot.updateHeight()

        return pivot
    }

    private fun rotateRight(): AVLNode<K, V> {
        val pivot = leftChild!!

        leftChild = pivot.rightChild
        pivot.rightChild = this

        this.updateHeight()
        pivot.updateHeight()

        return pivot
    }

    private fun rotateRightLeft(): AVLNode<K, V> {
        rightChild ?: return this
        rightChild = rightChild!!.rotateRight()

        return rotateLeft()
    }

    private fun rotateLeftRight(): AVLNode<K, V> {
        leftChild ?: return this
        leftChild = leftChild!!.rotateLeft()

        return rotateRight()
    }

    fun balance(): AVLNode<K, V> {
        val balanceCoefficient = 2

        return when (balanceFactor()) {
            balanceCoefficient -> {
                if (rightChild?.balanceFactor() == -1) {
                    rotateRightLeft()
                } else {
                    rotateLeft()
                }
            }
            -balanceCoefficient -> {
                if (leftChild?.balanceFactor() == 1) {
                    rotateLeftRight()
                } else {
                    rotateRight()
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

    private fun removeMinNodeFromBranch(): AVLNode<K, V> {
        var currentVertex: AVLNode<K, V>? = this
        var parentVertex: AVLNode<K, V>? = null
        while (currentVertex!!.leftChild != null) {
            parentVertex = currentVertex
            currentVertex = currentVertex.leftChild
        }

        parentVertex?.leftChild = currentVertex.rightChild

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
                val swapNode = rightChild!!.removeMinNodeFromBranch()
                if (swapNode != rightChild) swapNode.setRight(rightChild)
                swapNode.setLeft(leftChild)
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

    fun clearBranch() {
        if (hasOneChild()) if (leftChild != null) leftChild = null else rightChild = null
        else if (!isLeaf()) {
            leftChild!!.clearBranch()
            rightChild!!.clearBranch()
            leftChild = null
            rightChild = null
        }

        height = 0
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
