package com.proxy.service.document.image.info.drawable

import android.graphics.RectF
import com.proxy.service.document.image.base.constants.Constants
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

    var minScale: Float = Constants.DEFAULT_MIN_SCALE
    var maxScale: Float = Constants.DEFAULT_MAX_SCALE

    var lockSizeWidthPx: Float? = null
    var lockSizeHeightPx: Float? = null
    var lockRect: RectF? = null
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
        if (lockSizeWidthPx == null) {
            return
        }
        if (lockSizeHeightPx == null) {
            return
        }
        val offsetX = ((right - left) - lockSizeWidthPx!!) / 2f
        val offsetY = ((bottom - top) - lockSizeHeightPx!!) / 2f

        if (lockRect == null) {
            lockRect = RectF(0f, 0f, 0f, 0f)
        }
        lockRect?.set(
            left + offsetX,
            top + offsetY,
            right - offsetX,
            bottom - offsetY
        )
    }
}