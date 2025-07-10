package com.proxy.service.widget.info.toast

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import com.proxy.service.widget.info.utils.ThreadUtils

/**
 * @author: cangHX
 * @data: 2025/7/8 14:31
 * @desc:
 */
object CsToast {

    private val toastMap = HashMap<String, ToastViewFactory.BaseViewInfo>()

    private var config: ToastConfig = ToastConfig()
    private var factory: ToastViewFactory = ToastViewFactory()

    private var currentToast: Toast? = null

    /*** *** *** *** *** *** *** *** *** *** 全局配置 *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 设置全局 toast 配置
     * */
    fun setGlobalToastConfig(config: ToastConfig) {
        this.config = config
    }

    /**
     * 设置全局 toast 视图工厂
     * */
    fun setGlobalToastViewFactory(factory: ToastViewFactory) {
        this.factory = factory
    }

    /**
     * 清空已缓存的 toast 对象
     * */
    fun clearToastCache() {
        toastMap.clear()
    }

    /*** *** *** *** *** *** *** *** *** *** 文字吐司 *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 显示文字 toast, 单例
     * */
    fun show(
        @StringRes stringId: Int,
        duration: ToastDuration = ToastDuration.LENGTH_SHORT,
        tag: String = ""
    ) {
        val context = CsContextManager.getApplication()
        show(context.getString(stringId), duration, tag)
    }

    /**
     * 显示文字 toast, 单例
     * */
    fun show(
        content: String,
        duration: ToastDuration = ToastDuration.LENGTH_SHORT,
        tag: String = ""
    ) {
        ThreadUtils.runOnMainThread {
            val key = "text-$tag"

            val context = CsContextManager.getApplication()
            var viewInfo: ToastViewFactory.BaseViewInfo? = toastMap.get(key)
            if (viewInfo == null) {
                viewInfo = factory.getToastView(context, tag)
                toastMap.put(key, viewInfo)
            }
            viewInfo.updateTxt(content)

            val toast = createToast(context, viewInfo.rootView)
            toast.duration = duration.value
            toast.show()
        }
    }

    /*** *** *** *** *** *** *** *** *** *** Icon文字吐司 *** *** *** *** *** *** *** *** *** *** ***/

    /**
     * 显示 icon与文字 toast, 单例
     * */
    fun show(
        @DrawableRes resId: Int,
        @StringRes stringId: Int,
        duration: ToastDuration = ToastDuration.LENGTH_SHORT,
        tag: String = ""
    ) {
        val context = CsContextManager.getApplication()
        show(
            ContextCompat.getDrawable(context, resId),
            context.getString(stringId),
            duration,
            tag
        )
    }

    /**
     * 显示 icon与文字 toast, 单例
     * */
    fun show(
        @DrawableRes resId: Int,
        content: String,
        duration: ToastDuration = ToastDuration.LENGTH_SHORT,
        tag: String = ""
    ) {
        val context = CsContextManager.getApplication()
        show(ContextCompat.getDrawable(context, resId), content, duration, tag)
    }


    /**
     * 显示 icon与文字 toast, 单例
     * */
    fun show(
        icon: Drawable?,
        @StringRes stringId: Int,
        duration: ToastDuration = ToastDuration.LENGTH_SHORT,
        tag: String = ""
    ) {
        val context = CsContextManager.getApplication()
        show(
            icon,
            context.getString(stringId),
            duration,
            tag
        )
    }

    /**
     * 显示 icon与文字 toast, 单例
     * */
    fun show(
        icon: Drawable?,
        content: String,
        duration: ToastDuration = ToastDuration.LENGTH_SHORT,
        tag: String = ""
    ) {
        ThreadUtils.runOnMainThread {
            val key = "icon-text-$tag"

            val context = CsContextManager.getApplication()
            var viewInfo: ToastViewFactory.BaseViewInfo? = toastMap.get(key)
            if (viewInfo == null) {
                viewInfo = factory.getToastViewWithIcon(context, tag)
                toastMap.put(key, viewInfo)
            }
            viewInfo.updateIcon(icon)
            viewInfo.updateTxt(content)

            val toast = createToast(context, viewInfo.rootView)
            toast.duration = duration.value
            toast.show()
        }
    }


    /**
     * 显示 icon与文字 toast, 单例
     * */
    fun show(
        icon: Bitmap?,
        @StringRes stringId: Int,
        duration: ToastDuration = ToastDuration.LENGTH_SHORT,
        tag: String = ""
    ) {
        val context = CsContextManager.getApplication()
        show(
            icon,
            context.getString(stringId),
            duration,
            tag
        )
    }

    /**
     * 显示 icon与文字 toast, 单例
     * */
    fun show(
        icon: Bitmap?,
        content: String,
        duration: ToastDuration = ToastDuration.LENGTH_SHORT,
        tag: String = ""
    ) {
        ThreadUtils.runOnMainThread {
            val key = "icon-text-$tag"

            val context = CsContextManager.getApplication()
            var viewInfo: ToastViewFactory.BaseViewInfo? = toastMap.get(key)
            if (viewInfo == null) {
                viewInfo = factory.getToastViewWithIcon(context, tag)
                toastMap.put(key, viewInfo)
            }
            viewInfo.updateIcon(icon)
            viewInfo.updateTxt(content)

            val toast = createToast(context, viewInfo.rootView)
            toast.duration = duration.value
            toast.show()
        }
    }

    /*** *** *** *** *** *** *** *** *** *** 私有函数 *** *** *** *** *** *** *** *** *** *** ***/

    private fun createToast(context: Context, view: View): Toast {
        var toast = currentToast
        if (toast != null) {
            if (toast.view != view) {
                toast.view = view
            }
            return toast
        }
        toast = Toast(context)
        toast.view = view
        toast.setMargin(config.horizontalMargin, config.verticalMargin)
        toast.setGravity(config.gravity.value, config.xOffsetPx, config.yOffsetPx)
        currentToast = toast
        return toast
    }
}