package com.bytepace.moxytest.presenter

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.bytepace.moxytest.utils.SPref
import com.bytepace.moxytest.view.HighscoreView
import org.joda.time.DateTime
import java.lang.ref.WeakReference

const val ARG_HIGHSCORE_DAILY = "highscore_daily"
const val ARG_DAILY_UPDATE_TIME = "daily_update"
const val ARG_HIGHSCORE_WEEKLY = "highscore_weekly"
const val ARG_WEEKLY_UPDATE_TIME = "weekly_update"
const val ARG_HIGHSCORE_ALL_TIME = "all_time_best"

@InjectViewState
class HighscorePresenter : MvpPresenter<HighscoreView>() {

    private lateinit var ctx: WeakReference<Context>
    private var dailyHighscore = 0
    private var weeklyHighscore = 0
    private var allTimeHighscore = 0

    companion object {
        fun setScore(ctx: Context, score: Int) {
            checkScore(ctx, ARG_HIGHSCORE_DAILY, score)
            checkScore(ctx, ARG_HIGHSCORE_WEEKLY, score)
            checkScore(ctx, ARG_HIGHSCORE_ALL_TIME, score)
        }

        private fun checkScore(ctx: Context, key: String, score: Int) {
            if (score > SPref(WeakReference(ctx)).getInt(key)) {
                SPref(WeakReference(ctx)).set(key, score)
            }
        }
    }

    fun setContext(ctx: Context) {
        this.ctx = WeakReference(ctx)
        dailyHighscore = getDailyHighscore()
        weeklyHighscore = getWeeklyHighscore()
        allTimeHighscore = SPref(this.ctx).getInt(ARG_HIGHSCORE_ALL_TIME)
        viewState.setDailyHighscore(dailyHighscore)
        viewState.setWeeklyHighscore(weeklyHighscore)
        viewState.setAllTimeHighscore(allTimeHighscore)
    }

    private fun startOfTheDay(time: Long): DateTime {
        return DateTime(time).withTimeAtStartOfDay()
    }

    private fun getDailyHighscore(): Int {
        val dailyUpdate = SPref(this.ctx).getLong(ARG_DAILY_UPDATE_TIME)
        return if (startOfTheDay(dailyUpdate).millis > startOfTheDay(System.currentTimeMillis()).millis) {
            SPref(this.ctx).getInt(ARG_HIGHSCORE_DAILY)
        } else {
            SPref(this.ctx).set(ARG_DAILY_UPDATE_TIME, startOfTheDay(System.currentTimeMillis()).plusDays(1).millis)
            SPref(this.ctx).set(ARG_HIGHSCORE_DAILY, 0)
            0
        }
    }

    private fun startOfTheWeek(time: Long): DateTime {
        return DateTime(time).withDayOfWeek(1).withTimeAtStartOfDay()
    }

    private fun getWeeklyHighscore(): Int {
        val weeklyUpdate = SPref(this.ctx).getLong(ARG_WEEKLY_UPDATE_TIME)
        return if (startOfTheWeek(weeklyUpdate).millis > startOfTheWeek(System.currentTimeMillis()).millis) {
            SPref(this.ctx).getInt(ARG_HIGHSCORE_WEEKLY)
        } else {
            SPref(this.ctx).set(ARG_WEEKLY_UPDATE_TIME, startOfTheWeek(System.currentTimeMillis()).plusWeeks(1).millis)
            SPref(this.ctx).set(ARG_HIGHSCORE_WEEKLY, 0)
            0
        }
    }
}