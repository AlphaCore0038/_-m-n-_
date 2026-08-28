package com.calc.matrixcalculator.math

import org.junit.Assert.*
import org.junit.Test

class MatrixOperationsTest {

    private val EPS = 1e-6

    private fun assertMatrixEquals(expected: Matrix, actual: Matrix) {
        assertEquals(expected.rows, actual.rows)
        assertEquals(expected.cols, actual.cols)
        for (i in 0 until expected.rows) {
            for (j in 0 until expected.cols) {
                assertEquals("Mismatch at [$i][$j]", expected[i, j], actual[i, j], EPS)
            }
        }
    }

    private fun assertSuccess(result: MathResult<Matrix>): Matrix {
        assertTrue("Expected Success but got Error: ${(result as? MathResult.Error)?.message}", result is MathResult.Success)
        return (result as MathResult.Success).value
    }

    private fun assertError(result: MathResult<Matrix>) {
        assertTrue("Expected Error but got Success", result is MathResult.Error)
    }

    // ========== ADDITION ==========

    @Test
    fun `add 2x2 matrices`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val b = Matrix.of(doubleArrayOf(5.0, 6.0), doubleArrayOf(7.0, 8.0))
        val result = assertSuccess(MatrixOperations.add(a, b))
        assertMatrixEquals(Matrix.of(doubleArrayOf(6.0, 8.0), doubleArrayOf(10.0, 12.0)), result)
    }

    @Test
    fun `add 3x3 matrices`() {
        val a = Matrix.identity(3)
        val b = Matrix.identity(3)
        val result = assertSuccess(MatrixOperations.add(a, b))
        assertMatrixEquals(Matrix.of(
            doubleArrayOf(2.0, 0.0, 0.0),
            doubleArrayOf(0.0, 2.0, 0.0),
            doubleArrayOf(0.0, 0.0, 2.0),
        ), result)
    }

    @Test
    fun `add with zero matrix`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val z = Matrix.zero(2, 2)
        val result = assertSuccess(MatrixOperations.add(a, z))
        assertMatrixEquals(a, result)
    }

    @Test
    fun `add negative values`() {
        val a = Matrix.of(doubleArrayOf(-1.0, -2.0), doubleArrayOf(-3.0, -4.0))
        val b = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val result = assertSuccess(MatrixOperations.add(a, b))
        assertMatrixEquals(Matrix.zero(2, 2), result)
    }

    @Test
    fun `add mismatched dimensions returns error`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val b = Matrix.identity(3)
        assertError(MatrixOperations.add(a, b))
    }

    @Test
    fun `add 1x1 matrices`() {
        val a = Matrix.of(doubleArrayOf(5.0))
        val b = Matrix.of(doubleArrayOf(3.0))
        val result = assertSuccess(MatrixOperations.add(a, b))
        assertMatrixEquals(Matrix.of(doubleArrayOf(8.0)), result)
    }

    // ========== SUBTRACTION ==========

    @Test
    fun `subtract 2x2 matrices`() {
        val a = Matrix.of(doubleArrayOf(5.0, 6.0), doubleArrayOf(7.0, 8.0))
        val b = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val result = assertSuccess(MatrixOperations.subtract(a, b))
        assertMatrixEquals(Matrix.of(doubleArrayOf(4.0, 4.0), doubleArrayOf(4.0, 4.0)), result)
    }

    @Test
    fun `subtract same matrix returns zero`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val result = assertSuccess(MatrixOperations.subtract(a, a))
        assertMatrixEquals(Matrix.zero(2, 2), result)
    }

    @Test
    fun `subtract mismatched dimensions returns error`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0))
        val b = Matrix.of(doubleArrayOf(1.0, 2.0, 3.0))
        assertError(MatrixOperations.subtract(a, b))
    }

    // ========== MULTIPLICATION ==========

    @Test
    fun `multiply 2x2 matrices`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val b = Matrix.of(doubleArrayOf(5.0, 6.0), doubleArrayOf(7.0, 8.0))
        val result = assertSuccess(MatrixOperations.multiply(a, b))
        assertMatrixEquals(Matrix.of(doubleArrayOf(19.0, 22.0), doubleArrayOf(43.0, 50.0)), result)
    }

    @Test
    fun `multiply by identity`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val i = Matrix.identity(2)
        val result = assertSuccess(MatrixOperations.multiply(a, i))
        assertMatrixEquals(a, result)
    }

    @Test
    fun `multiply 3x3 matrices`() {
        val a = Matrix.of(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
            doubleArrayOf(7.0, 8.0, 9.0),
        )
        val b = Matrix.of(
            doubleArrayOf(9.0, 8.0, 7.0),
            doubleArrayOf(6.0, 5.0, 4.0),
            doubleArrayOf(3.0, 2.0, 1.0),
        )
        val result = assertSuccess(MatrixOperations.multiply(a, b))
        assertMatrixEquals(Matrix.of(
            doubleArrayOf(30.0, 24.0, 18.0),
            doubleArrayOf(84.0, 69.0, 54.0),
            doubleArrayOf(138.0, 114.0, 90.0),
        ), result)
    }

    @Test
    fun `multiply incompatible dimensions returns error`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0, 3.0))
        val b = Matrix.of(doubleArrayOf(1.0, 2.0))
        assertError(MatrixOperations.multiply(a, b))
    }

    @Test
    fun `multiply non-square matrices`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))
        val b = Matrix.of(doubleArrayOf(7.0, 8.0), doubleArrayOf(9.0, 10.0), doubleArrayOf(11.0, 12.0))
        val result = assertSuccess(MatrixOperations.multiply(a, b))
        assertMatrixEquals(Matrix.of(doubleArrayOf(58.0, 64.0), doubleArrayOf(139.0, 154.0)), result)
    }

    @Test
    fun `multiply 1x1 matrices`() {
        val a = Matrix.of(doubleArrayOf(3.0))
        val b = Matrix.of(doubleArrayOf(4.0))
        val result = assertSuccess(MatrixOperations.multiply(a, b))
        assertMatrixEquals(Matrix.of(doubleArrayOf(12.0)), result)
    }

    // ========== SCALAR MULTIPLY ==========

    @Test
    fun `scalar multiply`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val result = assertSuccess(MatrixOperations.scalarMultiply(a, 3.0))
        assertMatrixEquals(Matrix.of(doubleArrayOf(3.0, 6.0), doubleArrayOf(9.0, 12.0)), result)
    }

    @Test
    fun `scalar multiply by zero`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val result = assertSuccess(MatrixOperations.scalarMultiply(a, 0.0))
        assertMatrixEquals(Matrix.zero(2, 2), result)
    }

    @Test
    fun `scalar multiply by negative`() {
        val a = Matrix.of(doubleArrayOf(1.0, -2.0), doubleArrayOf(-3.0, 4.0))
        val result = assertSuccess(MatrixOperations.scalarMultiply(a, -1.0))
        assertMatrixEquals(Matrix.of(doubleArrayOf(-1.0, 2.0), doubleArrayOf(3.0, -4.0)), result)
    }

    @Test
    fun `scalar multiply by fractional`() {
        val a = Matrix.of(doubleArrayOf(2.0, 4.0), doubleArrayOf(6.0, 8.0))
        val result = assertSuccess(MatrixOperations.scalarMultiply(a, 0.5))
        assertMatrixEquals(Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0)), result)
    }

    // ========== POWER ==========

    @Test
    fun `power zero returns identity`() {
        val a = Matrix.of(doubleArrayOf(2.0, 3.0), doubleArrayOf(4.0, 5.0))
        val result = assertSuccess(MatrixOperations.power(a, 0))
        assertMatrixEquals(Matrix.identity(2), result)
    }

    @Test
    fun `power one returns same matrix`() {
        val a = Matrix.of(doubleArrayOf(2.0, 3.0), doubleArrayOf(4.0, 5.0))
        val result = assertSuccess(MatrixOperations.power(a, 1))
        assertMatrixEquals(a, result)
    }

    @Test
    fun `power two`() {
        val a = Matrix.of(doubleArrayOf(2.0, 3.0), doubleArrayOf(4.0, 5.0))
        val result = assertSuccess(MatrixOperations.power(a, 2))
        assertMatrixEquals(Matrix.of(doubleArrayOf(16.0, 21.0), doubleArrayOf(28.0, 37.0)), result)
    }

    @Test
    fun `power three`() {
        val a = Matrix.of(doubleArrayOf(2.0, 1.0), doubleArrayOf(0.0, 1.0))
        val result = assertSuccess(MatrixOperations.power(a, 3))
        assertMatrixEquals(Matrix.of(doubleArrayOf(8.0, 7.0), doubleArrayOf(0.0, 1.0)), result)
    }

    @Test
    fun `power non-square returns error`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))
        assertError(MatrixOperations.power(a, 2))
    }

    @Test
    fun `power negative returns error`() {
        val a = Matrix.identity(2)
        assertError(MatrixOperations.power(a, -1))
    }

    @Test
    fun `power 1x1 matrix`() {
        val a = Matrix.of(doubleArrayOf(3.0))
        val result = assertSuccess(MatrixOperations.power(a, 5))
        assertMatrixEquals(Matrix.of(doubleArrayOf(243.0)), result)
    }

    // ========== TRANSPOSE ==========

    @Test
    fun `transpose 2x2`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val result = assertSuccess(MatrixOperations.transpose(a))
        assertMatrixEquals(Matrix.of(doubleArrayOf(1.0, 3.0), doubleArrayOf(2.0, 4.0)), result)
    }

    @Test
    fun `transpose 2x3 to 3x2`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))
        val result = assertSuccess(MatrixOperations.transpose(a))
        assertEquals(3, result.rows)
        assertEquals(2, result.cols)
        assertMatrixEquals(Matrix.of(
            doubleArrayOf(1.0, 4.0),
            doubleArrayOf(2.0, 5.0),
            doubleArrayOf(3.0, 6.0),
        ), result)
    }

    @Test
    fun `transpose of identity is identity`() {
        val i = Matrix.identity(3)
        val result = assertSuccess(MatrixOperations.transpose(i))
        assertMatrixEquals(i, result)
    }

    @Test
    fun `double transpose returns original`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))
        val t = assertSuccess(MatrixOperations.transpose(a))
        val tt = assertSuccess(MatrixOperations.transpose(t))
        assertMatrixEquals(a, tt)
    }

    @Test
    fun `transpose 1x1`() {
        val a = Matrix.of(doubleArrayOf(5.0))
        val result = assertSuccess(MatrixOperations.transpose(a))
        assertMatrixEquals(Matrix.of(doubleArrayOf(5.0)), result)
    }

    // ========== TRACE ==========

    @Test
    fun `trace 2x2`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        val result = MatrixOperations.trace(a)
        assertTrue(result is MathResult.Success)
        assertEquals(5.0, (result as MathResult.Success).value, EPS)
    }

    @Test
    fun `trace 3x3`() {
        val a = Matrix.identity(3)
        val result = MatrixOperations.trace(a)
        assertEquals(3.0, (result as MathResult.Success).value, EPS)
    }

    @Test
    fun `trace non-square returns error`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))
        assertTrue(MatrixOperations.trace(a) is MathResult.Error)
    }

    @Test
    fun `trace 1x1`() {
        val a = Matrix.of(doubleArrayOf(7.0))
        assertEquals(7.0, (MatrixOperations.trace(a) as MathResult.Success).value, EPS)
    }

    // ========== DETERMINANT ==========

    @Test
    fun `determinant 1x1`() {
        val a = Matrix.of(doubleArrayOf(5.0))
        assertEquals(5.0, (MatrixOperations.determinant(a) as MathResult.Success).value, EPS)
    }

    @Test
    fun `determinant 2x2`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        assertEquals(-2.0, (MatrixOperations.determinant(a) as MathResult.Success).value, EPS)
    }

    @Test
    fun `determinant 3x3`() {
        val a = Matrix.of(
            doubleArrayOf(6.0, 1.0, 1.0),
            doubleArrayOf(4.0, -2.0, 5.0),
            doubleArrayOf(2.0, 8.0, 7.0),
        )
        assertEquals(-306.0, (MatrixOperations.determinant(a) as MathResult.Success).value, EPS)
    }

    @Test
    fun `determinant of identity is 1`() {
        assertEquals(1.0, (MatrixOperations.determinant(Matrix.identity(4)) as MathResult.Success).value, EPS)
    }

    @Test
    fun `determinant of singular matrix is 0`() {
        val a = Matrix.of(
            doubleArrayOf(1.0, 2.0),
            doubleArrayOf(2.0, 4.0),
        )
        assertEquals(0.0, (MatrixOperations.determinant(a) as MathResult.Success).value, EPS)
    }

    @Test
    fun `determinant non-square returns error`() {
        assertTrue(MatrixOperations.determinant(Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))) is MathResult.Error)
    }

    @Test
    fun `determinant 4x4`() {
        val a = Matrix.of(
            doubleArrayOf(1.0, 0.0, 2.0, -1.0),
            doubleArrayOf(3.0, 0.0, 0.0, 5.0),
            doubleArrayOf(2.0, 1.0, 4.0, -3.0),
            doubleArrayOf(1.0, 0.0, 5.0, 0.0),
        )
        assertEquals(30.0, (MatrixOperations.determinant(a) as MathResult.Success).value, EPS)
    }

    // ========== INVERSE ==========

    @Test
    fun `inverse 2x2`() {
        val a = Matrix.of(doubleArrayOf(4.0, 7.0), doubleArrayOf(2.0, 6.0))
        val inv = assertSuccess(MatrixOperations.inverse(a))
        val product = assertSuccess(MatrixOperations.multiply(a, inv))
        assertMatrixEquals(Matrix.identity(2), product)
    }

    @Test
    fun `inverse of identity is identity`() {
        val inv = assertSuccess(MatrixOperations.inverse(Matrix.identity(3)))
        assertMatrixEquals(Matrix.identity(3), inv)
    }

    @Test
    fun `inverse of singular returns error`() {
        val a = Matrix.of(
            doubleArrayOf(1.0, 2.0),
            doubleArrayOf(2.0, 4.0),
        )
        assertTrue(MatrixOperations.inverse(a) is MathResult.Error)
    }

    @Test
    fun `inverse non-square returns error`() {
        assertTrue(MatrixOperations.inverse(Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))) is MathResult.Error)
    }

    @Test
    fun `inverse 3x3`() {
        val a = Matrix.of(
            doubleArrayOf(2.0, 1.0, 1.0),
            doubleArrayOf(1.0, 3.0, 2.0),
            doubleArrayOf(1.0, 0.0, 0.0),
        )
        val inv = assertSuccess(MatrixOperations.inverse(a))
        val product = assertSuccess(MatrixOperations.multiply(a, inv))
        assertMatrixEquals(Matrix.identity(3), product)
    }

    @Test
    fun `inverse 1x1`() {
        val a = Matrix.of(doubleArrayOf(4.0))
        val inv = assertSuccess(MatrixOperations.inverse(a))
        assertMatrixEquals(Matrix.of(doubleArrayOf(0.25)), inv)
    }

    // ========== RANK ==========

    @Test
    fun `rank identity 3x3`() {
        assertEquals(3, (MatrixOperations.rank(Matrix.identity(3)) as MathResult.Success).value)
    }

    @Test
    fun `rank zero matrix`() {
        assertEquals(0, (MatrixOperations.rank(Matrix.zero(3, 3)) as MathResult.Success).value)
    }

    @Test
    fun `rank 2x2 with dependent rows`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(2.0, 4.0))
        assertEquals(1, (MatrixOperations.rank(a) as MathResult.Success).value)
    }

    @Test
    fun `rank 1x1 nonzero`() {
        assertEquals(1, (MatrixOperations.rank(Matrix.of(doubleArrayOf(5.0))) as MathResult.Success).value)
    }

    @Test
    fun `rank 1x1 zero`() {
        assertEquals(0, (MatrixOperations.rank(Matrix.of(doubleArrayOf(0.0))) as MathResult.Success).value)
    }

    @Test
    fun `rank 3x3 with rank 2`() {
        val a = Matrix.of(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
            doubleArrayOf(7.0, 8.0, 9.0),
        )
        assertEquals(2, (MatrixOperations.rank(a) as MathResult.Success).value)
    }

    // ========== REF ==========

    @Test
    fun `ref 2x2 identity`() {
        val result = assertSuccess(MatrixOperations.ref(Matrix.identity(2)))
        assertMatrixEquals(Matrix.identity(2), result)
    }

    @Test
    fun `ref 3x3`() {
        val a = Matrix.of(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
            doubleArrayOf(7.0, 8.0, 9.0),
        )
        val result = assertSuccess(MatrixOperations.ref(a))
        assertEquals(3, result.rows)
        assertEquals(3, result.cols)
        assertTrue(MathUtils.isZero(result[2, 0]))
        assertTrue(MathUtils.isZero(result[2, 1]))
        assertTrue(MathUtils.isZero(result[2, 2]))
    }

    @Test
    fun `ref already in echelon form`() {
        val a = Matrix.of(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(0.0, 1.0, 4.0),
            doubleArrayOf(0.0, 0.0, 1.0),
        )
        val result = assertSuccess(MatrixOperations.ref(a))
        assertMatrixEquals(a, result)
    }

    // ========== RREF ==========

    @Test
    fun `rref identity`() {
        val result = assertSuccess(MatrixOperations.rref(Matrix.identity(3)))
        assertMatrixEquals(Matrix.identity(3), result)
    }

    @Test
    fun `rref 3x3 rank 2`() {
        val a = Matrix.of(
            doubleArrayOf(1.0, 2.0, 3.0),
            doubleArrayOf(4.0, 5.0, 6.0),
            doubleArrayOf(7.0, 8.0, 9.0),
        )
        val result = assertSuccess(MatrixOperations.rref(a))
        assertEquals(1.0, result[0, 0], EPS)
        assertTrue(MathUtils.isZero(result[0, 1]))
        assertEquals(1.0, result[1, 1], EPS)
        assertTrue(MathUtils.isZero(result[2, 0]))
        assertTrue(MathUtils.isZero(result[2, 1]))
        assertTrue(MathUtils.isZero(result[2, 2]))
    }

    @Test
    fun `rref zero matrix`() {
        val result = assertSuccess(MatrixOperations.rref(Matrix.zero(3, 3)))
        assertMatrixEquals(Matrix.zero(3, 3), result)
    }

    // ========== GAUSSIAN ELIMINATION ==========

    @Test
    fun `gaussian elimination same as ref`() {
        val a = Matrix.of(
            doubleArrayOf(2.0, 1.0, -1.0),
            doubleArrayOf(-3.0, -1.0, 2.0),
            doubleArrayOf(-2.0, 1.0, 2.0),
        )
        val ref = assertSuccess(MatrixOperations.gaussianElimination(a))
        val ref2 = assertSuccess(MatrixOperations.ref(a))
        assertMatrixEquals(ref2, ref)
    }

    // ========== GAUSS-JORDAN ELIMINATION ==========

    @Test
    fun `gauss-jordan same as rref`() {
        val a = Matrix.of(
            doubleArrayOf(2.0, 1.0, -1.0),
            doubleArrayOf(-3.0, -1.0, 2.0),
            doubleArrayOf(-2.0, 1.0, 2.0),
        )
        val gj = assertSuccess(MatrixOperations.gaussJordanElimination(a))
        val rref = assertSuccess(MatrixOperations.rref(a))
        assertMatrixEquals(rref, gj)
    }

    // ========== SYMMETRIC ==========

    @Test
    fun `symmetric 2x2`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(2.0, 3.0))
        assertTrue((MatrixOperations.isSymmetric(a) as MathResult.Success).value)
    }

    @Test
    fun `not symmetric 2x2`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        assertFalse((MatrixOperations.isSymmetric(a) as MathResult.Success).value)
    }

    @Test
    fun `non-square not symmetric`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))
        assertFalse((MatrixOperations.isSymmetric(a) as MathResult.Success).value)
    }

    @Test
    fun `identity is symmetric`() {
        assertTrue((MatrixOperations.isSymmetric(Matrix.identity(3)) as MathResult.Success).value)
    }

    // ========== SKEW-SYMMETRIC ==========

    @Test
    fun `skew-symmetric 2x2`() {
        val a = Matrix.of(doubleArrayOf(0.0, 2.0), doubleArrayOf(-2.0, 0.0))
        assertTrue((MatrixOperations.isSkewSymmetric(a) as MathResult.Success).value)
    }

    @Test
    fun `not skew-symmetric nonzero diagonal`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(-2.0, 0.0))
        assertFalse((MatrixOperations.isSkewSymmetric(a) as MathResult.Success).value)
    }

    @Test
    fun `skew-symmetric 3x3`() {
        val a = Matrix.of(
            doubleArrayOf(0.0, 2.0, -3.0),
            doubleArrayOf(-2.0, 0.0, 1.0),
            doubleArrayOf(3.0, -1.0, 0.0),
        )
        assertTrue((MatrixOperations.isSkewSymmetric(a) as MathResult.Success).value)
    }

    @Test
    fun `non-square not skew-symmetric`() {
        assertFalse((MatrixOperations.isSkewSymmetric(Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))) as MathResult.Success).value)
    }

    // ========== ORTHOGONAL ==========

    @Test
    fun `identity is orthogonal`() {
        assertTrue((MatrixOperations.isOrthogonal(Matrix.identity(3)) as MathResult.Success).value)
    }

    @Test
    fun `rotation matrix is orthogonal`() {
        val angle = Math.PI / 4
        val a = Matrix.of(
            doubleArrayOf(Math.cos(angle), -Math.sin(angle)),
            doubleArrayOf(Math.sin(angle), Math.cos(angle)),
        )
        assertTrue((MatrixOperations.isOrthogonal(a) as MathResult.Success).value)
    }

    @Test
    fun `non-orthogonal matrix`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        assertFalse((MatrixOperations.isOrthogonal(a) as MathResult.Success).value)
    }

    @Test
    fun `non-square not orthogonal`() {
        assertFalse((MatrixOperations.isOrthogonal(Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))) as MathResult.Success).value)
    }

    // ========== SINGULAR ==========

    @Test
    fun `singular matrix`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(2.0, 4.0))
        assertTrue((MatrixOperations.isSingular(a) as MathResult.Success).value)
    }

    @Test
    fun `non-singular matrix`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        assertFalse((MatrixOperations.isSingular(a) as MathResult.Success).value)
    }

    @Test
    fun `singular non-square returns error`() {
        assertTrue(MatrixOperations.isSingular(Matrix.of(doubleArrayOf(1.0, 2.0, 3.0), doubleArrayOf(4.0, 5.0, 6.0))) is MathResult.Error)
    }

    // ========== POSITIVE DEFINITE ==========

    @Test
    fun `positive definite 2x2`() {
        val a = Matrix.of(doubleArrayOf(2.0, 1.0), doubleArrayOf(1.0, 3.0))
        assertTrue((MatrixOperations.isPositiveDefinite(a) as MathResult.Success).value)
    }

    @Test
    fun `identity is positive definite`() {
        assertTrue((MatrixOperations.isPositiveDefinite(Matrix.identity(3)) as MathResult.Success).value)
    }

    @Test
    fun `not positive definite - negative diagonal`() {
        val a = Matrix.of(doubleArrayOf(-1.0, 0.0), doubleArrayOf(0.0, -1.0))
        assertFalse((MatrixOperations.isPositiveDefinite(a) as MathResult.Success).value)
    }

    @Test
    fun `non-symmetric not positive definite`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        assertFalse((MatrixOperations.isPositiveDefinite(a) as MathResult.Success).value)
    }

    @Test
    fun `positive semi-definite not positive definite`() {
        val a = Matrix.of(doubleArrayOf(1.0, -1.0), doubleArrayOf(-1.0, 1.0))
        assertFalse((MatrixOperations.isPositiveDefinite(a) as MathResult.Success).value)
    }

    // ========== NEGATIVE DEFINITE ==========

    @Test
    fun `negative definite 2x2`() {
        val a = Matrix.of(doubleArrayOf(-2.0, 0.0), doubleArrayOf(0.0, -3.0))
        assertTrue((MatrixOperations.isNegativeDefinite(a) as MathResult.Success).value)
    }

    @Test
    fun `negative definite 3x3`() {
        val a = Matrix.of(
            doubleArrayOf(-4.0, 2.0, -1.0),
            doubleArrayOf(2.0, -5.0, 3.0),
            doubleArrayOf(-1.0, 3.0, -6.0),
        )
        assertTrue((MatrixOperations.isNegativeDefinite(a) as MathResult.Success).value)
    }

    @Test
    fun `not negative definite - positive values`() {
        val a = Matrix.of(doubleArrayOf(1.0, 0.0), doubleArrayOf(0.0, 1.0))
        assertFalse((MatrixOperations.isNegativeDefinite(a) as MathResult.Success).value)
    }

    @Test
    fun `non-symmetric not negative definite`() {
        val a = Matrix.of(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        assertFalse((MatrixOperations.isNegativeDefinite(a) as MathResult.Success).value)
    }

    // ========== FLOATING POINT EDGE CASES ==========

    @Test
    fun `floating point addition handles near-zero correctly`() {
        val a = Matrix.of(doubleArrayOf(0.1, 0.2), doubleArrayOf(0.3, 0.0))
        val b = Matrix.of(doubleArrayOf(0.0, 0.1 + 0.2), doubleArrayOf(0.3, 0.0))
        val result = assertSuccess(MatrixOperations.add(a, b))
        assertTrue(MathUtils.eq(result[0, 1], 0.2 + (0.1 + 0.2)))
    }

    @Test
    fun `large values`() {
        val a = Matrix.of(doubleArrayOf(1e8, 2e8), doubleArrayOf(3e8, 4e8))
        val b = Matrix.of(doubleArrayOf(1e8, 2e8), doubleArrayOf(3e8, 4e8))
        val result = assertSuccess(MatrixOperations.multiply(a, b))
        assertEquals(1e16 + 6e16, result[0, 0], 1e8)
    }
}
