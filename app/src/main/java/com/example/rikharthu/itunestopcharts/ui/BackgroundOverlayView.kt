package com.example.rikharthu.itunestopcharts.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ImageView

class BackgroundOverlayView : ImageView {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var base: Bitmap? = null
    private var overlay: Bitmap? = null
    private var clipOffset: Int = 0

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun setImagePair(base: Bitmap, overlay: Bitmap) {
        this.base = base
        this.overlay = overlay
        setImageBitmap(base)
    }

    /**
     * Adjust the vertical point where the normal and blurred copy should switch
     */
    fun setOverlayOffset(overlayOffset: Int) {
        clipOffset = overlayOffset
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        // Draw base image first, clipped to the top section
        // We clip the base image to avoid unnecessary overdraw in
        // the bottom section of the view
        canvas.save()
        canvas.clipRect(left, top, right, clipOffset)
        super.onDraw(canvas)
        canvas.restore()

        // Obtain the matrix used to scale the base image, and apply it
        // to the blurred overlay iamge so the two match up
        canvas.save()
        canvas.clipRect(left, clipOffset, right, bottom)
        canvas.drawBitmap(overlay, imageMatrix, paint)
        canvas.restore()
    }
}