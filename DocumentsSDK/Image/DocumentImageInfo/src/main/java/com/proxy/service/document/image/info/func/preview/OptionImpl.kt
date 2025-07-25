package com.proxy.service.document.image.info.func.preview

import android.graphics.Bitmap
import android.graphics.RectF
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.callback.base.OnBoundChangedCallback
import com.proxy.service.document.image.base.callback.base.OnDoubleClickCallback
import com.proxy.service.document.image.base.callback.base.OnDragCallback
import com.proxy.service.document.image.base.callback.base.OnDrawCallback
import com.proxy.service.document.image.base.callback.base.OnLongPressCallback
import com.proxy.service.document.image.base.callback.base.OnScaleCallback
import com.proxy.service.document.image.base.callback.base.OnSingleClickCallback
import com.proxy.service.document.image.base.callback.base.OnTouchEventCallback
import com.proxy.service.document.image.base.loader.base.IOption
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
        private const val TAG = "${ImageConstants.LOG_TAG_IMAGE_START}Option"
    }

    init {
        glideOption?.format(GlideDecodeFormat.ARGB_8888)
    }

    override fun setMinScale(minScale: Float): IOption {
        if (minScale <= 0) {
            CsLogger.tag(TAG).e("The minScale cannot be less than or equal to 0.")
            return this
        }
        if (minScale > maxScale) {
            CsLogger.tag(TAG).e("The minScale cannot be more than maxScale.")
            return this
        }
        this.minScale = minScale
        return this
    }

    override fun setMaxScale(maxScale: Float): IOption {
        if (maxScale < 1) {
            CsLogger.tag(TAG).e("The maxScale cannot be less than 1.")
            return this
        }
        if (maxScale < minScale) {
            CsLogger.tag(TAG).e("The maxScale cannot be less than minScale.")
            return this
        }
        this.maxScale = maxScale
        return this
    }

    override fun setScale(minScale: Float, maxScale: Float): IOption {
        if (minScale <= 0) {
            CsLogger.tag(TAG).e("The minScale cannot be less than or equal to 0.")
            return this
        }
        if (maxScale < 1) {
            CsLogger.tag(TAG).e("The maxScale cannot be less than 1.")
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

    override fun setLockViewWithMovable(overScrollBounceEnabled: Boolean): IOption {
        this.lockRect = null
        this.lockView = true
        this.lockSizeWidthPx = null
        this.lockSizeHeightPx = null
        this.canMoveInLockRect = true
        this.overScrollBounceEnabled = overScrollBounceEnabled
        return this
    }

    override fun setLockSizeWithMovable(
        widthPx: Float,
        heightPx: Float,
        overScrollBounceEnabled: Boolean
    ): IOption {
        if (widthPx < 0) {
            CsLogger.tag(TAG).e("The widthPx in the locked size cannot be less than 0.")
            return this
        }
        if (heightPx < 0) {
            CsLogger.tag(TAG).e("The heightPx in the locked size cannot be less than 0.")
            return this
        }
        this.lockRect = null
        this.lockView = false
        this.lockSizeWidthPx = widthPx
        this.lockSizeHeightPx = heightPx
        this.canMoveInLockRect = true
        this.overScrollBounceEnabled = overScrollBounceEnabled
        return this
    }

    override fun setLockRectWithMovable(
        lockRect: RectF,
        overScrollBounceEnabled: Boolean
    ): IOption {
        if (lockRect.right < lockRect.left) {
            CsLogger.tag(TAG).e("The rect right cannot be less than rect left.")
            return this
        }
        if (lockRect.bottom < lockRect.top) {
            CsLogger.tag(TAG).e("The rect bottom cannot be less than rect top.")
            return this
        }
        this.lockRect = lockRect
        this.lockView = false
        this.lockSizeWidthPx = null
        this.lockSizeHeightPx = null
        this.canMoveInLockRect = true
        this.overScrollBounceEnabled = overScrollBounceEnabled
        return this
    }

    override fun setLockViewWithImmovable(overScrollBounceEnabled: Boolean): IOption {
        this.lockRect = null
        this.lockView = true
        this.lockSizeWidthPx = null
        this.lockSizeHeightPx = null
        this.canMoveInLockRect = false
        this.overScrollBounceEnabled = overScrollBounceEnabled
        return this
    }

    override fun setLockSizeWithImmovable(
        widthPx: Float,
        heightPx: Float,
        overScrollBounceEnabled: Boolean
    ): IOption {
        if (widthPx < 0) {
            CsLogger.tag(TAG).e("The widthPx in the locked size cannot be less than 0.")
            return this
        }
        if (heightPx < 0) {
            CsLogger.tag(TAG).e("The heightPx in the locked size cannot be less than 0.")
            return this
        }
        this.lockRect = null
        this.lockView = false
        this.lockSizeWidthPx = widthPx
        this.lockSizeHeightPx = heightPx
        this.canMoveInLockRect = false
        this.overScrollBounceEnabled = overScrollBounceEnabled
        return this
    }

    override fun setLockRectWithImmovable(
        lockRect: RectF,
        overScrollBounceEnabled: Boolean
    ): IOption {
        if (lockRect.right < lockRect.left) {
            CsLogger.tag(TAG).e("The rect right cannot be less than rect left.")
            return this
        }
        if (lockRect.bottom < lockRect.top) {
            CsLogger.tag(TAG).e("The rect bottom cannot be less than rect top.")
            return this
        }
        this.lockRect = lockRect
        this.lockView = false
        this.lockSizeWidthPx = null
        this.lockSizeHeightPx = null
        this.canMoveInLockRect = false
        this.overScrollBounceEnabled = overScrollBounceEnabled
        return this
    }

    override fun setBoundChangedCallback(callback: OnBoundChangedCallback): IOption {
        this.boundChangedCallback = callback
        return this
    }

    override fun setTouchEventCallback(callback: OnTouchEventCallback): IOption {
        this.touchEventCallback = callback
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

    override fun setSingleClickCallback(callback: OnSingleClickCallback): IOption {
        this.singleClickCallback = callback
        return this
    }

    override fun setDoubleClickCallback(callback: OnDoubleClickCallback): IOption {
        this.doubleClickCallback = callback
        return this
    }

    override fun setLongPressCallback(callback: OnLongPressCallback): IOption {
        this.longPressCallback = callback
        return this
    }
}