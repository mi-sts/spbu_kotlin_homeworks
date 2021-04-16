package util.hash_Table

import kotlin.math.pow

interface HashFunction<K> {
    val name: String
    fun hashOf(key: K): Int
}

object SimpleHashFunction : HashFunction<Any> {
    override val name: String = "Simple hash function"
    override fun hashOf(key: Any): Int = key.hashCode()
}

object SimpleStringHashFunction : HashFunction<String> {
    override val name: String = "Simple string hash function"
    override fun hashOf(key: String): Int = key.sumOf { it.toInt() }
}

object PolynomialHashFunction : HashFunction<String> {
    override val name: String = "Polynomial hash function"
    private const val POLYNOMIAL_VALUE = 13
    override fun hashOf(key: String): Int {
        return key.mapIndexed { index, char -> char.toInt() * POLYNOMIAL_VALUE.toFloat().pow(index) }.sum().toInt()
    }
}
