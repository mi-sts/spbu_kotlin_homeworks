@file:Suppress("TooManyFunctions")

package storages

class AVLTree <K : Comparable<K>, V> : MutableMap <K, V> {
    private var treeNode: AVLVertex<K, V>? = null

    private var elementsCount = 0

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() = getBranchVertexesListRecursive(treeNode).toMutableSet()

    override val keys: MutableSet<K>
        get() = getBranchVertexesListRecursive(treeNode).map { it.key }.toMutableSet()

    override val values: MutableCollection<V>
        get() = getBranchVertexesListRecursive(treeNode).map { it.value }.toMutableList()

    enum class Direction {
        LEFT,
        RIGHT,
        NONE
    }

    private fun getRotatedRight(pivotVertex: AVLVertex<K, V>?): AVLVertex<K, V>? {
        if (pivotVertex == null) return null

        val leftVertex = pivotVertex.leftVertex
        pivotVertex.leftVertex = leftVertex?.rightVertex
        leftVertex?.rightVertex = pivotVertex

        pivotVertex.updateVertexHeight()
        leftVertex?.updateVertexHeight()

        return leftVertex
    }

    private fun getRotatedLeft(pivotVertex: AVLVertex<K, V>?): AVLVertex<K, V>? {
        if (pivotVertex == null) return null

        val rightVertex = pivotVertex.rightVertex
        pivotVertex.rightVertex = rightVertex?.leftVertex
        rightVertex?.leftVertex = pivotVertex

        pivotVertex.updateVertexHeight()
        rightVertex?.updateVertexHeight()

        return rightVertex
    }

    private fun getBalancedBranch(vertex: AVLVertex<K, V>?): AVLVertex<K, V>? {
        if (vertex == null) return null

        val balanceCoefficient = 2

        return when (vertex.getBalanceFactor()) {
            balanceCoefficient -> {
                if (vertex.rightVertex?.getBalanceFactor() ?: 0 < 0) vertex.rightVertex =
                    getRotatedRight(vertex.rightVertex)
                getRotatedLeft(vertex)
            }
            -balanceCoefficient -> {
                if (vertex.leftVertex?.getBalanceFactor() ?: 0 > 0) vertex.leftVertex =
                    getRotatedLeft(vertex.leftVertex)
                getRotatedRight(vertex)
            }
            else -> {
                vertex.updateVertexHeight()
                vertex
            }
        }
    }

    private fun putVertexRecursive(
        currentVertex: AVLVertex<K, V>,
        addingVertex: AVLVertex<K, V>,
        parentVertex: AVLVertex<K, V>?,
        direction: Direction
    ): V? {
        if (addingVertex.key == currentVertex.key) {
            if (parentVertex == null) treeNode = addingVertex
            else parentVertex.setChild(addingVertex, direction)

            return currentVertex.value
        }

        val nextVertexDir = if (addingVertex.key < currentVertex.key) Direction.LEFT else Direction.RIGHT

        if (currentVertex.getChild(nextVertexDir) == null) {
            currentVertex.setChild(addingVertex, nextVertexDir)
            elementsCount++
        } else {
            putVertexRecursive(currentVertex.getChild(nextVertexDir)!!, addingVertex, currentVertex, nextVertexDir)
            currentVertex.setChild(getBalancedBranch(currentVertex.getChild(nextVertexDir)), nextVertexDir)
            currentVertex.updateVertexHeight()
        }

        return null
    }

    private fun getMinVertexFromBranch(branchNode: AVLVertex<K, V>?): AVLVertex<K, V>? {
        var currentVertex: AVLVertex<K, V>? = branchNode
        var parentVertex: AVLVertex<K, V>? = null
        while (currentVertex?.leftVertex != null) {
            parentVertex = currentVertex
            currentVertex = currentVertex.leftVertex
        }

        parentVertex?.leftVertex = currentVertex?.rightVertex

        return currentVertex
    }

