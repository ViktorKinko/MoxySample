package com.bytepace.moxytest.presenter

import android.content.Context
import android.os.SystemClock
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.bytepace.moxytest.utils.SPref
import com.bytepace.moxytest.view.CounterView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

const val ARG_COUNTER: String = "counter"

@InjectViewState
class CounterPresenter : MvpPresenter<CounterView>() {

    private var isInited = false
    private var count: Int = 0
    private lateinit var ctx: WeakReference<Context>
    private var isPaused = false

    fun setContext(ctx: Context) {
        this.ctx = WeakReference(ctx)
        if (!isInited) {
            isInited = true
            loadCounterValue()
            startCounterCycle()
        }
    }

    private fun loadCounterValue() {
        count = SPref(this.ctx).getInt(ARG_COUNTER)
        onBadgeChange(count)
    }

    private fun startCounterCycle() {
        Observable.create<Int> {
            while (true) {
                addToCounter()
                if (!isPaused) {
                    it.onNext(count)
                }
            }
        }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onBadgeChange(it)
                })
    }

    private fun addToCounter() {
        SystemClock.sleep(1000)
        if (!isPaused) {
            count++
            SPref(this.ctx).set(ARG_COUNTER, count)
        }
    }

    private fun onBadgeChange(counter: Int) {
        viewState.onCountChanged(counter)
    }

    fun getCount(): Int {
        return count
    }

    fun alterCount(int: Int) {
        count += int
        onBadgeChange(count)
    }

    fun pause() {
        isPaused = true
    }

    fun unPause() {
        isPaused = false
    }
}