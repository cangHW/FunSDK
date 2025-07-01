package com.proxy.service.document.image.info.drawable

import android.graphics.RectF
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.callback.base.OnBoundChangedCallback
import com.proxy.service.document.image.base.callback.base.OnDoubleClickCallback
import com.proxy.service.document.image.base.callback.base.OnDragCallback
import com.proxy.service.document.image.base.callback.base.OnDrawCallback
import com.proxy.service.document.image.base.callback.base.OnLongPressCallback
import com.proxy.service.document.image.base.callback.base.OnScaleCallback
import com.proxy.service.document.image.base.callback.base.OnSingleClickCallback
import com.proxy.service.document.image.base.callback.base.OnTouchEventCallback

/**
 * @author: cangHX
 * @data: 2025/6/2 18:18
 * @desc:
 */
class ConfigInfo {

    var minScale: Float = ImageConstants.DEFAULT_MIN_SCALE
    var maxScale: Float = ImageConstants.DEFAULT_MAX_SCALE

    var lockSizeWidthPx: Float? = null
    var lockSizeHeightPx: Float? = null
    var lockRect: RectF? = null
    var lockView: Boolean = false
    var canMoveInLockRect: Boolean = true
    var overScrollBounceEnabled: Boolean = false

    var boundChangedCallback: OnBoundChangedCallback? = null
    var touchEventCallback: OnTouchEventCallback? = null
    var dragCallback: OnDragCallback? = null
    var scaleCallback: OnScaleCallback? = null
    var drawCallback: OnDrawCallback? = null
    var singleClickCallback: OnSingleClickCallback? = null
    var doubleClickCallback: OnDoubleClickCallback? = null
    var longPressCallback: OnLongPressCallback? = null

    fun boundsChangedToCheckLockRect(left: Int, top: Int, right: Int, bottom: Int) {
        if (lockRect != null) {
            return
        }

        if (lockView) {
            lockRect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
            return
        }

        if (lockSizeWidthPx == null) {
            return
        }
        if (lockSizeHeightPx == null) {
            return
        }
        val offsetX = ((right - left) - lockSizeWidthPx!!) / 2f
        val offsetY = ((bottom - top) - lockSizeHeightPx!!) / 2f
        lockRect = RectF(left + offsetX, top + offsetY, right - offsetX, bottom - offsetY)
    }
}