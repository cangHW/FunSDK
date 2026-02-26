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
         * */
        fun onSelectionChanged(oldPosition: Int, newPosition: Int)
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

    private var linearSnapHelper = LinearSnapHelper()
    private var selectionChangedListener: OnSelectionChangedListener? = null
    private var currentSelectItem = -1

    private fun init(context: Context) {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        super.setLayoutManager(layoutManager)
        clipToPadding = false
        overScrollMode = OVER_SCROLL_NEVER
        linearSnapHelper.attachToRecyclerView(this)
        super.addOnScrollListener(scrollListener)
    }

    private val scrollListener = object : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == SCROLL_STATE_IDLE) {
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                if (layoutManager == null) {
                    CsLogger.tag(TAG)
                        .e("layoutManager cannot be null and must be of the LinearLayoutManager type.")
                    return
                }

                val snapView = linearSnapHelper.findSnapView(layoutManager) ?: return
                val position = layoutManager.getPosition(snapView)

                if (currentSelectItem != position) {
                    selectionChangedListener?.onSelectionChanged(currentSelectItem, position)
                }
                currentSelectItem = position
            }
        }
    }

    override fun setLayoutManager(layout: LayoutManager?) {
//        super.setLayoutManager(layout)

        CsLogger.tag(TAG).e("Setting the LayoutManager is not supported.")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0) {
            val sidePadding = w / 2
            setPadding(sidePadding, paddingTop, sidePadding, paddingBottom)
        }
    }

    override fun addOnScrollListener(listener: OnScrollListener) {
//        super.addOnScrollListener(listener)

        CsLogger.tag(TAG).e("AddOnScrollListener is not supported.")
    }

    /**
     * 设置选中项
     *
     * @param position  选中项下标
     * @param smooth    是否显示滑动动画
     * */
    fun setSelectedPosition(position: Int, smooth: Boolean = true) {
        post {
            val adapter = adapter
            if (adapter == null) {
                CsLogger.tag(TAG).e("adapter is null.")
                return@post
            }

            if (position < 0 || position >= adapter.itemCount) {
                CsLogger.tag(TAG)
                    .e("position cannot be less than 0 or greater than or equal to adapter.itemCount.")
                return@post
            }

            val lm = layoutManager as? LinearLayoutManager
            if (lm == null) {
                CsLogger.tag(TAG)
                    .e("layoutManager cannot be null and must be of the LinearLayoutManager type.")
                return@post
            }

            val item = lm.findViewByPosition(position)
            if (item == null) {
                CsLogger.tag(TAG).e("The item with subscript $position was not found.")
                return@post
            }

            item.post {
                val scroller = object : LinearSmoothScroller(context) {
                    override fun calculateTimeForScrolling(dx: Int): Int {
                        if (!smooth) {
                            return 1
                        }
                        return super.calculateTimeForScrolling(dx)
                    }

                    override fun calculateDxToMakeVisible(view: View, snapPreference: Int): Int {
                        return width / 2 - (view.left + view.width / 2)
                    }
                }
                scroller.targetPosition = position
                lm.startSmoothScroll(scroller)
            }
        }
    }

    /**
     * 设置选中项变化监听
     * */
    fun setOnSelectionChangedListener(listener: OnSelectionChangedListener) {
        this.selectionChangedListener = listener
    }
}