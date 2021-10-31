package org.scoutant.rpn

import android.util.Log
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CalculatorTest {
    var s: Calculator = Calculator()
    @Test
    fun testToString() {
        assertTrue(s.isEmpty())
        s.push(305)
        assertFalse(s.isEmpty())
        assertTrue(s.size() == 1)
        Log.d("calc", "stack is : $s")
        assertEquals("305", s.toString())
    }

    @Test
    fun assertCalc(value: Double) {
        assertEquals(value, s.toString())
    }

    @Test
    fun assertCalc(value: Int) {
        assertCalc(value.toDouble())
    }

    @Test
    fun testAdd() {
        s.push(30.0)
        assertCalc(30)
        s.push(1.15)
        s.add()
        assertCalc(31.15)
    }

    @Test
    fun testPushAndDrop() {
        s.push(78704)
        s.push(42)
        s.drop()
        assertEquals("Drop failed", "78704.00", s.toString())
        s.drop()
        assertEquals("Double drop failed to empty", "", s.toString())
        s.drop()
        assertEquals("Drop on empty stack failed", "", s.toString())
    }
}