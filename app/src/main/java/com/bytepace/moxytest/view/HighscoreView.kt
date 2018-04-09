package com.bytepace.moxytest.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface HighscoreView: MvpView {
    fun setDailyHighscore(score: Int)
    fun setWeeklyHighscore(score: Int)
    fun setAllTimeHighscore(score: Int)
}