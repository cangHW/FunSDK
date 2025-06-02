package com.proxy.service.funsdk.document.image

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Matrix
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.proxy.service.api.CloudSystem
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.base.ImageService
import com.proxy.service.document.base.PdfService
import com.proxy.service.document.base.image.callback.loader.OnBoundChangedCallback
import com.proxy.service.document.base.image.callback.loader.OnDragCallback
import com.proxy.service.document.base.image.callback.loader.OnScaleCallback
import com.proxy.service.funsdk.R

/**
 * @author: cangHX
 * @data: 2025/5/30 18:58
 * @desc:
 */
class ImageActivity : AppCompatActivity() {

    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, ImageActivity::class.java)
            if (context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    private val service = CloudSystem.getService(ImageService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_image)

        val group = findViewById<FrameLayout>(R.id.group)

        service?.createLoader(this)
            ?.loadRes(R.drawable.crop)
            ?.setDragCallback(dragCallback)
            ?.setScaleCallback(scaleCallback)
            ?.setBoundChangedCallback(boundChangedCallback)
            ?.into(group)
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.show_image -> {

            }

        }
    }

    private val dragCallback = object : OnDragCallback {
        override fun onDragged(
            bitmapRect: Rect,
            matrix: Matrix,
            oldEvent: MotionEvent?,
            newEvent: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            CsLogger.i("onDragged($bitmapRect, $distanceX, $distanceY)")
            return false
        }
    }

    private val scaleCallback = object : OnScaleCallback {
        override fun onScale(
            bitmapRect: Rect,
            matrix: Matrix,
            currentScale: Float,
            scale: Float,
            centerX: Float,
            centerY: Float
        ): Boolean {
            CsLogger.i("onScale($bitmapRect, $currentScale, $scale, $centerX, $centerY)")
            return false
        }
    }

    private val boundChangedCallback = object : OnBoundChangedCallback {
        override fun onBoundChanged(
            bitmapRect: Rect,
            matrix: Matrix,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int
        ): Boolean {
            CsLogger.i("onBoundChanged($bitmapRect, $left, $top, $right, $bottom)")
            return false
        }
    }
}