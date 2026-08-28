package com.calc.matrixcalculator.math

class Vector(val values: DoubleArray) {
    val dimension: Int get() = values.size

    init {
        require(values.isNotEmpty()) { "Vector must have at least one component" }
    }

    operator fun get(i: Int): Double = values[i]

    fun copy(): Vector = Vector(values.copyOf())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vector) return false
        if (dimension != other.dimension) return false
        for (i in 0 until dimension) {
            if (!MathUtils.eq(values[i], other.values[i])) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = dimension
        for (v in values) {
            result = 31 * result + MathUtils.clean(v).hashCode()
        }
        return result
    }

    override fun toString(): String {
        return "(%s)".format(values.joinToString(", ") { "%.4f".format(it) })
    }

    companion object {
        fun of(vararg components: Double): Vector = Vector(components.clone())

        fun zero(dimension: Int): Vector = Vector(DoubleArray(dimension))
    }
}
