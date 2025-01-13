package com.dragnell.myapplication.view.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.dragnell.myapplication.view.PatternListener

class CustomPatternView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val points = mutableListOf<PointF>()

    private val selectedPoints = mutableListOf<Int>()

    private val circlePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth=8f
        isAntiAlias = true
    }

    private val selectedCirclePaint = Paint().apply { // Paint cho các điểm đã chọn
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val linePaint = Paint().apply { // Paint cho các đường nối
        color = Color.WHITE
        strokeWidth = 5f
        isAntiAlias = true
    }

    open var patternListener: PatternListener? = null

    private val radius = 15f

    private var currentLine: PointF? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupPoints(w, h)
    }

    private fun setupPoints(width: Int, height: Int) {
        points.clear()
        val cols = 3
        val rows = 3
        val gapX = width / (cols + 1)
        val gapY = height / (rows + 1)

        for (row in 1..rows) {
            for (col in 1..cols) {
                points.add(PointF(col * gapX.toFloat(), row * gapY.toFloat()))
            }
        }
    }

    fun updateColors(color: Int) {
        selectedCirclePaint.color = color
        circlePaint.color = color
        linePaint.color=color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        points.forEachIndexed { index, point ->
            val paint = if (selectedPoints.contains(index)) selectedCirclePaint else circlePaint
            canvas.drawCircle(point.x, point.y, radius, paint)
        }

        if (selectedPoints.size > 1) {
            for (i in 0 until selectedPoints.size - 1) {
                val start = points[selectedPoints[i]]
                val end = points[selectedPoints[i + 1]]
                canvas.drawLine(start.x, start.y, end.x, end.y, linePaint)
            }
        }

        currentLine?.let { line ->
            val lastPoint = points[selectedPoints.last()]
            canvas.drawLine(lastPoint.x, lastPoint.y, line.x, line.y, linePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                val touchedPointIndex = findTouchedPoint(event.x, event.y)
                if (touchedPointIndex != null && !selectedPoints.contains(touchedPointIndex)) {
                    selectedPoints.add(touchedPointIndex)
                }
                currentLine = PointF(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                currentLine = null
                invalidate()
                patternListener?.onPatternDetected(selectedPoints)
            }
        }
        return true
    }

    private fun findTouchedPoint(x: Float, y: Float): Int? {
        points.forEachIndexed { index, point ->
            if (Math.hypot((x - point.x).toDouble(), (y - point.y).toDouble()) < (radius + 35f)) {
                return index
            }
        }
        return null
    }

     fun resetPattern() {
        circlePaint.color=Color.WHITE
        selectedCirclePaint.color=Color.WHITE
        linePaint.color=Color.WHITE
        selectedPoints.clear()
        currentLine = null
        invalidate()
    }
}
