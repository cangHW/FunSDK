package com.proxy.service.widget.info.view.recyclerview.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proxy.service.core.framework.app.resource.CsDpUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.widget.info.constants.WidgetConstants
import kotlin.math.roundToInt

/**
 * @author: cangHX
 * @data: 2025/7/9 15:32
 * @desc: RecyclerView item 间距管理
 */
object CsRecyclerItemDecoration {

    private const val TAG = "${WidgetConstants.TAG}ItemDecoration"

    fun builder(): Builder {
        return Builder()
    }

    class Builder {
        private var spaceVPx = 0
        private var spaceHPx = 0
        private var showOnTop = false
        private var showOnBottom = false
        private var showOnLeft = false
        private var showOnRight = false
        private var firstLineOutSizePx: Int? = null
        private var lastLineOutSizePx: Int? = null

        /**
         * 纵向间距，单位 dp
         * */
        fun verticalSpaceDp(dp: Float) = apply { spaceVPx = CsDpUtils.dp2px(dp) }

        /**
         * 横向间距，单位 dp
         * */
        fun horizontalSpaceDp(dp: Float) = apply { spaceHPx = CsDpUtils.dp2px(dp) }

        /**
         * 纵向间距，单位 px
         * */
        fun verticalSpacePx(px: Int) = apply { spaceVPx = maxOf(px, 0) }

        /**
         * 横向间距，单位 px
         * */
        fun horizontalSpacePx(px: Int) = apply { spaceHPx = maxOf(px, 0) }

        /**
         * 顶部是否显示间距
         * */
        fun showOnTop(show: Boolean = true) = apply { showOnTop = show }

        /**
         * 底部是否显示间距
         * */
        fun showOnBottom(show: Boolean = true) = apply { showOnBottom = show }

        /**
         * 左侧是否显示间距
         * */
        fun showOnLeft(show: Boolean = true) = apply { showOnLeft = show }

        /**
         * 右侧是否显示间距
         * */
        fun showOnRight(show: Boolean = true) = apply { showOnRight = show }

        /**
         * 第一行（列）外边距，单位 dp；未设置时复用主间距
         * */
        fun firstLineOutSizeDp(dp: Float) = apply { firstLineOutSizePx = CsDpUtils.dp2px(dp) }

        /**
         * 最后一行（列）外边距，单位 dp；未设置时复用主间距
         * */
        fun lastLineOutSizeDp(dp: Float) = apply { lastLineOutSizePx = CsDpUtils.dp2px(dp) }

        /**
         * 第一行（列）外边距，单位 px；未设置时复用主间距
         * */
        fun firstLineOutSizePx(px: Int) = apply { if (px > 0) firstLineOutSizePx = px }

        /**
         * 最后一行（列）外边距，单位 px；未设置时复用主间距
         * */
        fun lastLineOutSizePx(px: Int) = apply { if (px > 0) lastLineOutSizePx = px }

        fun build(): RecyclerView.ItemDecoration = ItemDecorationImpl(
            Config(
                spaceVPx = spaceVPx,
                spaceHPx = spaceHPx,
                showOnTop = showOnTop,
                showOnBottom = showOnBottom,
                showOnLeft = showOnLeft,
                showOnRight = showOnRight,
                firstLineOutSizePx = firstLineOutSizePx,
                lastLineOutSizePx = lastLineOutSizePx
            )
        )
    }

    private data class Config(
        val spaceVPx: Int,
        val spaceHPx: Int,
        val showOnTop: Boolean,
        val showOnBottom: Boolean,
        val showOnLeft: Boolean,
        val showOnRight: Boolean,
        val firstLineOutSizePx: Int?,
        val lastLineOutSizePx: Int?
    )

    private class ItemDecorationImpl(
        private val config: Config
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.set(0, 0, 0, 0)
            when (val lm = parent.layoutManager) {
                is GridLayoutManager -> {
                    if (lm.spanSizeLookup !is GridLayoutManager.DefaultSpanSizeLookup) {
                        CsLogger.tag(TAG)
                            .e("Custom SpanSizeLookup is not supported; item offsets may be incorrect.")
                    }
                    processGrid(lm, outRect, view, parent)
                }

                is LinearLayoutManager -> {
                    processLinear(lm, outRect, view, parent)
                }
            }
        }

        private fun processLinear(
            lm: LinearLayoutManager,
            outRect: Rect,
            view: View,
            parent: RecyclerView
        ) {
            val position = parent.getChildAdapterPosition(view)
            val isFirst = position == 0
            val isLast = position == lm.itemCount - 1

            if (lm.orientation == RecyclerView.VERTICAL) {
                outRect.top = if (isFirst && config.showOnTop) {
                    config.firstLineOutSizePx ?: config.spaceVPx
                } else {
                    0
                }

                outRect.bottom = if (isLast && config.showOnBottom) {
                    config.lastLineOutSizePx ?: config.spaceVPx
                } else if (isLast) {
                    0
                } else {
                    config.spaceVPx
                }

                outRect.left = if (config.showOnLeft) {
                    config.spaceHPx
                } else {
                    0
                }

                outRect.right = if (config.showOnRight) {
                    config.spaceHPx
                } else {
                    0
                }
            } else {
                // 主轴（左右）
                outRect.left = if (isFirst && config.showOnLeft) {
                    config.firstLineOutSizePx ?: config.spaceHPx
                } else {
                    0
                }

                outRect.right = if (isLast && config.showOnRight) {
                    config.lastLineOutSizePx ?: config.spaceHPx
                } else if (isLast) {
                    0
                } else {
                    config.spaceHPx
                }

                outRect.top = if (config.showOnTop) {
                    config.spaceVPx
                } else {
                    0
                }

                outRect.bottom = if (config.showOnBottom) {
                    config.spaceVPx
                } else {
                    0
                }
            }
        }

