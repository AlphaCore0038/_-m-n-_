package com.calc.matrixcalculator.math

class Matrix(val rows: Int, val cols: Int, data: Array<DoubleArray>) {
    val data: Array<DoubleArray>

    init {
        require(rows > 0 && cols > 0) { "Matrix dimensions must be positive" }
        require(data.size == rows) { "Expected $rows rows but got ${data.size}" }
        data.forEachIndexed { i, row ->
            require(row.size == cols) { "Row $i: expected $cols columns but got ${row.size}" }
        }
        this.data = Array(rows) { i -> data[i].copyOf() }
    }

    operator fun get(i: Int, j: Int): Double = data[i][j]

    fun copy(): Matrix = Matrix(rows, cols, data)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Matrix) return false
        if (rows != other.rows || cols != other.cols) return false
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (!MathUtils.eq(data[i][j], other.data[i][j])) return false
            }
        }
        return true
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + cols
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                result = 31 * result + MathUtils.clean(data[i][j]).hashCode()
            }
        }
        return result
    }

    override fun toString(): String {
        return buildString {
            for (i in 0 until rows) {
                append("[ ")
                for (j in 0 until cols) {
                    append("%.4f".format(data[i][j]))
                    if (j < cols - 1) append(", ")
                }
                append(" ]\n")
            }
        }
    }

    companion object {
        fun zero(rows: Int, cols: Int): Matrix =
            Matrix(rows, cols, Array(rows) { DoubleArray(cols) })

        fun identity(n: Int): Matrix =
            Matrix(n, n, Array(n) { i -> DoubleArray(n) { j -> if (i == j) 1.0 else 0.0 } })

        fun of(vararg rows: DoubleArray): Matrix {
            require(rows.isNotEmpty()) { "Matrix must have at least one row" }
            val cols = rows[0].size
            require(rows.all { it.size == cols }) { "All rows must have the same number of columns" }
            return Matrix(rows.size, cols, Array(rows.size) { rows[it].copyOf() })
        }
    }
}
