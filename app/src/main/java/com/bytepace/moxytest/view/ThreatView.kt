package com.bytepace.moxytest.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ThreatView : MvpView {
    fun showThreat()
    fun removeThreat()
    fun hideThreat()
}