package com.proxy.service.widget.info.view.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.constants.WidgetConstants

/**
 * @author: cangHX
 * @data: 2026/2/11 16:44
 * @desc: 居中选中 RecyclerView
 */
class CsCenterSelectRecyclerView : RecyclerView {

    companion object {
        private const val TAG = "${WidgetConstants.TAG}RecyclerView"
    }

    interface OnSelectionChangedListener {
        /**
         * 选中项发生变化
         *
         * @param oldPosition 变化前的下标，-1 表示初始状态
         * @param newPosition 变化后的下标
         * @param fromUser    true = 用户手势触发；false = 代码调用 setSelectedPosition 触发
         */
        fun onSelectionChanged(oldPosition: Int, newPosition: Int, fromUser: Boolean)
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private val snapHelper = LinearSnapHelper()
    private var selectionChangedListener: OnSelectionChangedListener? = null

    private var currentSelectItem = NO_POSITION

    private var wasUserDriven = false
    private var isFlingEnabled = true

    private fun init(context: Context) {
        val lm = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        super.setLayoutManager(lm)
        clipToPadding = false
        overScrollMode = OVER_SCROLL_NEVER
        snapHelper.attachToRecyclerView(this)
        super.addOnScrollListener(internalScrollListener)
    }

    private val internalScrollListener = object : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            when (newState) {
                SCROLL_STATE_DRAGGING -> {
                    wasUserDriven = true
                }

                SCROLL_STATE_IDLE -> {
                    val lm = recyclerView.layoutManager as? LinearLayoutManager ?: return
                    val snapView = snapHelper.findSnapView(lm) ?: return
                    val position = lm.getPosition(snapView)
                    val fromUser = wasUserDriven
                    wasUserDriven = false
                    updateSelection(position, fromUser)
                }
            }
        }
    }

    private fun updateSelection(newPosition: Int, fromUser: Boolean) {
        if (currentSelectItem == newPosition) return
        val old = currentSelectItem
        currentSelectItem = newPosition
        selectionChangedListener?.onSelectionChanged(old, newPosition, fromUser)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        CsLogger.tag(TAG).e("Setting the LayoutManager is not supported.")
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        if (!isFlingEnabled) return false
        return super.fling(velocityX, velocityY)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && w != oldw) {
            val sidePadding = w / 2
            setPadding(sidePadding, paddingTop, sidePadding, paddingBottom)
            val selected = currentSelectItem
            if (selected != NO_POSITION) {
                post {
                    val lm = layoutManager as? LinearLayoutManager ?: return@post
                    val scroller = buildSmoothScroller(smooth = false)
                    scroller.targetPosition = selected
                    lm.startSmoothScroll(scroller)
                }
            }
        }
    }

    /**
     * 获取当前选中项下标，未选中时返回 [RecyclerView.NO_POSITION]
     */
    fun getCurrentSelectedPosition(): Int {
        return currentSelectItem
    }

    /**
     * 静默设置初始选中项，不触发 [OnSelectionChangedListener] 回调。
     * 适合在 adapter 填充数据后做首次定位。
     *
     * @param position 目标下标
     */
    fun setInitialPosition(position: Int) {
        post {
            val adapter = adapter ?: return@post
            if (position < 0 || position >= adapter.itemCount) {
                CsLogger.tag(TAG).e("setInitialPosition: position $position out of bounds.")
                return@post
            }
            val lm = layoutManager as? LinearLayoutManager ?: return@post
            currentSelectItem = position
            val scroller = buildSmoothScroller(smooth = false)
            scroller.targetPosition = position
            lm.startSmoothScroll(scroller)
        }
    }

    /**
     * 滚动到指定位置并居中选中。
     *
     * @param position 目标下标
     * @param smooth   是否显示滑动动画
     */
    fun setSelectedPosition(position: Int, smooth: Boolean = true) {
        post {
            val adapter = adapter ?: run {
                CsLogger.tag(TAG).e("setSelectedPosition: adapter is null.")
                return@post
            }
            if (position < 0 || position >= adapter.itemCount) {
                CsLogger.tag(TAG).e("setSelectedPosition: position $position out of bounds.")
                return@post
            }
            val lm = layoutManager as? LinearLayoutManager ?: run {
                CsLogger.tag(TAG)
                    .e("setSelectedPosition: layoutManager is not LinearLayoutManager.")
                return@post
            }
            val scroller = buildSmoothScroller(smooth)
            scroller.targetPosition = position
            lm.startSmoothScroll(scroller)
        }
    }

    /**
     * 控制是否开启惯性滑动（fling），默认开启。
     * 关闭后手指释放时列表立即停止，不会飞过多个 item。
     */
    fun setFlingEnabled(enabled: Boolean) {
        isFlingEnabled = enabled
    }

    /**
     * 设置选中项变化监听
     */
    fun setOnSelectionChangedListener(listener: OnSelectionChangedListener) {
        this.selectionChangedListener = listener
    }

    private fun buildSmoothScroller(smooth: Boolean) = object : LinearSmoothScroller(context) {
        override fun calculateTimeForScrolling(dx: Int): Int {
            return if (smooth) super.calculateTimeForScrolling(dx) else 1
        }

        override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int {
            return width / 2 - (view.left + view.width / 2)
        }
    }
}