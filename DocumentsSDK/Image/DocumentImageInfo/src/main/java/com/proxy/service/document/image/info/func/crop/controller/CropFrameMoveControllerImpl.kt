package com.proxy.service.document.image.info.func.crop.controller

import android.graphics.Matrix
import android.graphics.RectF
import android.view.MotionEvent
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.image.base.callback.base.OnTouchEventCallback
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.document.image.base.mode.CropMode
import com.proxy.service.document.image.info.func.crop.CropInfo
import com.proxy.service.document.image.info.utils.RectUtils

/**
 * @author: cangHX
 * @data: 2025/6/26 10:28
 * @desc:
 */
class CropFrameMoveControllerImpl(
    private val option: IOption,
    info: CropInfo,
    private val mode: CropMode.CropFrameMoveMode
) : BaseController(option, info), OnTouchEventCallback {

    companion object {
        private const val TAG = "${ImageConstants.LOG_TAG_IMAGE_START}CFM"
    }

    private val limitRectF = RectF()

    override fun init() {
        super.init()
        option.setScale(1f, 1f)
        option.setTouchEventCallback(this)
    }

    override fun onBoundChanged(
        bitmapRect: RectF,
        matrix: Matrix,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ): Boolean {
        super.onBoundChanged(bitmapRect, matrix, left, top, right, bottom)
        RectUtils.fitRect(bitmapRect, limitRectF, matrix, offset, left, top, right, bottom)
        if (limitRectF.contains(cropRect)) {
            CsLogger.tag(TAG).i("limitRectF=$limitRectF")
        } else {
            CsLogger.tag(TAG)
                .e("Special pictures cannot be cropped. BitmapRect=$limitRectF, CropRect=$cropRect")
        }
        return true
    }

    private var isCropFrameMove = false
    private var downX = 0f
    private var downY = 0f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (limitRectF.contains(cropRect)) {
                    downX = event.x
                    downY = event.y
                    isCropFrameMove = cropRect.contains(downX, downY)
                } else {
                    CsLogger.tag(TAG)
                        .e("Special pictures cannot be cropped. BitmapRect=$limitRectF, CropRect=$cropRect")
                }
            }

            MotionEvent.ACTION_MOVE -> {
                if (isCropFrameMove) {
                    if (downX >= 0f && downY >= 0f) {
                        move(event.x - downX, event.y - downY)
                    }
                    downX = event.x
                    downY = event.y
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isCropFrameMove = false
            }
        }
        return true
    }

    private fun move(offsetX: Float, offsetY: Float) {
        cropRect.offset(offsetX, offsetY)
        val diffX = when {
            cropRect.left < limitRectF.left -> limitRectF.left - cropRect.left
            cropRect.right > limitRectF.right -> limitRectF.right - cropRect.right
            else -> 0f
        }
        val diffY = when {
            cropRect.top < limitRectF.top -> limitRectF.top - cropRect.top
            cropRect.bottom > limitRectF.bottom -> limitRectF.bottom - cropRect.bottom
            else -> 0f
        }
        cropRect.offset(diffX, diffY)

        refreshCropPath()
        previewController?.invalidate()
    }

}