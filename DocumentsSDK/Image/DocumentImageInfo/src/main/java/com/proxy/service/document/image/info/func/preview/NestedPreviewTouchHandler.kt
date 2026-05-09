package com.proxy.service.document.image.info.func.preview

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.mode.TouchConflictMode
import com.proxy.service.document.image.info.drawable.ActionDrawable
import kotlin.math.abs

/**
 * 图片预览与父容器滑动冲突处理器.
 *
 * @author: cangHX
 * @data: 2026/5/6 10:25
 * @desc:
 */
class NestedPreviewTouchHandler(
    private val drawable: ActionDrawable,
    private val mode: TouchConflictMode
) : View.OnTouchListener {

    companion object{
        private const val TAG = "${ImageConstants.LOG_TAG_IMAGE_START}TouchHandler"
    }

    private var downX = 0f
    private var downY = 0f
    private var lastX = 0f
    private var lastY = 0f

    private var childAcceptedGesture = false
    private var isScaling = false

    /**
     * 处理图片预览触摸事件.
     *
     * 当前监听器始终消费本次事件, 并通过 requestDisallowInterceptTouchEvent
     * 决定父容器是否可以在后续事件中拦截.
     * */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        if (mode == TouchConflictMode.AlwaysConsume) {
            drawable.onTouchEvent(event)
            return true
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                CsLogger.tag(TAG).d("onTouch, ACTION_DOWN")
                downX = event.x
                downY = event.y
                lastX = event.x
                lastY = event.y

                childAcceptedGesture = false
                isScaling = false
                view.parent?.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                CsLogger.tag(TAG).d("onTouch, ACTION_POINTER_DOWN")
                isScaling = true
                childAcceptedGesture = true
                view.parent?.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                handleMove(view, event)
            }

            MotionEvent.ACTION_POINTER_UP -> {
                CsLogger.tag(TAG).d("onTouch, ACTION_POINTER_UP")
                isScaling = event.pointerCount > 2
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (event.actionMasked == MotionEvent.ACTION_UP) {
                    CsLogger.tag(TAG).d("onTouch, ACTION_UP")
                }else{
                    CsLogger.tag(TAG).d("onTouch, ACTION_CANCEL")
                }
                childAcceptedGesture = false
                isScaling = false
                view.parent?.requestDisallowInterceptTouchEvent(false)
            }
        }

        drawable.onTouchEvent(event)
        return true
    }

    /**
     * 处理移动过程中的父子滑动仲裁.
     *
     * 图片能沿当前主方向移动时由图片接管; 图片已经接管且开启越界回弹时,
     * 即使后续到达边界也继续接管到本次手势结束.
     * */
    private fun handleMove(view: View, event: MotionEvent) {
        val dx = event.x - lastX
        val dy = event.y - lastY
        val totalDx = event.x - downX
        val totalDy = event.y - downY

        val canHandleDrag = if (abs(totalDx) > abs(totalDy)) {
            drawable.canHandleHorizontalDrag(dx)
        } else {
            drawable.canHandleVerticalDrag(dy)
        }

        CsLogger.tag(TAG).d("handleMove, canHandleDrag=$canHandleDrag")

        val shouldChildHandle = when {
            event.pointerCount > 1 || isScaling -> true
            canHandleDrag -> true
            childAcceptedGesture && drawable.isOverScrollBounceEnabled() -> true
            else -> false
        }

        if (shouldChildHandle) {
            childAcceptedGesture = true
        }

        view.parent?.requestDisallowInterceptTouchEvent(shouldChildHandle)

        lastX = event.x
        lastY = event.y
    }
}
