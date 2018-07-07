package com.example.rikharthu.itunestopcharts

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.support.v7.app.AppCompatActivity
import com.example.rikharthu.itunestopcharts.ui.OverlayFadeDrawable
import kotlinx.android.synthetic.main.activity_main.*

class TopChartsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val original = BitmapFactory.decodeResource(resources, R.drawable.lenna)
        val blurry = original.copy(original.config, true)

        // Create RenderScript context
        val rs = RenderScript.create(this)
        val input = Allocation.createFromBitmap(rs, original,
                Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT)
        val output = Allocation.createTyped(rs, input.type)
        // Run a blur at the maximum supported radius (25f)
        ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)).apply {
            setRadius(25f)
            setInput(input)
            forEach(output)
        }
        output.copyTo(blurry)
        rs.destroy()

        val drawable = OverlayFadeDrawable(arrayOf(BitmapDrawable(resources, original),
                BitmapDrawable(resources, blurry)))
        backgroundFade.setImageDrawable(drawable)
        backgroundFade.setImageLevel(7500)
    }
}
