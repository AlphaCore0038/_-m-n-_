package com.calc.matrixcalculator.math

import org.junit.Assert.*
import org.junit.Test
import kotlin.math.PI

class VectorOperationsTest {

    private val EPS = 1e-6

    // ========== ADDITION ==========

    @Test
    fun `add 2D vectors`() {
        val a = Vector.of(1.0, 2.0)
        val b = Vector.of(3.0, 4.0)
        val result = VectorOperations.add(a, b)
        assertTrue(result is MathResult.Success)
        assertEquals(Vector.of(4.0, 6.0), (result as MathResult.Success).value)
    }

    @Test
    fun `add 3D vectors`() {
        val a = Vector.of(1.0, 2.0, 3.0)
        val b = Vector.of(4.0, 5.0, 6.0)
        val result = VectorOperations.add(a, b)
        assertTrue(result is MathResult.Success)
        assertEquals(Vector.of(5.0, 7.0, 9.0), (result as MathResult.Success).value)
    }

    @Test
    fun `add with negative values`() {
        val a = Vector.of(1.0, -2.0)
        val b = Vector.of(-1.0, 2.0)
        val result = VectorOperations.add(a, b)
        assertEquals(Vector.zero(2), (result as MathResult.Success).value)
    }

    @Test
    fun `add mismatched dimensions returns error`() {
        val a = Vector.of(1.0, 2.0)
        val b = Vector.of(1.0, 2.0, 3.0)
        assertTrue(VectorOperations.add(a, b) is MathResult.Error)
    }

    // ========== SUBTRACTION ==========

    @Test
    fun `subtract 2D vectors`() {
        val a = Vector.of(5.0, 6.0)
        val b = Vector.of(3.0, 4.0)
        val result = VectorOperations.subtract(a, b)
        assertEquals(Vector.of(2.0, 2.0), (result as MathResult.Success).value)
    }

    @Test
    fun `subtract same vector returns zero`() {
        val a = Vector.of(1.0, 2.0, 3.0)
        val result = VectorOperations.subtract(a, a)
        assertEquals(Vector.zero(3), (result as MathResult.Success).value)
    }

    @Test
    fun `subtract mismatched dimensions returns error`() {
        assertTrue(VectorOperations.subtract(Vector.of(1.0), Vector.of(1.0, 2.0)) is MathResult.Error)
    }

    // ========== SCALAR MULTIPLY ==========

    @Test
    fun `scalar multiply`() {
        val v = Vector.of(1.0, 2.0, 3.0)
        val result = VectorOperations.scalarMultiply(v, 3.0)
        assertEquals(Vector.of(3.0, 6.0, 9.0), (result as MathResult.Success).value)
    }

    @Test
    fun `scalar multiply by zero`() {
        val v = Vector.of(1.0, 2.0, 3.0)
        val result = VectorOperations.scalarMultiply(v, 0.0)
        assertEquals(Vector.zero(3), (result as MathResult.Success).value)
    }

    @Test
    fun `scalar multiply by negative`() {
        val v = Vector.of(1.0, -2.0)
        val result = VectorOperations.scalarMultiply(v, -1.0)
        assertEquals(Vector.of(-1.0, 2.0), (result as MathResult.Success).value)
    }

    // ========== DOT PRODUCT ==========

    @Test
    fun `dot product 3D`() {
        val a = Vector.of(1.0, 2.0, 3.0)
        val b = Vector.of(4.0, 5.0, 6.0)
        val result = VectorOperations.dotProduct(a, b)
        assertEquals(32.0, (result as MathResult.Success).value, EPS)
    }

    @Test
    fun `dot product orthogonal vectors is zero`() {
        val a = Vector.of(1.0, 0.0, 0.0)
        val b = Vector.of(0.0, 1.0, 0.0)
        assertEquals(0.0, (VectorOperations.dotProduct(a, b) as MathResult.Success).value, EPS)
    }

    @Test
    fun `dot product with itself equals magnitude squared`() {
        val v = Vector.of(3.0, 4.0)
        val dotResult = (VectorOperations.dotProduct(v, v) as MathResult.Success).value
        assertEquals(25.0, dotResult, EPS)
    }

    @Test
    fun `dot product mismatched dimensions returns error`() {
        assertTrue(VectorOperations.dotProduct(Vector.of(1.0), Vector.of(1.0, 2.0)) is MathResult.Error)
    }

    // ========== CROSS PRODUCT ==========

