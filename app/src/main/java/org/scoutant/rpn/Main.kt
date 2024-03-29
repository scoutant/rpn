package org.scoutant.rpn

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

class Main : Activity(), Update {

    private val buffer: Buffer = Buffer()
    private var calculator: Calculator = Calculator()
    private val state : State by lazy { State( this) }

    private var bv: TextView? = null
    private val ids = listOf( R.id.stack0, R.id.stack1, R.id.stack2, R.id.stack3, R.id.stack4, R.id.stack5, R.id.stack6, R.id.stack7 )
    private var svs : Array<TextView?> = arrayOfNulls(ids.size)

    lateinit var vibrator:Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        bv = findViewById<TextView> (R.id.buffer) // buffer view
//        for (i in  0..ids.size-1) svs[i] = this.findViewById( ids[i]) as TextView?
        for (i in  0..ids.size-1) svs[i] = findViewById<TextView>( ids[i])
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator // use VibratorManager with Android 12
    }

    override fun onResume() {
        super.onResume()
        calculator = Calculator( state.cache())
        update()
    }

    private var previous: String = ""

    /** updates the display */
    override fun update() {
//        Log.d("calculator", "calculator stack size :" + calculator.size())
//        Log.d("buffer", "buffer is : " + buffer)
        if (buffer.isEmpty()) bv!!.visibility = View.GONE
        else {
            bv!!.visibility = View.VISIBLE
            bv!!.text = buffer.get()
        }

        val nb = Math.min( ids.size-1, calculator.size()-1)
        for (i in 0..nb) {
            svs[i]?.text = ""+ calculator.format( calculator.stack[calculator.size()-1-i])
        }
        for (i in nb+1..ids.size-1) svs[i]?.text = ""

        previous = state.cache();
        state.cache( calculator.toString())

    }

    /** pushes the buffer onto the stack if not empty */
    fun push() {
        vibrate()
        if (buffer.isEmpty()) return
        calculator.push( buffer.get())
        buffer.reset()
    }

    fun digit(@Suppress("UNUSED_PARAMETER") v: View) {
        val digit:String = v.tag as String
        Log.d("keyboard", "digit : $digit")
        buffer.append( digit)
        vibrate_short()
        update()
    }

    /** validates the buffer or duplicate topmost stack item if empty buffer */
    fun enter(@Suppress("UNUSED_PARAMETER") v: View) {
        if (buffer.isEmpty()) {
            calculator.dup()
            vibrate()
        }
        else push()
        update()
    }

    // 1-operand operations
    fun drop(@Suppress("UNUSED_PARAMETER") v: View)   { push(); calculator.drop(); update(); }
    fun sqrt(@Suppress("UNUSED_PARAMETER") v:View) {
        push()
        try {
            calculator.sqrt()
            update()
        } catch (e: ArithmeticException) {
            toast( "Only for positive numbers.")
        }
    }
    fun negate(@Suppress("UNUSED_PARAMETER") v:View) { push(); calculator.negate(); update(); }
    fun reciprocal(@Suppress("UNUSED_PARAMETER") v:View) {
        push()
        try {
            calculator.reciprocal()
            update()
        } catch (e: ArithmeticException) {
            toast( "Division by zero.")
        }
    }

    // 2-operand operations
    fun swap(@Suppress("UNUSED_PARAMETER") v: View)   { push(); calculator.swap(); update(); }
    fun add(@Suppress("UNUSED_PARAMETER") v: View)    { push(); calculator.add(); update(); }
    fun subtract(@Suppress("UNUSED_PARAMETER") v: View) { push(); calculator.subtract(); update(); }
    fun multiply(@Suppress("UNUSED_PARAMETER") v: View) { push(); calculator.multiply(); update(); }
    fun divide(@Suppress("UNUSED_PARAMETER") v: View) {
        push()
        try {
            calculator.divide()
            update()
        } catch (e: ArithmeticException) {
            toast( "Division by zero.")
        }
    }
    fun power(@Suppress("UNUSED_PARAMETER") v: View) { push(); calculator.power(); update(); }

    // buffer operations
    fun delete(@Suppress("UNUSED_PARAMETER") v: View) {
        if (buffer.isNotEmpty()) vibrate_short()
        buffer.delete()
        update()
    }
    fun dot(@Suppress("UNUSED_PARAMETER") v: View)    { vibrate_short(); buffer.dot(); update(); }

    // other
    fun undo(@Suppress("UNUSED_PARAMETER") v: View) {
        calculator = Calculator( previous)
        vibrate()
        update()
    }

    fun toast(msg:String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG)
            .show()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        this.finish()
        startActivity( Intent( this, this.javaClass))
    }

    fun vibrate_short() {
        vibrator.vibrate( VibrationEffect.createWaveform(longArrayOf(20,20 ), intArrayOf(120, 80), -1))
        // vibrator.vibrate(VibrationEffect.EFFECT_DOUBLE_CLICK) // with Android 10+
    }

    fun vibrate() {
        vibrator.vibrate( VibrationEffect.createWaveform(longArrayOf(60,60 ), intArrayOf(180, 100), -1))
        // vibrator.vibrate(VibrationEffect.EFFECT_DOUBLE_CLICK) // with Android 10+
    }

}