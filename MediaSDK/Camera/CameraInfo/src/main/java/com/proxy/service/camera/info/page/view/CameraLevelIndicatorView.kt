package com.proxy.service.camera.info.page.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.proxy.service.core.framework.app.resource.CsDpUtils

class CameraLevelIndicatorView : View, DefaultLifecycleObserver, SensorEventListener {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var levelEnabled = false
    private var currentRoll = 0f
    private var smoothRoll = 0f

    private val alpha = 0.15f
    private val levelThreshold = 1.5f

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var lifecycleOwner: LifecycleOwner? = null

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = CsDpUtils.dp2pxf(1f)
        style = Paint.Style.STROKE
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = CsDpUtils.dp2pxf(11f)
        textAlign = Paint.Align.CENTER
    }

    fun setLevelEnabled(enabled: Boolean) {
        if (levelEnabled == enabled) return
        levelEnabled = enabled
        if (enabled) startSensor() else stopSensor()
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val owner = (context as? LifecycleOwner) ?: return
        lifecycleOwner = owner
        owner.lifecycle.addObserver(this)
    }

    override fun onDetachedFromWindow() {
        stopSensor()
        lifecycleOwner?.lifecycle?.removeObserver(this)
        lifecycleOwner = null
        super.onDetachedFromWindow()
    }

    override fun onResume(owner: LifecycleOwner) {
        if (levelEnabled) startSensor()
    }

    override fun onPause(owner: LifecycleOwner) {
        stopSensor()
    }

    private fun startSensor() {
        accelerometer?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun stopSensor() {
        sensorManager?.unregisterListener(this)
        smoothRoll = 0f
        currentRoll = 0f
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val z = event.values[2]
        val roll = Math.toDegrees(Math.atan2(x.toDouble(), z.toDouble())).toFloat()
        smoothRoll += alpha * (roll - smoothRoll)
        currentRoll = smoothRoll
        invalidate()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDraw(canvas: Canvas) {
        if (!levelEnabled) return

        val cx = width / 2f
        val cy = height / 2f
        val lineHalfLen = width / 6f

        val isLevel = Math.abs(currentRoll) < levelThreshold
        val color = if (isLevel) Color.GREEN else Color.argb(200, 255, 255, 255)
        linePaint.color = color
        dotPaint.color = color

        canvas.save()
        canvas.rotate(-currentRoll, cx, cy)

        canvas.drawLine(cx - lineHalfLen, cy, cx + lineHalfLen, cy, linePaint)

        canvas.drawCircle(cx, cy, CsDpUtils.dp2pxf(3f), dotPaint)

        val tickLen = CsDpUtils.dp2pxf(6f)
        canvas.drawLine(cx - lineHalfLen, cy - tickLen, cx - lineHalfLen, cy + tickLen, linePaint)
        canvas.drawLine(cx + lineHalfLen, cy - tickLen, cx + lineHalfLen, cy + tickLen, linePaint)

        canvas.restore()

        dotPaint.color = Color.argb(120, 255, 255, 255)
        canvas.drawCircle(cx, cy, CsDpUtils.dp2pxf(2f), dotPaint)

        val degree = String.format("%.1f°", currentRoll)
        textPaint.color = color
        val textY = cy + lineHalfLen + CsDpUtils.dp2pxf(20f)
        canvas.drawText(degree, cx, textY, textPaint)
    }
}