    @Test
    fun `cross product standard`() {
        val a = Vector.of(1.0, 0.0, 0.0)
        val b = Vector.of(0.0, 1.0, 0.0)
        val result = (VectorOperations.crossProduct(a, b) as MathResult.Success).value
        assertEquals(Vector.of(0.0, 0.0, 1.0), result)
    }

    @Test
    fun `cross product anti-commutative`() {
        val a = Vector.of(1.0, 2.0, 3.0)
        val b = Vector.of(4.0, 5.0, 6.0)
        val ab = (VectorOperations.crossProduct(a, b) as MathResult.Success).value
        val ba = (VectorOperations.crossProduct(b, a) as MathResult.Success).value
        val sum = (VectorOperations.add(ab, ba) as MathResult.Success).value
        assertEquals(Vector.zero(3), sum)
    }

    @Test
    fun `cross product parallel vectors is zero`() {
        val a = Vector.of(1.0, 2.0, 3.0)
        val b = Vector.of(2.0, 4.0, 6.0)
        val result = (VectorOperations.crossProduct(a, b) as MathResult.Success).value
        assertEquals(Vector.zero(3), result)
    }

    @Test
    fun `cross product non-3D returns error`() {
        assertTrue(VectorOperations.crossProduct(Vector.of(1.0, 2.0), Vector.of(3.0, 4.0)) is MathResult.Error)
        assertTrue(VectorOperations.crossProduct(Vector.of(1.0, 2.0, 3.0, 4.0), Vector.of(1.0, 2.0, 3.0, 4.0)) is MathResult.Error)
    }

    // ========== MAGNITUDE ==========

    @Test
    fun `magnitude 3D`() {
        val v = Vector.of(3.0, 4.0, 0.0)
        assertEquals(5.0, (VectorOperations.magnitude(v) as MathResult.Success).value, EPS)
    }

    @Test
    fun `magnitude 1D`() {
        val v = Vector.of(-5.0)
        assertEquals(5.0, (VectorOperations.magnitude(v) as MathResult.Success).value, EPS)
    }

    @Test
    fun `magnitude zero vector`() {
        assertEquals(0.0, (VectorOperations.magnitude(Vector.zero(3)) as MathResult.Success).value, EPS)
    }

    @Test
    fun `magnitude unit vectors`() {
        assertEquals(1.0, (VectorOperations.magnitude(Vector.of(1.0, 0.0, 0.0)) as MathResult.Success).value, EPS)
        assertEquals(1.0, (VectorOperations.magnitude(Vector.of(0.0, 1.0, 0.0)) as MathResult.Success).value, EPS)
    }

    // ========== UNIT VECTOR ==========

    @Test
    fun `unit vector`() {
        val v = Vector.of(3.0, 4.0, 0.0)
        val result = (VectorOperations.unitVector(v) as MathResult.Success).value
        assertEquals(Vector.of(0.6, 0.8, 0.0), result)
    }

    @Test
    fun `unit vector already unit`() {
        val v = Vector.of(1.0, 0.0, 0.0)
        val result = (VectorOperations.unitVector(v) as MathResult.Success).value
        assertEquals(v, result)
    }

    @Test
    fun `unit vector zero vector returns error`() {
        assertTrue(VectorOperations.unitVector(Vector.zero(3)) is MathResult.Error)
    }

    @Test
    fun `unit vector magnitude is 1`() {
        val v = Vector.of(1.0, 2.0, 3.0)
        val unit = (VectorOperations.unitVector(v) as MathResult.Success).value
        assertEquals(1.0, (VectorOperations.magnitude(unit) as MathResult.Success).value, EPS)
    }

    // ========== ANGLE ==========

    @Test
    fun `angle between parallel vectors is 0`() {
        val a = Vector.of(1.0, 0.0, 0.0)
        val b = Vector.of(2.0, 0.0, 0.0)
        assertEquals(0.0, (VectorOperations.angle(a, b) as MathResult.Success).value, EPS)
    }

    @Test
    fun `angle between perpendicular vectors is pi 2`() {
        val a = Vector.of(1.0, 0.0, 0.0)
        val b = Vector.of(0.0, 1.0, 0.0)
        assertEquals(PI / 2, (VectorOperations.angle(a, b) as MathResult.Success).value, EPS)
    }

    @Test
    fun `angle between opposite vectors is pi`() {
        val a = Vector.of(1.0, 0.0, 0.0)
        val b = Vector.of(-1.0, 0.0, 0.0)
        assertEquals(PI, (VectorOperations.angle(a, b) as MathResult.Success).value, EPS)
    }

    @Test
    fun `angle zero vector returns error`() {
        assertTrue(VectorOperations.angle(Vector.zero(3), Vector.of(1.0, 0.0, 0.0)) is MathResult.Error)
    }

