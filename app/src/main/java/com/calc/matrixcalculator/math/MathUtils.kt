package com.calc.matrixcalculator.math

import kotlin.math.abs

object MathUtils {
    const val EPSILON = 1e-10

    fun eq(a: Double, b: Double): Boolean = abs(a - b) < EPSILON

    fun isZero(a: Double): Boolean = abs(a) < EPSILON

    fun isPositive(a: Double): Boolean = a > EPSILON

    fun isNegative(a: Double): Boolean = a < -EPSILON

    fun clean(a: Double): Double = if (isZero(a)) 0.0 else a
}
