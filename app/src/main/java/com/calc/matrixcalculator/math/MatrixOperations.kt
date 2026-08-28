package com.calc.matrixcalculator.math

import kotlin.math.abs

object MatrixOperations {

    fun add(a: Matrix, b: Matrix): MathResult<Matrix> {
        if (a.rows != b.rows || a.cols != b.cols) {
            return MathResult.Error("Matrices must have the same dimensions for addition.")
        }
        val result = Array(a.rows) { i ->
            DoubleArray(a.cols) { j -> a[i, j] + b[i, j] }
        }
        return MathResult.Success(Matrix(a.rows, a.cols, result))
    }

    fun subtract(a: Matrix, b: Matrix): MathResult<Matrix> {
        if (a.rows != b.rows || a.cols != b.cols) {
            return MathResult.Error("Matrices must have the same dimensions for subtraction.")
        }
        val result = Array(a.rows) { i ->
            DoubleArray(a.cols) { j -> a[i, j] - b[i, j] }
        }
        return MathResult.Success(Matrix(a.rows, a.cols, result))
    }

    fun multiply(a: Matrix, b: Matrix): MathResult<Matrix> {
        if (a.cols != b.rows) {
            return MathResult.Error("Columns of Matrix A (${a.cols}) must equal rows of Matrix B (${b.rows}).")
        }
        val result = Array(a.rows) { i ->
            DoubleArray(b.cols) { j ->
                var sum = 0.0
                for (k in 0 until a.cols) {
                    sum += a[i, k] * b[k, j]
                }
                sum
            }
        }
        return MathResult.Success(Matrix(a.rows, b.cols, result))
    }

    fun scalarMultiply(matrix: Matrix, scalar: Double): MathResult<Matrix> {
        val result = Array(matrix.rows) { i ->
            DoubleArray(matrix.cols) { j -> matrix[i, j] * scalar }
        }
        return MathResult.Success(Matrix(matrix.rows, matrix.cols, result))
    }

    fun power(matrix: Matrix, n: Int): MathResult<Matrix> {
        if (n < 0) {
            return MathResult.Error("Negative powers are not supported.")
        }
        if (matrix.rows != matrix.cols) {
            return MathResult.Error("Matrix must be square for power operation.")
        }
        if (n == 0) {
            return MathResult.Success(Matrix.identity(matrix.rows))
        }
        if (n == 1) {
            return MathResult.Success(matrix.copy())
        }
        var result = matrix.copy()
        for (i in 1 until n) {
            val multiplied = multiply(result, matrix)
            if (multiplied is MathResult.Error) return multiplied
            result = (multiplied as MathResult.Success).value
        }
        return MathResult.Success(result)
    }

    fun transpose(matrix: Matrix): MathResult<Matrix> {
        val result = Array(matrix.cols) { j ->
            DoubleArray(matrix.rows) { i -> matrix[i, j] }
        }
        return MathResult.Success(Matrix(matrix.cols, matrix.rows, result))
    }

    fun trace(matrix: Matrix): MathResult<Double> {
        if (matrix.rows != matrix.cols) {
            return MathResult.Error("Trace requires a square matrix.")
        }
        var sum = 0.0
        for (i in 0 until matrix.rows) {
            sum += matrix[i, i]
        }
        return MathResult.Success(sum)
    }

    fun determinant(matrix: Matrix): MathResult<Double> {
        if (matrix.rows != matrix.cols) {
            return MathResult.Error("Determinant requires a square matrix.")
        }
        val n = matrix.rows
        if (n == 1) {
            return MathResult.Success(matrix[0, 0])
        }
        if (n == 2) {
            return MathResult.Success(matrix[0, 0] * matrix[1, 1] - matrix[0, 1] * matrix[1, 0])
        }
        val (lu, swaps) = luDecomposition(matrix)
        var det = if (swaps % 2 == 0) 1.0 else -1.0
        for (i in 0 until n) {
            det *= lu[i][i]
        }
        return MathResult.Success(MathUtils.clean(det))
    }

    fun inverse(matrix: Matrix): MathResult<Matrix> {
        if (matrix.rows != matrix.cols) {
            return MathResult.Error("Inverse requires a square matrix.")
        }
        val n = matrix.rows
        val detResult = determinant(matrix)
        if (detResult is MathResult.Error) return detResult
        if (MathUtils.isZero((detResult as MathResult.Success).value)) {
            return MathResult.Error("Matrix is singular and does not have an inverse.")
        }
        val augmented = Array(n) { i ->
            DoubleArray(2 * n) { j ->
                if (j < n) matrix[i, j] else if (j - n == i) 1.0 else 0.0
            }
        }
        gaussJordanFull(augmented, n)
        val inv = Array(n) { i ->
            DoubleArray(n) { j -> augmented[i][j + n] }
        }
        return MathResult.Success(Matrix(n, n, inv))
    }

