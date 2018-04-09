package com.bytepace.moxytest.utils

import android.content.Context
import android.preference.PreferenceManager
import java.lang.ref.WeakReference

class SPref(private val context: WeakReference<Context>) {

    fun getBoolean(key: String): Boolean {
        if (context.get() != null) {
            return PreferenceManager.getDefaultSharedPreferences(context.get())
                    .getBoolean(key, false)
        }
        return false
    }

    fun set(key: String, value: Boolean) {
        if (context.get() != null) {
            PreferenceManager.getDefaultSharedPreferences(context.get())
                    .edit()
                    .putBoolean(key, value)
                    .apply()
        }
    }

    fun getString(key: String): String {
        if (context.get() != null) {
            return PreferenceManager.getDefaultSharedPreferences(context.get())
                    .getString(key, "")
        }
        return ""
    }

    fun set(key: String, value: String) {
        if (context.get() != null) {
            PreferenceManager.getDefaultSharedPreferences(context.get())
                    .edit()
                    .putString(key, value)
                    .apply()
        }
    }

    fun set(key: String, value: Long) {
        if (context.get() != null) {
            PreferenceManager.getDefaultSharedPreferences(context.get())
                    .edit()
                    .putLong(key, value)
                    .apply()
        }
    }

    fun set(key: String, value: Int) {
        if (context.get() != null) {
            PreferenceManager.getDefaultSharedPreferences(context.get())
                    .edit()
                    .putInt(key, value)
                    .apply()
        }
    }

    fun getInt(key: String): Int {
        if (context.get() != null) {
            return PreferenceManager.getDefaultSharedPreferences(context.get())
                    .getInt(key, 0)
        }
        return 0
    }

    fun getLong(key: String): Long {
        if (context.get() != null) {
            return PreferenceManager.getDefaultSharedPreferences(context.get())
                    .getLong(key, 0)
        }
        return 0
    }
}