    private fun removeVertexRecursive(
        key: K,
        checkingVertex: AVLVertex<K, V>?,
        parentVertex: AVLVertex<K, V>?,
        direction: Direction
    ): AVLVertex<K, V>? {
        if (checkingVertex == null) return null

        var removedVertex: AVLVertex<K, V>?

        if (key == checkingVertex.key) {
            var changedChild: AVLVertex<K, V>? = null

            when {
                checkingVertex.isLeaf() -> parentVertex?.setChild(null, direction)
                checkingVertex.hasOneChild() -> changedChild =
                    if (checkingVertex.leftVertex != null) checkingVertex.leftVertex else checkingVertex.rightVertex
                else -> {
                    val swapChild = getMinVertexFromBranch(checkingVertex.rightVertex)

                    if (swapChild != checkingVertex.rightVertex) swapChild!!.rightVertex = checkingVertex.rightVertex

                    swapChild!!.leftVertex = checkingVertex.leftVertex

                    changedChild = swapChild
                }
            }

            if (parentVertex != null) parentVertex.setChild(changedChild, direction)
            else treeNode = changedChild

            changedChild?.updateVertexHeight()
            elementsCount--

            removedVertex = checkingVertex
        } else {
            val nextCheckingVertexDir = if (key > checkingVertex.key) Direction.RIGHT else Direction.LEFT
            removedVertex = removeVertexRecursive(key, checkingVertex.getChild(nextCheckingVertexDir),
                checkingVertex, nextCheckingVertexDir)
        }

        parentVertex?.setChild(getBalancedBranch(parentVertex.getChild(direction)), direction)

        return removedVertex
    }

    private fun removeVertex(key: K): AVLVertex<K, V>? {
        if (treeNode == null) return null

        val searchDir = if (key > treeNode!!.key) Direction.RIGHT else Direction.LEFT

        return removeVertexRecursive(key, treeNode, null, searchDir)
    }

    private fun getVertexRecursive(key: K, checkingVertex: AVLVertex<K, V>?): V? {
        if (checkingVertex == null) return null

        return when {
            key == checkingVertex.key -> checkingVertex.value
            key < checkingVertex.key ->
                if (checkingVertex.leftVertex != null) getVertexRecursive(key, checkingVertex.leftVertex) else null
            else ->
                if (checkingVertex.rightVertex != null) getVertexRecursive(key, checkingVertex.rightVertex) else null
            }
    }

    private fun isVertexExistRecursive(key: K, checkingVertex: AVLVertex<K, V>?): Boolean {
        if (checkingVertex == null) return false

        return when {
            key == checkingVertex.key -> true
            key < checkingVertex.key -> isVertexExistRecursive(key, checkingVertex.leftVertex)
            else -> isVertexExistRecursive(key, checkingVertex.rightVertex)
        }
    }

    private fun getBranchWrittenInDirectOrderRecursive(branchNode: AVLVertex<K, V>?): String {
        if (branchNode == null) return ""

        return "(${branchNode.key} - ${branchNode.value}) " +
                getBranchWrittenInDirectOrderRecursive(branchNode.leftVertex) +
                getBranchWrittenInDirectOrderRecursive(branchNode.rightVertex)
    }

    fun getTreeWrittenInDirectOrder(): String = "${getBranchWrittenInDirectOrderRecursive(treeNode)}\n"

    private fun clearBranchRecursive(branchNode: AVLVertex<K, V>?) {
        if (branchNode == null) return

        clearBranchRecursive(branchNode.leftVertex)
        clearBranchRecursive(branchNode.rightVertex)

        branchNode.leftVertex = null
        branchNode.rightVertex = null
    }

    private fun getBranchVertexesListRecursive(branchNode: AVLVertex<K, V>?): List<AVLVertex<K, V>> {
        if (branchNode == null) return emptyList()

        val childBranchesVertexes =
            if (!branchNode.isLeaf()) getBranchVertexesListRecursive(branchNode.leftVertex)
                .plus(getBranchVertexesListRecursive(branchNode.rightVertex))
            else if (branchNode.leftVertex != null) getBranchVertexesListRecursive(branchNode.leftVertex)
            else if (branchNode.rightVertex != null) getBranchVertexesListRecursive(branchNode.rightVertex)
            else emptyList()

        return childBranchesVertexes.plus(branchNode)
    }

    override val size: Int
        get() = elementsCount

    override fun containsKey(key: K): Boolean = isVertexExistRecursive(key, treeNode)

    override fun containsValue(value: V): Boolean = value in values

    override fun get(key: K): V? = getVertexRecursive(key, treeNode)

    override fun isEmpty(): Boolean = size == 0

    override fun clear() {
        clearBranchRecursive(treeNode)
        treeNode = null

        elementsCount = 0
    }

    override fun put(key: K, value: V): V? {
        var returnedValue: V?

        val vertex = AVLVertex(key, value, 1, null, null)
        if (treeNode == null) {
            treeNode = vertex
            elementsCount = 1

            returnedValue = null
        } else {
            val oldValue: V? = putVertexRecursive(treeNode!!, vertex, null, Direction.NONE)

            if (oldValue == null) {
                treeNode = getBalancedBranch(treeNode)

                returnedValue = null
            } else returnedValue = oldValue
        }

        return returnedValue
    }

    override fun putAll(from: Map<out K, V>) = from.forEach { put(it.key, it.value) }

    override fun remove(key: K): V? = removeVertex(key)?.value
}
