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
import com.proxy.service.document.pdf.view.touch.TouchManager

/**
 * @author: cangHX
 * @data: 2025/5/14 11:35
 * @desc:
 */
class FinalView : SurfaceView {

    companion object{
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
    }

    private fun init(context: Context) {
        holder.addCallback(surfaceCallback)
        touchManager = TouchManager.create(context, touchCallback)
    }

    private var loader: IPdfLoader? = null

    fun setLoader(loader: IPdfLoader?) {
        this.loader = loader

        tryShow()
    }

    private fun tryShow() {
        val sf = surface ?: return
        val ld = loader ?: return



        ld.renderPageToSurface(0, sf, width, height, true, true)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (touchManager?.onTouch(event) == true) {
            return true
        }
        return super.onTouchEvent(event)
    }

}