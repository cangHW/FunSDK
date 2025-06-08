package com.proxy.service.document.image.drawable

import android.graphics.RectF
import com.proxy.service.document.base.constants.Constants
import com.proxy.service.document.base.image.callback.base.OnBoundChangedCallback
import com.proxy.service.document.base.image.callback.base.OnDoubleClickCallback
import com.proxy.service.document.base.image.callback.base.OnDragCallback
import com.proxy.service.document.base.image.callback.base.OnDrawCallback
import com.proxy.service.document.base.image.callback.base.OnLongPressCallback
import com.proxy.service.document.base.image.callback.base.OnScaleCallback
import com.proxy.service.document.base.image.callback.base.OnSingleClickCallback
import com.proxy.service.document.base.image.callback.base.OnTouchEventCallback

/**
 * @author: cangHX
 * @data: 2025/6/2 18:18
 * @desc:
 */
class ConfigInfo {

    var minScale: Float = Constants.DEFAULT_MIN_SCALE
    var maxScale: Float = Constants.DEFAULT_MAX_SCALE

    var lockRect: RectF? = null
    var canDragInLockRect: Boolean = true
    var overScrollBounceEnabled: Boolean = false

    var boundChangedCallback: OnBoundChangedCallback? = null
    var touchEventCallback: OnTouchEventCallback? = null
    var dragCallback: OnDragCallback? = null
    var scaleCallback: OnScaleCallback? = null
    var drawCallback: OnDrawCallback? = null
    var singleClickCallback: OnSingleClickCallback? = null
    var doubleClickCallback: OnDoubleClickCallback? = null
    var longPressCallback: OnLongPressCallback? = null
}