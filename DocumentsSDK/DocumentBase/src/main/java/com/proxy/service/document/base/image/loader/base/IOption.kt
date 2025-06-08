package com.proxy.service.document.base.image.loader.base

import android.graphics.RectF
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
 * @data: 2025/5/30 10:09
 * @desc:
 */
interface IOption : ILoader<IController> {

    /**
     * 设置缩放倍数
     *
     * @param minScale  最小缩放倍数, 默认: [com.proxy.service.document.base.constants.Constants.DEFAULT_MIN_SCALE]
     * */
    fun setMinScale(minScale: Float): IOption

    /**
     * 设置缩放倍数
     *
     * @param maxScale  最小缩放倍数, 默认: [com.proxy.service.document.base.constants.Constants.DEFAULT_MAX_SCALE]
     * */
    fun setMaxScale(maxScale: Float): IOption

    /**
     * 设置缩放倍数
     *
     * @param minScale  最小缩放倍数, 默认: [com.proxy.service.document.base.constants.Constants.DEFAULT_MIN_SCALE]
     * @param maxScale  最大缩放倍数, 默认: [com.proxy.service.document.base.constants.Constants.DEFAULT_MAX_SCALE]
     * */
    fun setScale(minScale: Float, maxScale: Float): IOption

    /**
     * 设置锁定区域, 锁定区域内允许移动, 默认锁定区域为空
     *
     * 举例: 锁定区域为[100, 100, 200, 200], 图片大小位置为[130, 80, 180, 260]
     *
     * 1、图片向左最大移动到[100, 80, 150, 260]
     * 2、图片向右最大移动到[150, 80, 200, 260]
     * 3、图片向上最大移动到[130, 20, 180, 200]
     * 4、图片向下最大移动到[130, 100, 180, 280]
     *
     * @param lockRect                  锁定区域
     * @param overScrollBounceEnabled   是否使用越界回弹
     * */
    fun setLockRectWithMovable(lockRect: RectF, overScrollBounceEnabled: Boolean): IOption

    /**
     * 设置锁定区域, 锁定区域内不允许移动, 默认锁定区域为空
     *
     * 举例：锁定区域为[100, 100, 200, 200], 图片大小位置为[130, 80, 180, 260]
     *
     * 1、图片向左不可移动
     * 2、图片向右不可移动
     * 3、图片向上最大移动到[130, 20, 180, 200]
     * 4、图片向下最大移动到[130, 100, 180, 280]
     *
     * @param lockRect                  锁定区域
     * @param overScrollBounceEnabled   是否使用越界回弹
     * */
    fun setLockRectWithImmovable(lockRect: RectF, overScrollBounceEnabled: Boolean): IOption

    /**
     * 设置显示区域大小位置变化时回调
     * */
    fun setBoundChangedCallback(callback: OnBoundChangedCallback): IOption

    /**
     * 设置触摸事件回调
     * */
    fun setTouchEventCallback(callback: OnTouchEventCallback): IOption

    /**
     * 设置拖动时回调
     * */
    fun setDragCallback(callback: OnDragCallback): IOption

    /**
     * 设置缩放时回调
     * */
    fun setScaleCallback(callback: OnScaleCallback): IOption

    /**
     * 设置缩放时回调
     * */
    fun setDrawCallback(callback: OnDrawCallback): IOption

    /**
     * 设置单击回调
     * */
    fun setSingleClickCallback(callback: OnSingleClickCallback): IOption

    /**
     * 设置双击回调
     * */
    fun setDoubleClickCallback(callback: OnDoubleClickCallback): IOption

    /**
     * 设置长按回调
     * */
    fun setLongPressCallback(callback: OnLongPressCallback): IOption

}