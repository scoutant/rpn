package org.scoutant.rpn


class Buffer {
    private var buffer = StringBuilder( 32)

    fun isEmpty() : Boolean = buffer.isEmpty()
    fun isNotEmpty() = !isEmpty()

    fun reset() = buffer.setLength(0)

    fun append( value : String) = buffer.append( value)

    fun length() : Int = buffer.length

    fun has( value : String) : Boolean = buffer.contains( value)

    fun get() : String = buffer.toString()
    override fun toString(): String = get()

    /** Deletes the rightmost character */
    fun delete() {
        if (isEmpty()) return
        buffer.setLength( length()-1)
    }

    fun dot() {
        if (has( ".")) return
        if (isEmpty()) append( "0")
        append( ".")
    }

}