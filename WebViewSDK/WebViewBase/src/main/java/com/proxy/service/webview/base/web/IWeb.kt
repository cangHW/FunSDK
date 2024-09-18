package com.proxy.service.webview.base.web

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import com.proxy.service.webview.base.web.callback.ValueCallback

/**
 * @author: cangHX
 * @data: 2024/7/31 18:40
 * @desc:
 */
interface IWeb {

    @IntDef(
        value = [
            View.LAYER_TYPE_NONE,
            View.LAYER_TYPE_SOFTWARE,
            View.LAYER_TYPE_HARDWARE
        ]
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class LayerType

    @IntDef(
        value = [
            View.VISIBLE,
            View.INVISIBLE,
            View.GONE
        ]
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class Visibility

    /**
     * 从父布局中移除，可以用于 Web 容器复用。[changeParentView]
     * */
    fun removeFromParent()

    /**
     * 修改父布局
     * */
    fun changeParentView(viewGroup: ViewGroup?)

    /**
     * 设置背景颜色
     * */
    fun setBackgroundColor(color: Int)

    /**
     * 设置设置显示隐藏状态
     * */
    fun setVisibility(@Visibility visibility: Int)

    /**
     * 设置渲染方式，用于启用或禁用硬件加速，或者强制使用软件渲染等
     * */
    fun setLayerType(@LayerType layerType: Int, paint: Paint?)

    /**
     *  调用 js 方法
     * */
    fun evaluateJavascript(script: String, resultCallback: ValueCallback<String?>?)

    /**
     * 加载页面
     * */
    fun loadUrl(url: String)

    /**
     * 重新加载
     * */
    fun reload()

    /**
     * 是否可以回到上一页
     * */
    fun canGoBack(): Boolean

    /**
     * 进入上一页
     * */
    fun goBack()

    /**
     * 是否可以回到下一页
     * */
    fun canGoForward(): Boolean

    /**
     * 进入下一页
     * */
    fun goForward()

    /**
     * 暂停 Web 容器活动，包括 JavaScript 定时器和其他可能消耗资源的操作，
     * 以减少内存占用，释放不必要资源占用
     * */
    fun onPause()

    /**
     * 暂停所有与 Web 容器相关的时间计数器，
     * 更高级别减少内存占用，释放不必要资源占用
     * */
    fun pauseTimers()

    /**
     * 保存 web 容器状态
     * */
    fun saveState(outState: Bundle)

    /**
     * 恢复 web 容器状态
     * */
    fun restoreState(savedInstanceState: Bundle)

    /**
     * 恢复 Web 容器，包括 JavaScript 定时器和其他的操作
     * */
    fun onResume()

    /**
     * 恢复所有与 Web 容器相关的定时器
     * */
    fun resumeTimers()

    /**
     * 清除 Web 容器缓存，以减少内存占用
     * @param includeDiskFiles  是否包含磁盘文件
     * */
    fun clearCache(includeDiskFiles: Boolean)

    /**
     * 清除 Web 容器历史记录，以减少内存占用，会导致无法通过后退按钮返回到之前访问过的页面
     * */
    fun clearHistory()

    /**
     * 销毁 Web 容器
     * */
    fun destroy()

}