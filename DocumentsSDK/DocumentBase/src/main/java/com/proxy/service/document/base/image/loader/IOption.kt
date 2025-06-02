package com.proxy.service.document.base.image.loader

import android.graphics.Rect
import com.proxy.service.document.base.image.callback.loader.OnBoundChangedCallback
import com.proxy.service.document.base.image.callback.loader.OnDragCallback
import com.proxy.service.document.base.image.callback.loader.OnDrawCallback
import com.proxy.service.document.base.image.callback.loader.OnScaleCallback

/**
 * @author: cangHX
 * @data: 2025/5/30 10:09
 * @desc:
 */
interface IOption : ILoader {

    /**
     * 设置缩放模式
     *
     * @param minScale  最小缩放倍数, 默认: [com.proxy.service.document.base.constants.Constants.DEFAULT_MIN_SCALE]
     * @param maxScale  最大缩放倍数, 默认: [com.proxy.service.document.base.constants.Constants.DEFAULT_MAX_SCALE]
     * */
    fun setScale(minScale: Float, maxScale: Float): IOption

    /**
     * 设置锁定区域, 默认锁定区域为空
     * 1、图片宽/高小于锁定区域宽高时, 只能在锁定区域内拖动。
     * 2、图片宽/高大于锁定区域宽高时, 对应宽高必须覆盖锁定区域
     *
     * 举例：锁定区域为[100, 100, 200, 200], 图片大小位置为[130, 80, 180, 260], 则
     * 1、图片向左最大滑动到[100, 80, 150, 260]
     * 2、图片向右最大滑动到[150, 80, 200, 260]
     * 3、图片向上最大滑动到[130, 20, 180, 200]
     * 4、图片向下最大滑动到[130, 100, 180, 280]
     * */
    fun setLockRect(rect: Rect): IOption

    /**
     * 设置显示区域变化时回调
     * */
    fun setBoundChangedCallback(callback: OnBoundChangedCallback): IOption

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

}