package com.bytepace.moxytest.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.bytepace.moxytest.R
import com.bytepace.moxytest.presenter.CounterPresenter
import com.bytepace.moxytest.presenter.HighscorePresenter
import com.bytepace.moxytest.presenter.ThreatPresenter
import com.bytepace.moxytest.utils.AnimUtils
import com.bytepace.moxytest.utils.GooseCreator
import com.bytepace.moxytest.view.CounterView
import com.bytepace.moxytest.view.ThreatView


class MainActivity : BaseActivity(), CounterView, ThreatView {

    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var counterPresenter: CounterPresenter
    @InjectPresenter(type = PresenterType.GLOBAL)
    lateinit var threatPresenter: ThreatPresenter

    private val counterText: TextView by bind(R.id.text_counter)
    private val relativeLayout: RelativeLayout by bind(R.id.view)
    private var goose: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        counterText.text = "У тебя ноль карасей"
        counterPresenter.setContext(this)
        threatPresenter.setContextAndCounter(this, counterPresenter)
        counterText.setOnClickListener { startActivity(Intent(this, JengaActivity::class.java)) }
    }

    override fun onCountChanged(count: Int) {
        counterText.text = getString(R.string.cart_count, count)
        HighscorePresenter.setScore(this, count)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun showThreat() {
        val goose = GooseCreator.addGoose(relativeLayout, windowManager)
        goose.setOnTouchListener({ _, _ ->
            threatPresenter.removeThreat()
            return@setOnTouchListener true
        })

        this.goose = goose
    }

    override fun removeThreat() {
        val goose = goose
        goose ?: return
        val splatter = GooseCreator.splatGoose(relativeLayout, goose)
        this.goose = null
        AnimUtils().animateViewFadeOut(splatter, 1000, {
            relativeLayout.removeView(splatter)
        })
    }

    override fun hideThreat() {
        val goose = goose
        goose ?: return
        relativeLayout.removeView(goose)
        this.goose = null
    }

    override fun onPause() {
        super.onPause()
        counterPresenter.pause()
        threatPresenter.pause()
    }

    override fun onStart() {
        super.onStart()
        counterPresenter.unPause()
        threatPresenter.unPause()
    }
}