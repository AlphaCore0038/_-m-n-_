package com.calc.matrixcalculator.viewmodel

import androidx.lifecycle.ViewModel
import com.calc.matrixcalculator.math.MathResult
import com.calc.matrixcalculator.math.Matrix
import com.calc.matrixcalculator.math.MatrixOperations

class MatrixViewModel : ViewModel() {

    var matrixA: Matrix = Matrix.zero(2, 2)
        private set

    var matrixB: Matrix = Matrix.zero(2, 2)
        private set

    var rowsA: Int = 2
        private set

    var colsA: Int = 2
        private set

    var rowsB: Int = 2
        private set

    var colsB: Int = 2
        private set

    var scalarValue: String = "1.0"
        private set

    var resultMatrix: Matrix? = null
        private set

    var resultScalar: Double? = null
        private set

    var resultBoolean: Boolean? = null
        private set

    var resultLabel: String = ""
        private set

    var errorMessage: String? = null
        private set

    var currentInput: Array<DoubleArray> = Array(2) { DoubleArray(2) }
        private set

    var currentInputB: Array<DoubleArray>? = null
        private set

    fun setDimensions(rows: Int, cols: Int) {
        rowsA = rows.coerceIn(1, 10)
        colsA = cols.coerceIn(1, 10)
        val newData = Array(rowsA) { i ->
            DoubleArray(colsA) { j ->
                if (i < currentInput.size && j < currentInput[0].size) currentInput[i][j] else 0.0
            }
        }
        currentInput = newData
        matrixA = Matrix(rowsA, colsA, currentInput)
    }

    fun setDimensionsB(rows: Int, cols: Int) {
        rowsB = rows.coerceIn(1, 10)
        colsB = cols.coerceIn(1, 10)
        val existing = currentInputB ?: Array(matrixB.rows) { i ->
            DoubleArray(matrixB.cols) { j -> matrixB[i, j] }
        }
        val newData = Array(rowsB) { i ->
            DoubleArray(colsB) { j ->
                if (i < existing.size && j < existing[0].size) existing[i][j] else 0.0
            }
        }
        currentInputB = newData
        matrixB = Matrix(rowsB, colsB, newData)
    }

    fun updateCell(i: Int, j: Int, value: String) {
        val parsed = value.toDoubleOrNull() ?: return
        if (i < currentInput.size && j < currentInput[0].size) {
            currentInput[i][j] = parsed
            matrixA = Matrix(rowsA, colsA, currentInput)
        }
    }

    fun updateCellB(i: Int, j: Int, value: String) {
        val parsed = value.toDoubleOrNull() ?: return
        val data = currentInputB ?: Array(rowsB) { r ->
            DoubleArray(colsB) { c -> matrixB[r, c] }
        }
        if (i < data.size && j < data[0].size) {
            data[i][j] = parsed
            currentInputB = data
            matrixB = Matrix(rowsB, colsB, data)
        }
    }

    fun updateScalar(value: String) {
        scalarValue = value
    }

    fun setZeroMatrix() {
        currentInput = Array(rowsA) { DoubleArray(colsA) }
        matrixA = Matrix.zero(rowsA, colsA)
    }

    fun setIdentityMatrix() {
        if (rowsA != colsA) return
        currentInput = Array(rowsA) { i -> DoubleArray(colsA) { j -> if (i == j) 1.0 else 0.0 } }
        matrixA = Matrix.identity(rowsA)
    }

    fun clearMatrix() {
        currentInput = Array(rowsA) { DoubleArray(colsA) }
        matrixA = Matrix(rowsA, colsA, currentInput)
    }

    fun setZeroMatrixB() {
        currentInputB = Array(rowsB) { DoubleArray(colsB) }
        matrixB = Matrix.zero(rowsB, colsB)
    }

    fun setIdentityMatrixB() {
        if (rowsB != colsB) return
        currentInputB = Array(rowsB) { i -> DoubleArray(colsB) { j -> if (i == j) 1.0 else 0.0 } }
        matrixB = Matrix.identity(rowsB)
    }

    fun clearMatrixB() {
        currentInputB = Array(rowsB) { DoubleArray(colsB) }
        matrixB = Matrix(rowsB, colsB, currentInputB!!)
    }

    fun clearResult() {
        resultMatrix = null
        resultScalar = null
        resultBoolean = null
        resultLabel = ""
        errorMessage = null
    }

    fun performAdd() {
        val result = MatrixOperations.add(matrixA, matrixB)
        handleResult(result, "A + B")
    }

    fun performSubtract() {
        val result = MatrixOperations.subtract(matrixA, matrixB)
        handleResult(result, "A − B")
    }

    fun performMultiply() {
        val result = MatrixOperations.multiply(matrixA, matrixB)
        handleResult(result, "A × B")
    }

