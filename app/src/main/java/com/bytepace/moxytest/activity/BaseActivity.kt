package com.bytepace.moxytest.activity

import android.app.Activity
import android.support.annotation.IdRes
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity

abstract class BaseActivity : MvpAppCompatActivity() {
    fun <T : View> Activity.bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy { findViewById<T>(res) }
    }
}