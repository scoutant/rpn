package org.scoutant.rpn

import android.content.Context
import androidx.preference.PreferenceManager

class State ( context: Context){
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)
    private val editor = prefs.edit()
    private val CACHE = "stack-cache"

    fun cache (list: String) : State {
        // save only if different so as to preserve true previous state for Undo feature
        if (cache().equals(list)) return this
        return save( CACHE, list)
    }
    fun cache() : String {
        return prefs.getString( CACHE, "") ?: ""
    }

    private fun save( key:String, value: String) : State {
        editor.putString( key, value).apply()
        return this
    }
}