package com.proxy.service.imageloader.base.option.pag

import com.proxy.service.imageloader.base.constants.ImageLoaderConstants
import com.proxy.service.imageloader.base.loader.pag.IPagLoader
import com.proxy.service.imageloader.base.option.base.IOption
import com.proxy.service.imageloader.base.option.pag.callback.PagAnimationCallback
import com.proxy.service.imageloader.base.option.pag.callback.PagAnimationUpdateCallback
import com.proxy.service.imageloader.base.option.pag.image.PagImageData
import com.proxy.service.imageloader.base.option.pag.txt.PagTextData

/**
 * @author: cangHX
 * @data: 2025/10/10 15:02
 * @desc:
 */
interface IPageOption : IPagLoader, IOption<IPageOption>, IPagAction<IPageOption> {

    /**
     * 设置是否自动播放
     *
     * @param isAutoPlay 是否自动播放, 默认为 [ImageLoaderConstants.IS_AUTO_PLAY]
     * */
    fun setAutoPlay(isAutoPlay: Boolean): IPageOption

    /**
     * 替换文案配置信息
     *
     * @param index 待替换的文案下标
     * @param data  待替换的配置信息
     * */
    fun replaceText(index: Int, data: PagTextData): IPageOption

    /**
     * 替换图片配置信息
     *
     * @param index 待替换的图片下标
     * @param data  待替换的配置信息
     * */
    fun replaceImage(index: Int, data: PagImageData): IPageOption

    /**
     * 替换图片配置信息
     *
     * @param name  待替换的图片名称
     * @param data  待替换的配置信息
     * */
    fun replaceImageByName(name: String, data: PagImageData): IPageOption

    /**
     * 设置动画开始回调
     * */
    fun setAnimationStartCallback(callback: PagAnimationCallback): IPageOption

    /**
     * 设置动画结束回调
     * */
    fun setAnimationEndCallback(callback: PagAnimationCallback): IPageOption

    /**
     * 设置动画取消回调
     * */
    fun setAnimationCancelCallback(callback: PagAnimationCallback): IPageOption

    /**
     * 设置动画重播回调，每次重播都会回调一次
     * */
    fun setAnimationRepeatCallback(callback: PagAnimationCallback): IPageOption

    /**
     * 设置动画更新回调
     * */
    fun setAnimationUpdateCallback(callback: PagAnimationUpdateCallback): IPageOption
}