    fun rank(matrix: Matrix): MathResult<Int> {
        val rref = rref(matrix)
        if (rref is MathResult.Error) return rref
        val reduced = (rref as MathResult.Success).value
        var rank = 0
        for (i in 0 until reduced.rows) {
            var hasPivot = false
            for (j in 0 until reduced.cols) {
                if (!MathUtils.isZero(reduced[i, j])) {
                    hasPivot = true
                    break
                }
            }
            if (hasPivot) rank++
        }
        return MathResult.Success(rank)
    }

    fun ref(matrix: Matrix): MathResult<Matrix> {
        val copy = Array(matrix.rows) { matrix.data[it].copyOf() }
        val n = matrix.rows
        val m = matrix.cols
        var pivotRow = 0
        var col = 0
        while (col < m && pivotRow < n) {
            var maxRow = pivotRow
            for (row in pivotRow + 1 until n) {
                if (abs(copy[row][col]) > abs(copy[maxRow][col])) {
                    maxRow = row
                }
            }
            if (MathUtils.isZero(copy[maxRow][col])) { col++; continue }
            val temp = copy[pivotRow]
            copy[pivotRow] = copy[maxRow]
            copy[maxRow] = temp
            val pivot = copy[pivotRow][col]
            for (j in col until m) {
                copy[pivotRow][j] /= pivot
            }
            for (row in pivotRow + 1 until n) {
                val factor = copy[row][col]
                if (!MathUtils.isZero(factor)) {
                    for (j in col until m) {
                        copy[row][j] -= factor * copy[pivotRow][j]
                    }
                }
            }
            pivotRow++
            col++
        }
        for (i in 0 until n) {
            for (j in 0 until m) {
                copy[i][j] = MathUtils.clean(copy[i][j])
            }
        }
        return MathResult.Success(Matrix(n, m, copy))
    }

    fun rref(matrix: Matrix): MathResult<Matrix> {
        val copy = Array(matrix.rows) { matrix.data[it].copyOf() }
        val n = matrix.rows
        val m = matrix.cols
        var pivotRow = 0
        var rrefCol = 0
        while (rrefCol < m && pivotRow < n) {
            var maxRow = pivotRow
            for (row in pivotRow + 1 until n) {
                if (abs(copy[row][rrefCol]) > abs(copy[maxRow][rrefCol])) {
                    maxRow = row
                }
            }
            if (MathUtils.isZero(copy[maxRow][rrefCol])) { rrefCol++; continue }
            val temp = copy[pivotRow]
            copy[pivotRow] = copy[maxRow]
            copy[maxRow] = temp
            val pivot = copy[pivotRow][rrefCol]
            for (j in 0 until m) {
                copy[pivotRow][j] /= pivot
            }
            for (row in 0 until n) {
                if (row == pivotRow) continue
                val factor = copy[row][rrefCol]
                if (!MathUtils.isZero(factor)) {
                    for (j in 0 until m) {
                        copy[row][j] -= factor * copy[pivotRow][j]
                    }
                }
            }
            pivotRow++
            rrefCol++
        }
        for (i in 0 until n) {
            for (j in 0 until m) {
                copy[i][j] = MathUtils.clean(copy[i][j])
            }
        }
        return MathResult.Success(Matrix(n, m, copy))
    }

    fun gaussianElimination(matrix: Matrix): MathResult<Matrix> = ref(matrix)

    fun gaussJordanElimination(matrix: Matrix): MathResult<Matrix> = rref(matrix)

    fun isSymmetric(matrix: Matrix): MathResult<Boolean> {
        if (matrix.rows != matrix.cols) {
            return MathResult.Success(false)
        }
        for (i in 0 until matrix.rows) {
            for (j in 0 until i) {
                if (!MathUtils.eq(matrix[i, j], matrix[j, i])) {
                    return MathResult.Success(false)
                }
            }
        }
        return MathResult.Success(true)
    }

    fun isSkewSymmetric(matrix: Matrix): MathResult<Boolean> {
        if (matrix.rows != matrix.cols) {
            return MathResult.Success(false)
        }
        for (i in 0 until matrix.rows) {
            if (!MathUtils.isZero(matrix[i, i])) {
                return MathResult.Success(false)
            }
            for (j in 0 until i) {
                if (!MathUtils.eq(matrix[i, j], -matrix[j, i])) {
                    return MathResult.Success(false)
                }
            }
        }
        return MathResult.Success(true)
    }

