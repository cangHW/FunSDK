package com.proxy.service.webview.base.web.history

/**
 * @author: cangHX
 * @data: 2026/1/16 14:25
 * @desc:
 */
interface IWebBackForwardList {

    /**
     * 获取当前记录项
     * */
    fun getCurrentItem(): IWebHistoryItem?

    /**
     * 获取当前记录项下标, 如果列表为空，则为 -1
     * */
    fun getCurrentIndex(): Int

    /**
     * 获取目标位置的记录项, 如果不存在则为 null
     * */
    fun getItemAtIndex(index: Int): IWebHistoryItem?

    /**
     * 获取全部记录项数量
     * */
    fun getSize(): Int

}