package com.calc.matrixcalculator.viewmodel

import androidx.lifecycle.ViewModel
import com.calc.matrixcalculator.math.MathResult
import com.calc.matrixcalculator.math.Vector
import com.calc.matrixcalculator.math.VectorOperations

class VectorViewModel : ViewModel() {

    var vectorA: Vector = Vector.zero(3)
        private set

    var vectorB: Vector = Vector.zero(3)
        private set

    var dimensionA: Int = 3
        private set

    var dimensionB: Int = 3
        private set

    var scalarValue: String = "1.0"
        private set

    var resultVector: Vector? = null
        private set

    var resultScalar: Double? = null
        private set

    var resultBoolean: Boolean? = null
        private set

    var resultLabel: String = ""
        private set

    var errorMessage: String? = null
        private set

    var currentInputA: DoubleArray = DoubleArray(3)
        private set

    var currentInputB: DoubleArray = DoubleArray(3)
        private set

    fun setDimensionA(dim: Int) {
        dimensionA = dim.coerceIn(1, 10)
        val newData = DoubleArray(dimensionA) { i ->
            if (i < currentInputA.size) currentInputA[i] else 0.0
        }
        currentInputA = newData
        vectorA = Vector(newData)
    }

    fun setDimensionB(dim: Int) {
        dimensionB = dim.coerceIn(1, 10)
        val newData = DoubleArray(dimensionB) { i ->
            if (i < currentInputB.size) currentInputB[i] else 0.0
        }
        currentInputB = newData
        vectorB = Vector(newData)
    }

    fun updateComponentA(i: Int, value: String) {
        val parsed = value.toDoubleOrNull() ?: return
        if (i < currentInputA.size) {
            currentInputA[i] = parsed
            vectorA = Vector(currentInputA)
        }
    }

    fun updateComponentB(i: Int, value: String) {
        val parsed = value.toDoubleOrNull() ?: return
        if (i < currentInputB.size) {
            currentInputB[i] = parsed
            vectorB = Vector(currentInputB)
        }
    }

    fun updateScalar(value: String) {
        scalarValue = value
    }

    fun clearVectorA() {
        currentInputA = DoubleArray(dimensionA)
        vectorA = Vector.zero(dimensionA)
    }

    fun clearVectorB() {
        currentInputB = DoubleArray(dimensionB)
        vectorB = Vector.zero(dimensionB)
    }

    fun clearResult() {
        resultVector = null
        resultScalar = null
        resultBoolean = null
        resultLabel = ""
        errorMessage = null
    }

    fun performAdd() {
        val result = VectorOperations.add(vectorA, vectorB)
        handleVectorResult(result, "v + w")
    }

    fun performSubtract() {
        val result = VectorOperations.subtract(vectorA, vectorB)
        handleVectorResult(result, "v − w")
    }

    fun performScalarMultiply() {
        val scalar = scalarValue.toDoubleOrNull()
        if (scalar == null) {
            errorMessage = "Invalid scalar value."
            return
        }
        val result = VectorOperations.scalarMultiply(vectorA, scalar)
        handleVectorResult(result, "$scalar × v")
    }

    fun performDotProduct() {
        val result = VectorOperations.dotProduct(vectorA, vectorB)
        handleScalarResult(result, "v · w")
    }

    fun performCrossProduct() {
        val result = VectorOperations.crossProduct(vectorA, vectorB)
        handleVectorResult(result, "v × w")
    }

    fun performMagnitude() {
        val result = VectorOperations.magnitude(vectorA)
        handleScalarResult(result, "|v|")
    }

    fun performUnitVector() {
        val result = VectorOperations.unitVector(vectorA)
        handleVectorResult(result, "û")
    }

    fun performAngle() {
        val result = VectorOperations.angle(vectorA, vectorB)
        when (result) {
            is MathResult.Success -> {
                resultVector = null
                resultScalar = Math.toDegrees(result.value)
                resultBoolean = null
                resultLabel = "angle(v,w) (degrees)"
                errorMessage = null
            }
            is MathResult.Error -> {
                errorMessage = result.message
                resultVector = null
                resultScalar = null
                resultBoolean = null
            }
        }
    }

    fun performProjection() {
        val result = VectorOperations.projection(vectorA, vectorB)
        handleVectorResult(result, "proj(v onto w)")
    }

    fun performIsParallel() {
        val result = VectorOperations.isParallel(vectorA, vectorB)
        when (result) {
            is MathResult.Success -> {
                resultVector = null
                resultScalar = null
                resultBoolean = result.value
                resultLabel = "Parallel: ${if (result.value) "Yes" else "No"}"
                errorMessage = null
            }
            is MathResult.Error -> {
                errorMessage = result.message
                resultVector = null
                resultScalar = null
                resultBoolean = null
            }
        }
    }

    fun performIsPerpendicular() {
        val result = VectorOperations.isPerpendicular(vectorA, vectorB)
        when (result) {
            is MathResult.Success -> {
                resultVector = null
                resultScalar = null
                resultBoolean = result.value
                resultLabel = "Perpendicular: ${if (result.value) "Yes" else "No"}"
                errorMessage = null
            }
            is MathResult.Error -> {
                errorMessage = result.message
                resultVector = null
                resultScalar = null
                resultBoolean = null
            }
        }
    }

    private fun handleVectorResult(result: MathResult<Vector>, label: String) {
        when (result) {
            is MathResult.Success -> {
                resultVector = result.value
                resultScalar = null
                resultBoolean = null
                resultLabel = label
                errorMessage = null
            }
            is MathResult.Error -> {
                errorMessage = result.message
                resultVector = null
                resultScalar = null
                resultBoolean = null
            }
        }
    }

    private fun handleScalarResult(result: MathResult<Double>, label: String) {
        when (result) {
            is MathResult.Success -> {
                resultVector = null
                resultScalar = result.value
                resultBoolean = null
                resultLabel = label
                errorMessage = null
            }
            is MathResult.Error -> {
                errorMessage = result.message
                resultVector = null
                resultScalar = null
                resultBoolean = null
            }
        }
    }

    fun useResultAsInputA() {
        val res = resultVector ?: return
        dimensionA = res.dimension
        currentInputA = res.values.copyOf()
        vectorA = res.copy()
        clearResult()
    }

    fun useResultAsInputB() {
        val res = resultVector ?: return
        dimensionB = res.dimension
        currentInputB = res.values.copyOf()
        vectorB = res.copy()
        clearResult()
    }
}
