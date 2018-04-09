package com.bytepace.moxytest.presenter

import android.content.Context
import android.os.SystemClock
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.bytepace.moxytest.view.ThreatView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference
import java.util.*

const val THREAT_START = 1
const val THREAT_END = 2
const val THREAT_ALARMED = 3

@InjectViewState
class ThreatPresenter : MvpPresenter<ThreatView>() {

    private lateinit var ctx: WeakReference<Context>
    private lateinit var counterPresenter: CounterPresenter
    private var isInited = false
    private var isThreatNegated = false
    private var threatValue: Int = 0
    private var isPaused = false

    fun setContextAndCounter(ctx: Context, counterPresenter: CounterPresenter) {
        this.ctx = WeakReference(ctx)
        if (!isInited) {
            isInited = true
            this.counterPresenter = counterPresenter
            startThreatCycle()
        }
    }

    private fun startThreatCycle() {
        Observable.create<Int> {
            while (true) {
                waitRandTime()
                if (!isPaused) {
                    threatValue = generateTreatValue()
                    it.onNext(THREAT_START)
                    SystemClock.sleep(1000)
                    if (!isThreatNegated) {
                        it.onNext(THREAT_ALARMED)
                    } else {
                        it.onNext(THREAT_END)
                    }
                }
            }
        }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        THREAT_START -> viewState.showThreat()
                        THREAT_ALARMED -> {
                            counterPresenter.alterCount(-threatValue)
                            threatValue = 0
                            viewState.hideThreat()
                            isThreatNegated = false
                        }
                        THREAT_END -> {
                            viewState.removeThreat()
                            isThreatNegated = false
                        }
                    }
                })
    }

    fun removeThreat() {
        isThreatNegated = true
        viewState.removeThreat()
    }

    private fun waitRandTime(){
        val rand = Random(System.currentTimeMillis())
        SystemClock.sleep(1000 + Math.abs(rand.nextLong()) % 5000)
    }

    private fun generateTreatValue(): Int {
        val rand = Random(System.currentTimeMillis())
        val currentValue = counterPresenter.getCount()
        return currentValue / 3 + Math.abs(rand.nextInt()) % (currentValue / 2 + 1)
    }

    fun pause() {
        isPaused = true
    }

    fun unPause() {
        isPaused = false
    }
}