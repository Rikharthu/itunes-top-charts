package com.example.rikharthu.itunestopcharts.ui

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import kotlin.math.round

class OverlayFadeDrawable(layers: Array<out Drawable>?) : LayerDrawable(layers) {

    override fun onLevelChange(level: Int): Boolean {
        // Force a redraw when the level value is externally changed
        invalidateSelf()
        return true
    }

    override fun draw(canvas: Canvas?) {
        val base = getDrawable(0)
        val overlay = getDrawable(1)
        // Get the level as percentage of the maximum value
        val percent = level / 10000f
        val alpha = round(percent * 0xff).toInt()

        // Optimize for end-cases to avoid overdraw
        when (alpha) {
            255 -> {
                overlay.draw(canvas)
            }
            0 -> {
                base.draw(canvas)
            }
            else -> {
                // Draw composite if in-between
                base.draw(canvas)

                overlay.alpha = alpha
                overlay.draw(canvas)
                overlay.alpha = 0xFF
            }
        }
    }
}