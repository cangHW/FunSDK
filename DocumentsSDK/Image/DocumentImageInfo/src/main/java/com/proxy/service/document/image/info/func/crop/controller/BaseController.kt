package com.proxy.service.document.image.info.func.crop.controller

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.os.Looper
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.document.image.base.callback.base.OnBoundChangedCallback
import com.proxy.service.document.image.base.callback.base.OnDrawCallback
import com.proxy.service.document.image.base.callback.crop.OnCropCallback
import com.proxy.service.document.image.base.loader.base.IController
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.document.image.base.loader.crop.ICropController
import com.proxy.service.document.image.info.func.crop.CropInfo
import com.proxy.service.threadpool.base.thread.callback.OnFailedCallback
import com.proxy.service.threadpool.base.thread.task.ICallable

/**
 * @author: cangHX
 * @data: 2025/6/3 20:17
 * @desc:
 */
open class BaseController(
    private val option: IOption,
    private val info: CropInfo
) : ICropController, OnBoundChangedCallback,
    OnDrawCallback {

    protected val cropRect = RectF(0f, 0f, 0f, 0f)
    protected val cropPath = Path()

    protected var previewController: IController? = null

    open fun init() {
        option.setBoundChangedCallback(this)
        option.setDrawCallback(this)
    }

    fun setController(controller: IController) {
        this.previewController = controller
    }

    override fun onBoundChanged(
        bitmapRect: RectF,
        matrix: Matrix,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ): Boolean {
        info.boundsChangedToCheckCropRect(cropRect, left, top, right, bottom)

        cropPath.moveTo(cropRect.left, cropRect.top)
        cropPath.lineTo(cropRect.right, cropRect.top)
        cropPath.lineTo(cropRect.right, cropRect.bottom)
        cropPath.lineTo(cropRect.left, cropRect.bottom)
        cropPath.lineTo(cropRect.left, cropRect.top)
        return super.onBoundChanged(bitmapRect, matrix, left, top, right, bottom)
    }

    override fun onDraw(
        bitmapRect: RectF,
        matrix: Matrix,
        canvas: Canvas,
        paint: Paint,
        width: Int,
        height: Int
    ) {
        val flag = info.drawCropCallback?.onDrawCrop(
            bitmapRect,
            matrix,
            canvas,
            paint,
            width,
            height,
            info.maskColor,
            cropRect,
            info.cropLineWidth,
            info.cropLineColor
        )
        if (flag != true) {
            paint.color = info.maskColor
            canvas.drawRect(0f, 0f, width * 1f, height * 1f, paint)

            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.CLEAR))
            canvas.drawRect(cropRect, paint)

            paint.setXfermode(null)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = info.cropLineWidth
            paint.color = info.cropLineColor
            canvas.drawPath(cropPath, paint)
        }
    }

    override fun startCrop(callback: OnCropCallback) {
        realCrop(callback) {
            runMainThread {
                callback.onCropResult(OnCropCallback.CROP_STATUS_OK, it)
            }
        }
    }

    override fun startCrop(width: Int, height: Int, callback: OnCropCallback) {
        realCrop(callback) {
            val scaledBitmap = Bitmap.createScaledBitmap(it, width, height, true)
            it.recycle()
            runMainThread {
                callback.onCropResult(OnCropCallback.CROP_STATUS_OK, scaledBitmap)
            }
        }
    }

    override fun startCrop(
        width: Int,
        height: Int,
        keepAspectRatio: Boolean,
        callback: OnCropCallback
    ) {
        if (!keepAspectRatio) {
            startCrop(width, height, callback)
            return
        }

    }

    private fun realCrop(callback: OnCropCallback, crop: (Bitmap) -> Unit) {
        val bitmap = previewController?.getBitmap()
        val matrix = previewController?.getMatrix()
        if (bitmap == null || matrix == null) {
            runMainThread {
                callback.onCropResult(OnCropCallback.CROP_STATUS_LOAD_ERROR, null)
            }
            return
        }
        CsTask.computationThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val mappedRect = RectF()
                val inverseMatrix = Matrix()
                if (matrix.invert(inverseMatrix)) {
                    inverseMatrix.mapRect(mappedRect, cropRect)
                }

                crop(
                    Bitmap.createBitmap(
                        bitmap,
                        mappedRect.left.toInt(),
                        mappedRect.top.toInt(),
                        mappedRect.width().toInt(),
                        mappedRect.height().toInt()
                    )
                )
                return ""
            }
        })?.setOnFailedCallback(object : OnFailedCallback {
            override fun onCallback(throwable: Throwable) {
                runMainThread {
                    callback.onCropResult(OnCropCallback.CROP_STATUS_CROP_ERROR, null)
                }
            }
        })?.start()
    }

    private fun runMainThread(runnable: Runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run()
            return
        }
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                runnable.run()
                return ""
            }
        })?.start()
    }
}