        private fun processGrid(
            lm: GridLayoutManager,
            outRect: Rect,
            view: View,
            parent: RecyclerView
        ) {
            val position = parent.getChildAdapterPosition(view)
            val spanCount = lm.spanCount
            val lineCount = lm.itemCount.ceilDiv(spanCount)
            val line = position / spanCount
            val index = position % spanCount

            if (lm.orientation == RecyclerView.VERTICAL) {
                gridVertical(outRect, spanCount, lineCount, line, index)
            } else {
                gridHorizontal(outRect, spanCount, lineCount, line, index)
            }
        }

        /**
         * 垂直 Grid：
         * - 主轴（上下）首尾行特殊处理，中间行统一 bottom
         * - 交叉轴（左右）浮点均分，消除整除截断误差；
         *   单侧外边距只作用于边缘列，其余列保持均匀间距
         */
        private fun gridVertical(
            outRect: Rect,
            spanCount: Int,
            lineCount: Int,
            line: Int,
            index: Int
        ) {
            outRect.top = if (line == 0 && config.showOnTop) {
                config.firstLineOutSizePx ?: config.spaceVPx
            } else {
                0
            }

            outRect.bottom = if (line == lineCount - 1 && config.showOnBottom) {
                config.lastLineOutSizePx ?: config.spaceVPx
            } else if (line == lineCount - 1) {
                0
            } else {
                config.spaceVPx
            }

            val space = config.spaceHPx.toFloat()
            if (config.showOnLeft && config.showOnRight) {
                // 两侧均有外边距，列间距 = 外边距 = spaceHPx
                outRect.left = ((spanCount - index) * space / spanCount).roundToInt()
                outRect.right = ((index + 1) * space / spanCount).roundToInt()
            } else if (config.showOnLeft) {
                // 仅最左列加外边距，其余列按无外边距公式均分，保证列间距不变
                outRect.left = if (index == 0) {
                    config.spaceHPx
                } else {
                    (index * space / spanCount).roundToInt()
                }
                outRect.right = (space - (index + 1) * space / spanCount).roundToInt()
            } else if (config.showOnRight) {
                // 仅最右列加外边距
                outRect.left = (index * space / spanCount).roundToInt()
                outRect.right = if (index == spanCount - 1) {
                    config.spaceHPx
                } else {
                    (space - (index + 1) * space / spanCount).roundToInt()
                }
            } else {
                // 无外边距，纯列间距均分
                outRect.left = (index * space / spanCount).roundToInt()
                outRect.right = (space - (index + 1) * space / spanCount).roundToInt()
            }
        }

        /**
         * 水平 Grid：
         * - 主轴（左右）首尾列特殊处理，中间列统一 right
         * - 交叉轴（上下）浮点均分；单侧外边距只作用于边缘行
         */
        private fun gridHorizontal(
            outRect: Rect,
            spanCount: Int,
            lineCount: Int,
            line: Int,
            index: Int
        ) {
            // 主轴间距
            outRect.left = if (line == 0 && config.showOnLeft) {
                config.firstLineOutSizePx ?: config.spaceHPx
            } else {
                0
            }

            outRect.right = if (line == lineCount - 1 && config.showOnRight) {
                config.lastLineOutSizePx ?: config.spaceHPx
            } else if (line == lineCount - 1) {
                0
            } else {
                config.spaceHPx
            }

            // 交叉轴间距（浮点均分）
            val space = config.spaceVPx.toFloat()
            if (config.showOnTop && config.showOnBottom) {
                outRect.top = ((spanCount - index) * space / spanCount).roundToInt()
                outRect.bottom = ((index + 1) * space / spanCount).roundToInt()
            } else if (config.showOnTop) {
                outRect.top = if (index == 0) {
                    config.spaceVPx
                } else {
                    (index * space / spanCount).roundToInt()
                }
                outRect.bottom = (space - (index + 1) * space / spanCount).roundToInt()
            } else if (config.showOnBottom) {
                outRect.top = (index * space / spanCount).roundToInt()
                outRect.bottom = if (index == spanCount - 1) {
                    config.spaceVPx
                } else {
                    (space - (index + 1) * space / spanCount).roundToInt()
                }
            } else {
                outRect.top = (index * space / spanCount).roundToInt()
                outRect.bottom = (space - (index + 1) * space / spanCount).roundToInt()
            }
        }

        private fun Int.ceilDiv(other: Int) = (this + other - 1) / other
    }

}