    fun performScalarMultiply() {
        val scalar = scalarValue.toDoubleOrNull()
        if (scalar == null) {
            errorMessage = "Invalid scalar value."
            return
        }
        val result = MatrixOperations.scalarMultiply(matrixA, scalar)
        handleResult(result, "${scalar} × A")
    }

    fun performPower() {
        val power = scalarValue.toIntOrNull()
        if (power == null) {
            errorMessage = "Power must be a non-negative integer."
            return
        }
        val result = MatrixOperations.power(matrixA, power)
        handleResult(result, "A^$power")
    }

    fun performTranspose() {
        val result = MatrixOperations.transpose(matrixA)
        handleResult(result, "A^T")
    }

    fun performDeterminant() {
        handleDoubleResult(MatrixOperations.determinant(matrixA), "det(A)")
    }

    fun performInverse() {
        val result = MatrixOperations.inverse(matrixA)
        handleResult(result, "A⁻¹")
    }

    fun performRank() {
        val result = MatrixOperations.rank(matrixA)
        when (result) {
            is MathResult.Success -> handleDoubleResult(MathResult.Success(result.value.toDouble()), "rank(A)")
            is MathResult.Error -> errorMessage = result.message
        }
    }

    fun performTrace() {
        handleDoubleResult(MatrixOperations.trace(matrixA), "tr(A)")
    }

    fun performREF() {
        val result = MatrixOperations.ref(matrixA)
        handleResult(result, "REF(A)")
    }

    fun performRREF() {
        val result = MatrixOperations.rref(matrixA)
        handleResult(result, "RREF(A)")
    }

    fun performGaussianElimination() {
        val result = MatrixOperations.gaussianElimination(matrixA)
        handleResult(result, "Gaussian Elimination")
    }

    fun performGaussJordanElimination() {
        val result = MatrixOperations.gaussJordanElimination(matrixA)
        handleResult(result, "Gauss-Jordan Elimination")
    }

    fun performIsSymmetric() {
        checkClassification("Symmetric") {
            MatrixOperations.isSymmetric(matrixA)
        }
    }

    fun performIsSkewSymmetric() {
        checkClassification("Skew-symmetric") {
            MatrixOperations.isSkewSymmetric(matrixA)
        }
    }

    fun performIsOrthogonal() {
        checkClassification("Orthogonal") {
            MatrixOperations.isOrthogonal(matrixA)
        }
    }

    fun performIsSingular() {
        val result = MatrixOperations.isSingular(matrixA)
        when (result) {
            is MathResult.Success -> {
                resultMatrix = null
                resultScalar = null
                resultBoolean = result.value
                resultLabel = if (result.value) "Singular: Yes" else "Singular: No"
                errorMessage = null
            }
            is MathResult.Error -> {
                errorMessage = result.message
                resultMatrix = null
                resultScalar = null
                resultBoolean = null
            }
        }
    }

    fun performIsPositiveDefinite() {
        checkClassification("Positive definite") {
            MatrixOperations.isPositiveDefinite(matrixA)
        }
    }

    fun performIsNegativeDefinite() {
        checkClassification("Negative definite") {
            MatrixOperations.isNegativeDefinite(matrixA)
        }
    }

    private fun checkClassification(label: String, operation: () -> MathResult<Boolean>) {
        val result = operation()
        when (result) {
            is MathResult.Success -> {
                resultMatrix = null
                resultScalar = null
                resultBoolean = result.value
                resultLabel = "$label: ${if (result.value) "Yes" else "No"}"
                errorMessage = null
            }
            is MathResult.Error -> {
                errorMessage = result.message
                resultMatrix = null
                resultScalar = null
                resultBoolean = null
            }
        }
    }

    private fun handleResult(result: MathResult<Matrix>, label: String) {
        when (result) {
            is MathResult.Success -> {
                resultMatrix = result.value
                resultScalar = null
                resultBoolean = null
                resultLabel = label
                errorMessage = null
            }
            is MathResult.Error -> {
                errorMessage = result.message
                resultMatrix = null
                resultScalar = null
                resultBoolean = null
            }
        }
    }

    private fun handleDoubleResult(result: MathResult<Double>, label: String) {
        when (result) {
            is MathResult.Success -> {
                resultMatrix = null
                resultScalar = result.value
                resultBoolean = null
                resultLabel = label
                errorMessage = null
            }
            is MathResult.Error -> {
                errorMessage = result.message
                resultMatrix = null
                resultScalar = null
                resultBoolean = null
            }
        }
    }

    fun useResultAsInputA() {
        val res = resultMatrix ?: return
        rowsA = res.rows
        colsA = res.cols
        currentInput = Array(rowsA) { i -> DoubleArray(colsA) { j -> res[i, j] } }
        matrixA = res.copy()
        clearResult()
    }

    fun useResultAsInputB() {
        val res = resultMatrix ?: return
        rowsB = res.rows
        colsB = res.cols
        currentInputB = Array(rowsB) { i -> DoubleArray(colsB) { j -> res[i, j] } }
        matrixB = res.copy()
        clearResult()
    }
}
