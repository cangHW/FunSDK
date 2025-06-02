package com.proxy.service.document.image.drawable

import android.graphics.Rect
import com.proxy.service.document.base.constants.Constants
import com.proxy.service.document.base.image.callback.loader.OnBoundChangedCallback
import com.proxy.service.document.base.image.callback.loader.OnDragCallback
import com.proxy.service.document.base.image.callback.loader.OnScaleCallback

/**
 * @author: cangHX
 * @data: 2025/6/2 18:18
 * @desc:
 */
class ConfigInfo {

    var minScale: Float = Constants.DEFAULT_MIN_SCALE
    var maxScale: Float = Constants.DEFAULT_MAX_SCALE

    var lockRect: Rect? = null

    var boundChangedCallback: OnBoundChangedCallback? = null
    var dragCallback: OnDragCallback? = null
    var scaleCallback: OnScaleCallback? = null
}