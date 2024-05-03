package com.shubhasai.indoornavigation.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.shubhasai.indoornavigation.models.Location
import com.shubhasai.indoornavigation.models.Path

class IndoorMapView(context: Context) : View(context) {

    private var currentLocation: Location? = null
    private var selectedLocation: Location? = null  // Track selected location
    private var storedLocations: List<Location> = listOf()
    private var selectedPaths: List<Path?> = listOf()

    private var floor: Int = 1  // Default to floor 1

    fun setLocation(floor: Int, x: Float, y: Float) {
        this.floor = floor
        this.currentLocation = Location(floor, x, y)
        invalidate()  // Trigger redraw
    }

    fun setStoredLocations(locations: List<Location>) {
        this.storedLocations = locations
        invalidate()
    }

    fun setSelectedPaths(paths: List<Path?>) {
        this.selectedPaths = paths
        invalidate()  // Trigger redraw
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val touchX = event.x
            val touchY = event.y

            val scaleX = width.toFloat() / 720
            val scaleY = height.toFloat() / 1472

            // Check for click on stored locations
            for (location in storedLocations) {
                if (location.floor == floor) {
                    val markerX = location.x * scaleX
                    val markerY = location.y * scaleY
                    val markerRadius = 30f  // Adjust based on marker size

                    // Calculate distance between touch and marker center
                    val distance = calculateDistance(touchX, touchY, markerX, markerY)

                    if (distance <= markerRadius) {
                        selectedLocation = location
                        invalidate()  // Trigger redraw to show name
                        return true  // Consume the event (optional)
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // Draw map background (replace with your desired background)
        canvas.drawRect(0f, 0f, width, height, Paint().apply { color = Color.WHITE })

        // Adjust position based on coordinates (scale if needed)
        val scaleX = width /720
        val scaleY = height / 1472

                // Draw current location marker (if available)
        if (currentLocation != null) {
            val markerX = currentLocation!!.x * scaleX
            val markerY = currentLocation!!.y * scaleY
            drawCircle(canvas, markerX, markerY, 30f, Paint().apply { color = Color.BLUE })
        }

        // Draw stored location markers
        for (location in storedLocations) {
            if (location.floor == floor) {
                val markerX = location.x * scaleX
                val markerY = location.y * scaleY
                drawMarker(canvas, markerX, markerY, location.iconId)
            }
        }

        // Draw path if selected
        for (path in selectedPaths) {
            if (path != null && path.floor == floor) {
                val pathPaint = Paint().apply {
                    color = Color.BLUE  // Customize path color (can be different for each path)
                    style = Paint.Style.STROKE
                    strokeWidth = 10f  // Customize path line width
                }
                drawPath(path.pathPoints, scaleX, scaleY, pathPaint, canvas)
            }
        }
        if (selectedLocation != null) {
            val textPaint = Paint().apply {
                color = Color.BLACK  // Customize text color
                textSize = 30f  // Customize text size
            }
            val textX = selectedLocation!!.x * scaleX + 10f  // Adjust position as needed
            val textY = selectedLocation!!.y * scaleY - 30f  // Adjust position as needed
            selectedLocation!!.name?.let { canvas.drawText(it, textX, textY, textPaint) }
        }
    }
    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val dx = x2 - x1
        val dy = y2 - y1
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    private fun drawMarker(canvas: Canvas, x: Float, y: Float, iconId: Int?) {
        if (iconId != null) {
            val icon = ContextCompat.getDrawable(context, iconId)
            val intrinsicWidth = icon?.intrinsicWidth
            val intrinsicHeight = icon?.intrinsicHeight
            val iconLeft = (x - intrinsicWidth!! / 2).toInt()
            val iconTop = (y - intrinsicHeight!! / 2).toInt()
            icon.setBounds(iconLeft, iconTop, iconLeft + intrinsicWidth, iconTop + intrinsicHeight)
            icon.draw(canvas)
        } else {
            drawCircle(canvas, x, y, 10f, Paint().apply { color = Color.BLACK })
        }
    }

    private fun drawCircle(canvas: Canvas, x: Float, y: Float, radius: Float, paint: Paint) {
        canvas.drawCircle(x, y, radius, paint)
    }

    private fun drawPath(pathPoints: List<Location>, scaleX: Float, scaleY: Float, paint: Paint, canvas: Canvas) {
        val translatedPoints = mutableListOf<PointF>()
        for (location in pathPoints) {
            val translatedX = location.x * scaleX
            val translatedY = location.y * scaleY
            translatedPoints.add(PointF(translatedX, translatedY))
        }

        val pathLine = android.graphics.Path()
        pathLine.moveTo(translatedPoints[0].x, translatedPoints[0].y)
        for (i in 1 until translatedPoints.size) {
            pathLine.lineTo(translatedPoints[i].x, translatedPoints[i].y)
        }
        canvas.drawPath(pathLine, paint)
    }
}
