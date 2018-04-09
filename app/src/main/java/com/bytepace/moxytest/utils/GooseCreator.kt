package com.bytepace.moxytest.utils

import android.content.Context
import android.graphics.Point
import android.support.v4.app.ActivityCompat
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bytepace.moxytest.R
import java.util.*

class GooseCreator {
    companion object {

        fun addGoose(addHere: RelativeLayout, windowManager: WindowManager): ImageView {
            val screenSize = getScreenSize(windowManager)
            val rand = Random(System.currentTimeMillis())
            val drawableId = getRandomGoose(rand, addHere.context)
            val size = getRandomSize(rand)
            val goose = createGooseImageView(drawableId, size, addHere.context)
            addHere.addView(goose, goose.layoutParams)
            setRandomPosition(goose, size, rand, screenSize)
            return goose
        }

        fun splatGoose(addHere: RelativeLayout, goose: ImageView): ImageView {
            val size = goose.width
            val splatter = ImageView(addHere.context)
            splatter.setImageDrawable(ActivityCompat.getDrawable(addHere.context, R.drawable.splatter))
            splatter.layoutParams = RelativeLayout.LayoutParams(size, size)
            goose.adjustViewBounds = true
            goose.scaleType = ImageView.ScaleType.CENTER_INSIDE
            addHere.addView(splatter)
            addHere.removeView(goose)
            splatter.translationX = goose.translationX
            splatter.translationY = goose.translationY
            splatter.rotation = Random().nextFloat() * 360
            return splatter
        }

        private fun setRandomPosition(goose: ImageView, size: Int, rand: Random, screenSize: Point) {
            goose.translationX = rand.nextFloat() * (screenSize.x - size * 1f)
            goose.translationY = rand.nextFloat() * (screenSize.y - size * 1f)
        }

        private fun createGooseImageView(drawableId: Int, size: Int, ctx: Context): ImageView {
            val goose = ImageView(ctx)
            goose.setImageDrawable(ActivityCompat.getDrawable(ctx, drawableId))
            goose.layoutParams = RelativeLayout.LayoutParams(size, size)
            goose.adjustViewBounds = true
            goose.scaleType = ImageView.ScaleType.CENTER_INSIDE
            return goose
        }

        private fun getRandomGoose(rand: Random, ctx: Context) =
                ctx.resources.getIdentifier("goose" + (Math.abs(rand.nextInt()) % 10), "drawable", ctx.packageName)

        private fun getRandomSize(rand: Random) = 100 + Math.abs(rand.nextInt()) % 500

        private fun getScreenSize(windowManager: WindowManager): Point {
            val display = windowManager.defaultDisplay
            val screenSize = Point()
            display.getSize(screenSize)
            return screenSize
        }
    }
}