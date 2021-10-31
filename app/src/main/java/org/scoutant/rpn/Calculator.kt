package org.scoutant.rpn

import org.scoutant.rpn.BigDecimalUtils.approxPow
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/** Simple RPN calculator. Invalid operations (division by zero, sqrt of negative number) with be rejected.  */
class Calculator () {
    val stack: Stack<BigDecimal> = Stack()
    private val PRECISION = 10
    private val INTERNAL_SCALE = 32

    constructor (cache: String) : this() {
        for (item in cache.split(";")) {
            if (item.isEmpty()) continue
            stack.push( BigDecimal( item))
        }
    }

    fun push( value: BigDecimal) {
        stack.push( value)
    }

    fun stack(): Stack<BigDecimal>? {
        if (stack.isEmpty()) return null
        return stack
    }

    fun push( value: String) = push ( BigDecimal( value))

    fun push( value: Int) = push ( BigDecimal( value))
    fun push( value: Double) = push ( BigDecimal( value))


    fun isEmpty(): Boolean = stack.isEmpty()

    fun negate() {
        if (isEmpty()) return
        stack.push( stack.pop().negate())
    }

    fun peek() : BigDecimal = stack.peek()
    fun pop() : BigDecimal = stack.pop()

    fun drop() = stack()?.pop()

    fun dup() = stack()?.push( peek())

    fun size(): Int = stack.size
    fun small(): Boolean = size() <= 1

    fun swap() {
        if ( small()) return
        val x = pop()
        val y = pop()
        push( x)
        push( y)
    }

    fun add() {
        if (small()) return
        push(pop().add( pop()) )
    }

    fun subtract() {
        if (small()) return
        val x = pop()
        push( pop().subtract( x))
    }

    fun multiply() {
        if (small()) return
        val x = pop()
        push( pop().multiply( x))
    }

    fun divide() {
        if (small()) return
        if (BigDecimal.ZERO == peek()) throw ArithmeticException("Division by zero")
        val x = pop()
        push( pop().divide( x, INTERNAL_SCALE, RoundingMode.HALF_EVEN))
    }

    fun power() {
        if (small()) return
        val y = pop()
        val x = pop()
        try {
            val yi = y.intValueExact()
            // try to compute exactly
            push(x.pow(yi))
        } catch ( ae: ArithmeticException) {
            push(approxPow(x, y))
        }
    }

    fun sqrt() {
        if (stack.isEmpty()) return
        if (peek().toDouble()<0) throw ArithmeticException("Sqrt of negative number")
        push( BigDecimalUtils.sqrt( pop(), INTERNAL_SCALE))
    }

    fun reciprocal() {
        if (stack.isEmpty()) return
        if (BigDecimal.ZERO == peek()) throw ArithmeticException("Division by zero")
        push( BigDecimal.ONE.divide( pop(), INTERNAL_SCALE, RoundingMode.HALF_EVEN))
    }

    override fun toString(): String {
        var msg = ""
        for( value in stack.elements()) {
            msg += value
            msg += ";"
        }
        return msg
    }

    val df = DecimalFormat("0.#########", DecimalFormatSymbols(Locale.US))

    fun format( value: BigDecimal) : String = df.format( value.round(MathContext( PRECISION, RoundingMode.HALF_UP)).toDouble())

}