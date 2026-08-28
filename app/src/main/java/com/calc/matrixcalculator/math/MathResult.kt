package com.calc.matrixcalculator.math

sealed class MathResult<out T> {
    data class Success<T>(val value: T) : MathResult<T>()
    data class Error(val message: String) : MathResult<Nothing>()
}