    @Test
    fun `angle mismatched dimensions returns error`() {
        assertTrue(VectorOperations.angle(Vector.of(1.0, 0.0), Vector.of(1.0, 0.0, 0.0)) is MathResult.Error)
    }

    // ========== PROJECTION ==========

    @Test
    fun `projection of parallel vector`() {
        val a = Vector.of(3.0, 4.0, 0.0)
        val b = Vector.of(1.0, 0.0, 0.0)
        val result = (VectorOperations.projection(a, b) as MathResult.Success).value
        assertEquals(Vector.of(3.0, 0.0, 0.0), result)
    }

    @Test
    fun `projection of perpendicular vector is zero`() {
        val a = Vector.of(0.0, 1.0, 0.0)
        val b = Vector.of(1.0, 0.0, 0.0)
        val result = (VectorOperations.projection(a, b) as MathResult.Success).value
        assertEquals(Vector.zero(3), result)
    }

    @Test
    fun `projection onto zero vector returns error`() {
        assertTrue(VectorOperations.projection(Vector.of(1.0, 2.0), Vector.zero(2)) is MathResult.Error)
    }

    @Test
    fun `projection mismatched dimensions returns error`() {
        assertTrue(VectorOperations.projection(Vector.of(1.0), Vector.of(1.0, 2.0)) is MathResult.Error)
    }

    // ========== PARALLEL ==========

    @Test
    fun `parallel vectors detected`() {
        val a = Vector.of(1.0, 2.0, 3.0)
        val b = Vector.of(2.0, 4.0, 6.0)
        assertTrue((VectorOperations.isParallel(a, b) as MathResult.Success).value)
    }

    @Test
    fun `parallel same direction`() {
        val a = Vector.of(1.0, 2.0, 3.0)
        val b = Vector.of(1.0, 2.0, 3.0)
        assertTrue((VectorOperations.isParallel(a, b) as MathResult.Success).value)
    }

    @Test
    fun `parallel opposite direction`() {
        val a = Vector.of(1.0, 2.0, 3.0)
        val b = Vector.of(-1.0, -2.0, -3.0)
        assertTrue((VectorOperations.isParallel(a, b) as MathResult.Success).value)
    }

    @Test
    fun `non-parallel vectors`() {
        val a = Vector.of(1.0, 0.0, 0.0)
        val b = Vector.of(0.0, 1.0, 0.0)
        assertFalse((VectorOperations.isParallel(a, b) as MathResult.Success).value)
    }

    @Test
    fun `zero vector is parallel to everything`() {
        val a = Vector.zero(3)
        val b = Vector.of(1.0, 2.0, 3.0)
        assertTrue((VectorOperations.isParallel(a, b) as MathResult.Success).value)
    }

    @Test
    fun `parallel 2D vectors via proportionality`() {
        val a = Vector.of(2.0, 4.0)
        val b = Vector.of(1.0, 2.0)
        assertTrue((VectorOperations.isParallel(a, b) as MathResult.Success).value)
    }

    @Test
    fun `non-parallel 2D vectors`() {
        val a = Vector.of(1.0, 2.0)
        val b = Vector.of(3.0, 4.0)
        assertFalse((VectorOperations.isParallel(a, b) as MathResult.Success).value)
    }

    @Test
    fun `parallel mismatched dimensions returns error`() {
        assertTrue(VectorOperations.isParallel(Vector.of(1.0), Vector.of(1.0, 2.0)) is MathResult.Error)
    }

    // ========== PERPENDICULAR ==========

    @Test
    fun `perpendicular vectors`() {
        val a = Vector.of(1.0, 0.0)
        val b = Vector.of(0.0, 1.0)
        assertTrue((VectorOperations.isPerpendicular(a, b) as MathResult.Success).value)
    }

    @Test
    fun `non-perpendicular vectors`() {
        val a = Vector.of(1.0, 0.0)
        val b = Vector.of(1.0, 1.0)
        assertFalse((VectorOperations.isPerpendicular(a, b) as MathResult.Success).value)
    }

    @Test
    fun `perpendicular 3D vectors`() {
        val a = Vector.of(1.0, 2.0, 3.0)
        val b = Vector.of(2.0, -1.0, 0.0)
        assertTrue((VectorOperations.isPerpendicular(a, b) as MathResult.Success).value)
    }

    @Test
    fun `perpendicular mismatched dimensions returns error`() {
        assertTrue(VectorOperations.isPerpendicular(Vector.of(1.0), Vector.of(1.0, 2.0)) is MathResult.Error)
    }
}
