package com.calc.matrixcalculator.math

import org.junit.Assert.*
import org.junit.Test

class VectorTest {

    @Test
    fun `create vector`() {
        val v = Vector.of(1.0, 2.0, 3.0)
        assertEquals(3, v.dimension)
        assertEquals(1.0, v[0], 0.0)
        assertEquals(3.0, v[2], 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `empty vector throws`() {
        Vector(doubleArrayOf())
    }

    @Test
    fun `zero factory`() {
        val z = Vector.zero(4)
        assertEquals(4, z.dimension)
        for (i in 0 until 4) {
            assertEquals(0.0, z[i], 0.0)
        }
    }

    @Test
    fun `copy is independent`() {
        val v = Vector.of(1.0, 2.0)
        val c = v.copy()
        assertEquals(v, c)
    }

    @Test
    fun `equality with epsilon`() {
        val a = Vector.of(1.0, 2.0)
        val b = Vector.of(1.0 + 1e-11, 2.0)
        assertEquals(a, b)
    }

    @Test
    fun `inequality`() {
        val a = Vector.of(1.0, 2.0)
        val b = Vector.of(1.0, 3.0)
        assertNotEquals(a, b)
    }

    @Test
    fun `dimension`() {
        assertEquals(1, Vector.of(5.0).dimension)
        assertEquals(5, Vector.of(1.0, 2.0, 3.0, 4.0, 5.0).dimension)
    }
}
