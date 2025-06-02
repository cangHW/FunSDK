package com.proxy.service.document.image.loader

import android.graphics.Bitmap
import android.graphics.Rect
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.base.constants.Constants
import com.proxy.service.document.base.image.callback.loader.OnBoundChangedCallback
import com.proxy.service.document.base.image.callback.loader.OnDragCallback
import com.proxy.service.document.base.image.callback.loader.OnDrawCallback
import com.proxy.service.document.base.image.callback.loader.OnScaleCallback
import com.proxy.service.document.base.image.loader.IOption
import com.proxy.service.imageloader.base.option.glide.GlideDecodeFormat
import com.proxy.service.imageloader.base.option.glide.IGlideOption

/**
 * @author: cangHX
 * @data: 2025/5/30 10:50
 * @desc:
 */
class OptionImpl(
    glideOption: IGlideOption<Bitmap>?
) : LoaderImpl(glideOption), IOption {

    companion object {
        private const val TAG = "${Constants.LOG_TAG_IMAGE_START}Option"
    }

    init {
        glideOption?.format(GlideDecodeFormat.ARGB_8888)
    }

    override fun setScale(minScale: Float, maxScale: Float): IOption {
        if (minScale <= 0) {
            CsLogger.tag(TAG).e("The minScale cannot be less than or equal to 0.")
            return this
        }
        if (maxScale < minScale) {
            CsLogger.tag(TAG).e("The maxScale cannot be less than minScale.")
            return this
        }
        this.minScale = minScale
        this.maxScale = maxScale
        return this
    }

    override fun setLockRect(rect: Rect): IOption {
        if (rect.right < rect.left) {
            CsLogger.tag(TAG).e("The rect right cannot be less than rect left.")
            return this
        }
        if (rect.bottom < rect.top) {
            CsLogger.tag(TAG).e("The rect bottom cannot be less than rect top.")
            return this
        }
        this.lockRect = rect
        return this
    }

    override fun setBoundChangedCallback(callback: OnBoundChangedCallback): IOption {
        this.boundChangedCallback = callback
        return this
    }

    override fun setDragCallback(callback: OnDragCallback): IOption {
        this.dragCallback = callback
        return this
    }

    override fun setScaleCallback(callback: OnScaleCallback): IOption {
        this.scaleCallback = callback
        return this
    }

    override fun setDrawCallback(callback: OnDrawCallback): IOption {
        this.drawCallback = callback
        return this
    }
}