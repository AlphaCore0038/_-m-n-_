package com.calc.matrixcalculator.math

import org.junit.Assert.*
import org.junit.Test

class MathUtilsTest {

    @Test
    fun `eq returns true for equal values`() {
        assertTrue(MathUtils.eq(1.0, 1.0))
        assertTrue(MathUtils.eq(0.0, 0.0))
        assertTrue(MathUtils.eq(-5.5, -5.5))
    }

    @Test
    fun `eq returns true for values within epsilon`() {
        assertTrue(MathUtils.eq(1.0, 1.0 + 1e-11))
        assertTrue(MathUtils.eq(1.0, 1.0 - 1e-11))
    }

    @Test
    fun `eq returns false for values outside epsilon`() {
        assertFalse(MathUtils.eq(1.0, 1.1))
        assertFalse(MathUtils.eq(0.0, 1e-9))
    }

    @Test
    fun `isZero works`() {
        assertTrue(MathUtils.isZero(0.0))
        assertTrue(MathUtils.isZero(1e-11))
        assertTrue(MathUtils.isZero(-1e-11))
        assertFalse(MathUtils.isZero(1.0))
        assertFalse(MathUtils.isZero(-1.0))
    }

    @Test
    fun `isPositive works`() {
        assertTrue(MathUtils.isPositive(1.0))
        assertTrue(MathUtils.isPositive(1e-9))
        assertFalse(MathUtils.isPositive(0.0))
        assertFalse(MathUtils.isPositive(-1.0))
        assertFalse(MathUtils.isPositive(1e-11))
    }

    @Test
    fun `isNegative works`() {
        assertTrue(MathUtils.isNegative(-1.0))
        assertTrue(MathUtils.isNegative(-1e-9))
        assertFalse(MathUtils.isNegative(0.0))
        assertFalse(MathUtils.isNegative(1.0))
        assertFalse(MathUtils.isNegative(-1e-11))
    }

    @Test
    fun `clean normalizes near-zero to zero`() {
        assertEquals(0.0, MathUtils.clean(1e-11), 0.0)
        assertEquals(0.0, MathUtils.clean(-1e-11), 0.0)
        assertEquals(5.0, MathUtils.clean(5.0), 0.0)
    }
}
