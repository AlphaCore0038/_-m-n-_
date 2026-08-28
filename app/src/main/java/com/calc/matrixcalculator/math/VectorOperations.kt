package com.calc.matrixcalculator.math

import kotlin.math.acos
import kotlin.math.sqrt

object VectorOperations {

    fun add(a: Vector, b: Vector): MathResult<Vector> {
        if (a.dimension != b.dimension) {
            return MathResult.Error("Vectors must have the same dimension for addition.")
        }
        val result = DoubleArray(a.dimension) { i -> a[i] + b[i] }
        return MathResult.Success(Vector(result))
    }

    fun subtract(a: Vector, b: Vector): MathResult<Vector> {
        if (a.dimension != b.dimension) {
            return MathResult.Error("Vectors must have the same dimension for subtraction.")
        }
        val result = DoubleArray(a.dimension) { i -> a[i] - b[i] }
        return MathResult.Success(Vector(result))
    }

    fun scalarMultiply(vector: Vector, scalar: Double): MathResult<Vector> {
        val result = DoubleArray(vector.dimension) { i -> vector[i] * scalar }
        return MathResult.Success(Vector(result))
    }

    fun dotProduct(a: Vector, b: Vector): MathResult<Double> {
        if (a.dimension != b.dimension) {
            return MathResult.Error("Vectors must have the same dimension for dot product.")
        }
        var sum = 0.0
        for (i in 0 until a.dimension) {
            sum += a[i] * b[i]
        }
        return MathResult.Success(sum)
    }

    fun crossProduct(a: Vector, b: Vector): MathResult<Vector> {
        if (a.dimension != 3 || b.dimension != 3) {
            return MathResult.Error("Cross product requires two 3-dimensional vectors.")
        }
        val result = doubleArrayOf(
            a[1] * b[2] - a[2] * b[1],
            a[2] * b[0] - a[0] * b[2],
            a[0] * b[1] - a[1] * b[0],
        )
        return MathResult.Success(Vector(result))
    }

    fun magnitude(vector: Vector): MathResult<Double> {
        var sum = 0.0
        for (i in 0 until vector.dimension) {
            sum += vector[i] * vector[i]
        }
        return MathResult.Success(sqrt(sum))
    }

    fun unitVector(vector: Vector): MathResult<Vector> {
        val magResult = magnitude(vector)
        if (magResult is MathResult.Error) return magResult
        val mag = (magResult as MathResult.Success).value
        if (MathUtils.isZero(mag)) {
            return MathResult.Error("Cannot normalize a zero vector.")
        }
        val result = DoubleArray(vector.dimension) { i -> vector[i] / mag }
        return MathResult.Success(Vector(result))
    }

    fun angle(a: Vector, b: Vector): MathResult<Double> {
        if (a.dimension != b.dimension) {
            return MathResult.Error("Vectors must have the same dimension to compute angle.")
        }
        val dotResult = dotProduct(a, b)
        if (dotResult is MathResult.Error) return dotResult
        val magA = (magnitude(a) as? MathResult.Success)?.value ?: return MathResult.Error("Cannot compute angle with a zero vector.")
        val magB = (magnitude(b) as? MathResult.Success)?.value ?: return MathResult.Error("Cannot compute angle with a zero vector.")
        if (MathUtils.isZero(magA) || MathUtils.isZero(magB)) {
            return MathResult.Error("Cannot compute angle with a zero vector.")
        }
        val cosAngle = (dotResult as MathResult.Success).value / (magA * magB)
        val clamped = cosAngle.coerceIn(-1.0, 1.0)
        return MathResult.Success(acos(clamped))
    }

    fun projection(a: Vector, b: Vector): MathResult<Vector> {
        if (a.dimension != b.dimension) {
            return MathResult.Error("Vectors must have the same dimension for projection.")
        }
        val dotResult = dotProduct(b, b)
        if (dotResult is MathResult.Error) return dotResult
        if (MathUtils.isZero((dotResult as MathResult.Success).value)) {
            return MathResult.Error("Cannot project onto a zero vector.")
        }
        val dotAB = (dotProduct(a, b) as? MathResult.Success)?.value ?: return MathResult.Error("Dot product failed.")
        val scalar = dotAB / (dotResult.value)
        val result = DoubleArray(a.dimension) { i -> b[i] * scalar }
        return MathResult.Success(Vector(result))
    }

    fun isParallel(a: Vector, b: Vector): MathResult<Boolean> {
        if (a.dimension != b.dimension) {
            return MathResult.Error("Vectors must have the same dimension to check parallelism.")
        }
        val crossResult = crossProduct(a, b)
        if (crossResult is MathResult.Error) {
            if (crossResult.message.contains("3-dimensional")) {
                val magA = (magnitude(a) as? MathResult.Success)?.value ?: return MathResult.Success(false)
                val magB = (magnitude(b) as? MathResult.Success)?.value ?: return MathResult.Success(false)
                if (MathUtils.isZero(magA) || MathUtils.isZero(magB)) {
                    return MathResult.Success(true)
                }
                var ratio = Double.NaN
                for (i in 0 until a.dimension) {
                    if (!MathUtils.isZero(b[i])) {
                        ratio = a[i] / b[i]
                        break
                    }
                }
                if (ratio.isNaN()) return MathResult.Success(true)
                for (i in 0 until a.dimension) {
                    if (!MathUtils.eq(a[i], ratio * b[i])) {
                        return MathResult.Success(false)
                    }
                }
                return MathResult.Success(true)
            }
            return crossResult
        }
        val cross = (crossResult as MathResult.Success).value
        val crossMag = (magnitude(cross) as? MathResult.Success)?.value ?: return MathResult.Success(true)
        return MathResult.Success(MathUtils.isZero(crossMag))
    }

    fun isPerpendicular(a: Vector, b: Vector): MathResult<Boolean> {
        val dotResult = dotProduct(a, b)
        if (dotResult is MathResult.Error) return dotResult
        return MathResult.Success(MathUtils.isZero((dotResult as MathResult.Success).value))
    }
}
