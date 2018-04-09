package com.bytepace.moxytest.activity

import android.os.Bundle
import android.widget.TextView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.bytepace.moxytest.R
import com.bytepace.moxytest.presenter.HighscorePresenter
import com.bytepace.moxytest.view.HighscoreView

class JengaActivity : BaseActivity(), HighscoreView {
    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var highscorePresenter: HighscorePresenter

    private val dailyBest: TextView by bind(R.id.daily_best)
    private val weeklyBest: TextView by bind(R.id.weekly_best)
    private val allTimeBest: TextView by bind(R.id.all_time_best)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)
        highscorePresenter.setContext(this)
    }

    override fun setDailyHighscore(score: Int) {
        dailyBest.text = "$score"
    }

    override fun setWeeklyHighscore(score: Int) {
        weeklyBest.text = "$score"
    }

    override fun setAllTimeHighscore(score: Int) {
        allTimeBest.text = "$score"
    }
}