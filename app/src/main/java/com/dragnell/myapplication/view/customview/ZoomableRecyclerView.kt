package com.dragnell.myapplication.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ZoomableRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private var scaleFactor = 1.0f
    private val scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleFactor *= detector.scaleFactor
            scaleFactor = scaleFactor.coerceIn(0.5f, 3.0f)

            (layoutManager as? GridLayoutManager)?.let { gridLayoutManager ->
                val minSpan = 2
                val maxSpan = 6
                val spanCount = (maxSpan - (scaleFactor - 1) * (maxSpan - minSpan)).toInt().coerceIn(minSpan, maxSpan)

                if (gridLayoutManager.spanCount != spanCount) {
                    gridLayoutManager.spanCount = spanCount
                    gridLayoutManager.requestLayout()
                }
            }

            return true
        }
    }

    fun getScaleFactor(): Float = scaleFactor
}
