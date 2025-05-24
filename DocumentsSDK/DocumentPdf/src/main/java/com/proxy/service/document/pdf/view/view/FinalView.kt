package com.proxy.service.document.pdf.view.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.base.constants.Constants
import com.proxy.service.document.base.pdf.loader.IPdfLoader
import com.proxy.service.document.pdf.view.config.RenderConfig
import com.proxy.service.document.pdf.view.touch.TouchManager

/**
 * @author: cangHX
 * @data: 2025/5/14 11:35
 * @desc:
 */
class FinalView : SurfaceView {

    companion object {
        private const val TAG = "${Constants.LOG_TAG_PDF_START}View"
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private var surface: Surface? = null
    private val surfaceCallback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            surface = holder.surface
            tryShow()
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            surface = holder.surface
            tryShow()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            surface = null
        }
    }

    private var touchManager: TouchManager? = null
    private val touchCallback = object : TouchManager.Callback {
        override fun onSingleClick(x: Float, y: Float) {
            CsLogger.tag(TAG).i("onSingleClick x=$x, y=$y")
        }

        override fun onDoubleClick(x: Float, y: Float) {
            CsLogger.tag(TAG).i("onDoubleClick x=$x, y=$y")
        }

        override fun onLongClick(x: Float, y: Float) {
            CsLogger.tag(TAG).i("onLongClick x=$x, y=$y")
        }

        override fun onScale(scale: Float) {
            CsLogger.tag(TAG).i("onScale scale=$scale")
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            CsLogger.tag(TAG).i("onScroll distanceX=$distanceX, distanceY=$distanceY")
            tryShow(distanceX.toInt())
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            CsLogger.tag(TAG).i("onFling velocityX=$velocityX, velocityY=$velocityY")
            return true
        }
    }

    private fun init(context: Context) {
        holder.addCallback(surfaceCallback)
        touchManager = TouchManager.create(context, touchCallback)
    }

    private var config: RenderConfig? = null
    private var loader: IPdfLoader? = null

    fun setLoader(config: RenderConfig, loader: IPdfLoader?) {
        this.config = config
        this.loader = loader

        tryShow()
    }

    private var offsetX = 0

    private fun tryShow(distanceX:Int = 0) {
        val sf = surface ?: return
        val ld = loader ?: return

        offsetX -= distanceX

//        ld.renderPageToSurface(
//            0,
//            sf,
//            offsetX - width,
//            0,
//            offsetX,
//            height,
//            true,
//            true,
//            config?.viewBackgroundColor ?: 0,
//            config?.pageBackgroundColor ?: 0
//        )

        ld.renderPageToSurface(
            1,
            sf,
            offsetX,
            0,
            width + offsetX,
            height,
            true,
            true,
            config?.viewBackgroundColor ?: 0,
            config?.pageBackgroundColor ?: 0
        )

        ld.renderPageToSurface(
            2,
            sf,
            offsetX,
            width,
            width * 2 + offsetX,
            height,
            true,
            true,
            config?.viewBackgroundColor ?: 0,
            config?.pageBackgroundColor ?: 0
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (touchManager?.onTouch(event) == true) {
            return true
        }
        return super.onTouchEvent(event)
    }

}