    fun isOrthogonal(matrix: Matrix): MathResult<Boolean> {
        if (matrix.rows != matrix.cols) {
            return MathResult.Success(false)
        }
        val transposed = transpose(matrix)
        if (transposed is MathResult.Error) return transposed
        val product = multiply((transposed as MathResult.Success).value, matrix)
        if (product is MathResult.Error) return product
        val identity = Matrix.identity(matrix.rows)
        return MathResult.Success((product as MathResult.Success).value == identity)
    }

    fun isSingular(matrix: Matrix): MathResult<Boolean> {
        if (matrix.rows != matrix.cols) {
            return MathResult.Error("Singularity is only defined for square matrices.")
        }
        val detResult = determinant(matrix)
        if (detResult is MathResult.Error) return detResult
        return MathResult.Success(MathUtils.isZero((detResult as MathResult.Success).value))
    }

    fun isPositiveDefinite(matrix: Matrix): MathResult<Boolean> {
        if (matrix.rows != matrix.cols) {
            return MathResult.Success(false)
        }
        val symCheck = isSymmetric(matrix)
        if (symCheck is MathResult.Error) return symCheck
        if (!(symCheck as MathResult.Success).value) {
            return MathResult.Success(false)
        }
        for (k in 1..matrix.rows) {
            val minor = leadingPrincipalMinor(matrix, k)
            if (minor <= 0) return MathResult.Success(false)
        }
        return MathResult.Success(true)
    }

    fun isNegativeDefinite(matrix: Matrix): MathResult<Boolean> {
        if (matrix.rows != matrix.cols) {
            return MathResult.Success(false)
        }
        val symCheck = isSymmetric(matrix)
        if (symCheck is MathResult.Error) return symCheck
        if (!(symCheck as MathResult.Success).value) {
            return MathResult.Success(false)
        }
        for (k in 1..matrix.rows) {
            val minor = leadingPrincipalMinor(matrix, k)
            val expectedSign = if (k % 2 == 0) 1.0 else -1.0
            if (minor * expectedSign <= 0) return MathResult.Success(false)
        }
        return MathResult.Success(true)
    }

    private fun leadingPrincipalMinor(matrix: Matrix, k: Int): Double {
        val sub = Array(k) { i -> DoubleArray(k) { j -> matrix[i, j] } }
        val subMatrix = Matrix(k, k, sub)
        val detResult = determinant(subMatrix)
        return if (detResult is MathResult.Success) detResult.value else 0.0
    }

    private fun luDecomposition(matrix: Matrix): Pair<Array<DoubleArray>, Int> {
        val n = matrix.rows
        val a = Array(n) { matrix.data[it].copyOf() }
        var swaps = 0
        for (col in 0 until n) {
            var maxRow = col
            for (row in col + 1 until n) {
                if (abs(a[row][col]) > abs(a[maxRow][col])) {
                    maxRow = row
                }
            }
            if (maxRow != col) {
                val temp = a[col]
                a[col] = a[maxRow]
                a[maxRow] = temp
                swaps++
            }
            if (MathUtils.isZero(a[col][col])) continue
            for (row in col + 1 until n) {
                val factor = a[row][col] / a[col][col]
                a[row][col] = factor
                for (j in col + 1 until n) {
                    a[row][j] -= factor * a[col][j]
                }
            }
        }
        return Pair(a, swaps)
    }

    private fun gaussJordanFull(augmented: Array<DoubleArray>, n: Int) {
        val m = augmented[0].size
        var pivotRow = 0
        var gjCol = 0
        while (gjCol < n && pivotRow < n) {
            var maxRow = pivotRow
            for (row in pivotRow + 1 until n) {
                if (abs(augmented[row][gjCol]) > abs(augmented[maxRow][gjCol])) {
                    maxRow = row
                }
            }
            if (MathUtils.isZero(augmented[maxRow][gjCol])) { gjCol++; continue }
            val temp = augmented[pivotRow]
            augmented[pivotRow] = augmented[maxRow]
            augmented[maxRow] = temp
            val pivot = augmented[pivotRow][gjCol]
            for (j in 0 until m) {
                augmented[pivotRow][j] /= pivot
            }
            for (row in 0 until n) {
                if (row == pivotRow) continue
                val factor = augmented[row][gjCol]
                if (!MathUtils.isZero(factor)) {
                    for (j in 0 until m) {
                        augmented[row][j] -= factor * augmented[pivotRow][j]
                    }
                }
            }
            pivotRow++
            gjCol++
        }
    }
}
