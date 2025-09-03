package com.proxy.service.widget.info.view.recyclerview

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.proxy.service.core.framework.app.resource.CsDpUtils

/**
 * @author: cangHX
 * @data: 2025/7/9 15:32
 * @desc:
 */
class CsRecyclerItemDecoration private constructor(
    private val spaceInfo: SpaceInfo
) : RecyclerView.ItemDecoration() {

    private class SpaceInfo {
        var spaceHeightWithVertical = 0
        var spaceWidthWithHorizontal = 0

        var isSpaceShowOnViewTop = false
        var isSpaceShowOnViewBottom = false
        var isSpaceShowOnViewLeft = false
        var isSpaceShowOnViewRight = false

        var firstLineOutSize: Int? = null
        var lastLineOutSize: Int? = null
    }

    companion object {
        private val isSpaceShowOnViewTopLocal = ThreadLocal<Boolean>()
        private val isSpaceShowOnViewBottomLocal = ThreadLocal<Boolean>()
        private val isSpaceShowOnViewLeftLocal = ThreadLocal<Boolean>()
        private val isSpaceShowOnViewRightLocal = ThreadLocal<Boolean>()

        private val firstLineOutSizeLocal = ThreadLocal<Int>()
        private val lastLineOutSizeLocal = ThreadLocal<Int>()

        /**
         * 间距是否出现在顶部
         * */
        fun setShowOnTop(isShow: Boolean): Companion {
            isSpaceShowOnViewTopLocal.set(isShow)
            return this
        }

        /**
         * 间距是否出现在底部
         * */
        fun setShowOnBottom(isShow: Boolean): Companion {
            isSpaceShowOnViewBottomLocal.set(isShow)
            return this
        }

        /**
         * 间距是否出现在左侧
         * */
        fun setShowOnLeft(isShow: Boolean): Companion {
            isSpaceShowOnViewLeftLocal.set(isShow)
            return this
        }

        /**
         * 间距是否出现在右侧
         * */
        fun setShowOnRight(isShow: Boolean): Companion {
            isSpaceShowOnViewRightLocal.set(isShow)
            return this
        }

        /**
         * 设置行外间距默认大小
         * */
        fun setOutLineSpaceSize(firstLineOutSize: Int, lastLineOutSize: Int): Companion {
            if (firstLineOutSize > 0) {
                firstLineOutSizeLocal.set(firstLineOutSize)
            }
            if (lastLineOutSize > 0) {
                lastLineOutSizeLocal.set(lastLineOutSize)
            }
            return this
        }

        /**
         * 创建一个间距管理类，并设置横向纵向间距，数值格式：dp
         * */
        fun createWithDp(spaceVDp: Float, spaceHDp: Float): CsRecyclerItemDecoration {
            return createWithPx(CsDpUtils.dp2px(spaceVDp), CsDpUtils.dp2px(spaceHDp))
        }

        /**
         * 创建一个间距管理类，并设置横向纵向间距，数值格式：px
         * */
        fun createWithPx(spaceVPX: Int, spaceHPX: Int): CsRecyclerItemDecoration {
            val realSpaceVPX = Math.max(spaceVPX, 0)
            val realSpaceHPX = Math.max(spaceHPX, 0)

            val isSpaceShowOnViewTop = isSpaceShowOnViewTopLocal.get() ?: false
            isSpaceShowOnViewTopLocal.remove()

            val isSpaceShowOnViewBottom = isSpaceShowOnViewBottomLocal.get() ?: false
            isSpaceShowOnViewBottomLocal.remove()

            val isSpaceShowOnViewLeft = isSpaceShowOnViewLeftLocal.get() ?: false
            isSpaceShowOnViewLeftLocal.remove()

            val isSpaceShowOnViewRight = isSpaceShowOnViewRightLocal.get() ?: false
            isSpaceShowOnViewRightLocal.remove()

            val firstLineOutSize = firstLineOutSizeLocal.get()
            firstLineOutSizeLocal.remove()
            val lastLineOutSize = lastLineOutSizeLocal.get()
            lastLineOutSizeLocal.remove()

            val spaceInfo = SpaceInfo()
            spaceInfo.spaceHeightWithVertical = realSpaceVPX
            spaceInfo.spaceWidthWithHorizontal = realSpaceHPX

            spaceInfo.isSpaceShowOnViewTop = isSpaceShowOnViewTop
            spaceInfo.isSpaceShowOnViewBottom = isSpaceShowOnViewBottom
            spaceInfo.isSpaceShowOnViewLeft = isSpaceShowOnViewLeft
            spaceInfo.isSpaceShowOnViewRight = isSpaceShowOnViewRight

            spaceInfo.firstLineOutSize = firstLineOutSize
            spaceInfo.lastLineOutSize = lastLineOutSize

            return CsRecyclerItemDecoration(spaceInfo)
        }
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0, 0, 0, 0)

        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            processRectByGridLayoutManager(layoutManager, outRect, view, parent)
        } else if (layoutManager is LinearLayoutManager) {
            processRectByLinearLayoutManager(layoutManager, outRect, view, parent)
        }
    }

    private fun processRectByLinearLayoutManager(
        layoutManager: LinearLayoutManager,
        outRect: Rect,
        view: View,
        parent: RecyclerView
    ) {
        val isFirst = parent.getChildAdapterPosition(view) == 0
        val isLast = parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1

        if (layoutManager.orientation == RecyclerView.VERTICAL) {
            if (isFirst){
                if (spaceInfo.isSpaceShowOnViewTop) {
                    outRect.top = spaceInfo.firstLineOutSize ?: spaceInfo.spaceHeightWithVertical
                } else {
                    outRect.top = 0
                }
            }

            outRect.bottom = spaceInfo.spaceHeightWithVertical

            if (isLast){
                if (spaceInfo.isSpaceShowOnViewBottom) {
                    outRect.bottom = spaceInfo.lastLineOutSize ?: spaceInfo.spaceHeightWithVertical
                } else {
                    outRect.bottom = 0
                }
            }
        } else if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
            if (isFirst){
                if (spaceInfo.isSpaceShowOnViewLeft) {
                    outRect.left = spaceInfo.firstLineOutSize ?: spaceInfo.spaceWidthWithHorizontal
                } else {
                    outRect.left = 0
                }
            }

            outRect.right = spaceInfo.spaceWidthWithHorizontal

            if (isLast){
                if (spaceInfo.isSpaceShowOnViewRight) {
                    outRect.right = spaceInfo.lastLineOutSize ?: spaceInfo.spaceWidthWithHorizontal
                } else {
                    outRect.right = 0
                }
            }
        }
    }

    private fun processRectByGridLayoutManager(
        layoutManager: GridLayoutManager,
        outRect: Rect,
        view: View,
        parent: RecyclerView
    ) {
        var maxLine = layoutManager.itemCount / layoutManager.spanCount
        if ((layoutManager.itemCount % layoutManager.spanCount) > 0) {
            maxLine++
        }
        val line = parent.getChildAdapterPosition(view) / layoutManager.spanCount
        val index = parent.getChildAdapterPosition(view) % layoutManager.spanCount

        if (layoutManager.orientation == RecyclerView.VERTICAL) {
            gridLayoutManagerVertical(layoutManager, outRect, maxLine, line, index)
        } else if (layoutManager.orientation == RecyclerView.HORIZONTAL) {
            gridLayoutManagerHorizontal(layoutManager, outRect, maxLine, line, index)
        }
    }

    private fun gridLayoutManagerVertical(
        layoutManager: GridLayoutManager,
        outRect: Rect,
        maxLine: Int,
        currentLine: Int,
        currentIndex: Int
    ) {
        if (currentLine == 0){
            if (spaceInfo.isSpaceShowOnViewTop) {
                outRect.top = spaceInfo.firstLineOutSize ?: spaceInfo.spaceHeightWithVertical
            } else {
                outRect.top = 0
            }
        }

        outRect.bottom = spaceInfo.spaceHeightWithVertical

        if (currentLine == maxLine - 1){
            if (spaceInfo.isSpaceShowOnViewBottom) {
                outRect.bottom = spaceInfo.lastLineOutSize ?: spaceInfo.spaceHeightWithVertical
            } else {
                outRect.bottom = 0
            }
        }

        if (spaceInfo.isSpaceShowOnViewLeft && spaceInfo.isSpaceShowOnViewRight) {
            val eachSHSpace = spaceInfo.spaceWidthWithHorizontal / layoutManager.spanCount
            outRect.left = (layoutManager.spanCount - currentIndex) * eachSHSpace
            outRect.right = (currentIndex + 1) * eachSHSpace
        } else if (spaceInfo.isSpaceShowOnViewLeft) {
            outRect.left = spaceInfo.spaceWidthWithHorizontal
            outRect.right = 0
        } else if (spaceInfo.isSpaceShowOnViewRight) {
            outRect.left = 0
            outRect.right = spaceInfo.spaceWidthWithHorizontal
        } else {
            val eachSHSpace = spaceInfo.spaceWidthWithHorizontal / layoutManager.spanCount
            outRect.left = currentIndex * eachSHSpace
            outRect.right = spaceInfo.spaceWidthWithHorizontal - (currentIndex + 1) * eachSHSpace
        }
    }

    private fun gridLayoutManagerHorizontal(
        layoutManager: GridLayoutManager,
        outRect: Rect,
        maxLine: Int,
        currentLine: Int,
        currentIndex: Int
    ) {
        if (currentLine == 0){
            if (spaceInfo.isSpaceShowOnViewLeft) {
                outRect.left = spaceInfo.firstLineOutSize ?: spaceInfo.spaceWidthWithHorizontal
            } else {
                outRect.left = 0
            }
        }

        outRect.right = spaceInfo.spaceWidthWithHorizontal

        if (currentLine == maxLine - 1){
            if (spaceInfo.isSpaceShowOnViewRight) {
                outRect.right = spaceInfo.lastLineOutSize ?: spaceInfo.spaceWidthWithHorizontal
            } else {
                outRect.right = 0
            }
        }

        if (spaceInfo.isSpaceShowOnViewTop && spaceInfo.isSpaceShowOnViewBottom) {
            val eachSHSpace = spaceInfo.spaceHeightWithVertical / layoutManager.spanCount
            outRect.top = (layoutManager.spanCount - currentIndex) * eachSHSpace
            outRect.bottom = (currentIndex + 1) * eachSHSpace
        } else if (spaceInfo.isSpaceShowOnViewTop) {
            outRect.top = spaceInfo.spaceHeightWithVertical
            outRect.bottom = 0
        } else if (spaceInfo.isSpaceShowOnViewBottom) {
            outRect.top = 0
            outRect.bottom = spaceInfo.spaceHeightWithVertical
        } else {
            val eachSHSpace = spaceInfo.spaceHeightWithVertical / layoutManager.spanCount
            outRect.top = currentIndex * eachSHSpace
            outRect.bottom = spaceInfo.spaceHeightWithVertical - (currentIndex + 1) * eachSHSpace
        }
    }
}