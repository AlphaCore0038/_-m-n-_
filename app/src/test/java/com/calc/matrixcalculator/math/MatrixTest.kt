package com.calc.matrixcalculator.math

import org.junit.Assert.*
import org.junit.Test

class MatrixTest {

    @Test
    fun `create matrix with valid dimensions`() {
        val m = Matrix(2, 3, arrayOf(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0)))
        assertEquals(2, m.rows)
        assertEquals(3, m.cols)
        assertEquals(1.0, m[0, 0], 0.0)
        assertEquals(6.0, m[1, 2], 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `zero rows throws`() {
        Matrix(0, 3, emptyArray())
    }

    @Test(expected = IllegalArgumentException::class)
    fun `zero cols throws`() {
        Matrix(3, 0, Array(3) { doubleArrayOf() })
    }

    @Test(expected = IllegalArgumentException::class)
    fun `wrong row count throws`() {
        Matrix(2, 2, arrayOf(doubleArrayOf(1.0, 2.0)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `wrong col count throws`() {
        Matrix(2, 2, arrayOf(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0)))
    }

    @Test
    fun `of vararg creates correct matrix`() {
        val m = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        assertEquals(2, m.rows)
        assertEquals(2, m.cols)
        assertEquals(1.0, m[0, 0], 0.0)
        assertEquals(4.0, m[1, 1], 0.0)
    }

    @Test
    fun `zero factory`() {
        val z = Matrix.zero(3, 4)
        assertEquals(3, z.rows)
        assertEquals(4, z.cols)
        for (i in 0 until 3) {
            for (j in 0 until 4) {
                assertEquals(0.0, z[i, j], 0.0)
            }
        }
    }

    @Test
    fun `identity factory`() {
        val i = Matrix.identity(3)
        assertEquals(3, i.rows)
        assertEquals(3, i.cols)
        for (r in 0 until 3) {
            for (c in 0 until 3) {
                assertEquals(if (r == c) 1.0 else 0.0, i[r, c], 0.0)
            }
        }
    }

    @Test
    fun `copy is independent`() {
        val m = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val c = m.copy()
        assertEquals(m, c)
    }

    @Test
    fun `equality with epsilon`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val b = Matrix.of(doubleArrayOf(1.0 + 1e-11, 2.0), doubleArrayOf(3.0, 4.0))
        assertEquals(a, b)
    }

    @Test
    fun `inequality`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val b = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 5.0))
        assertNotEquals(a, b)
    }
}
