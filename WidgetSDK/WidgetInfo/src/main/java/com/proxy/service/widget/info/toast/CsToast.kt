package com.proxy.service.widget.info.toast

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.widget.info.toast.enums.ToastDuration
import com.proxy.service.widget.info.toast.info.BaseViewInfo
import com.proxy.service.widget.info.utils.ThreadUtils

/**
 * @author: cangHX
 * @data: 2025/7/8 14:31
 * @desc:
 */
object CsToast {

    private const val KEY_TXT = "text-"
    private const val KEY_ICON_TXT = "icon-text-"

    private val viewInfoCache = HashMap<String, BaseViewInfo>()

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
     * 清空已缓存的 toast 视图对象
     * */
    fun clearToastCache() {
        viewInfoCache.clear()
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
            val context = CsContextManager.getApplication()
            val viewInfo = createViewInfo(context, tag, false)
            viewInfo.updateTxt(content)

            showToast(createToast(context), viewInfo, duration.value)
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
            val context = CsContextManager.getApplication()
            val viewInfo = createViewInfo(context, tag, true)
            viewInfo.updateIcon(icon)
            viewInfo.updateTxt(content)

            showToast(createToast(context), viewInfo, duration.value)
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
            val context = CsContextManager.getApplication()
            val viewInfo = createViewInfo(context, tag, true)
            viewInfo.updateIcon(icon)
            viewInfo.updateTxt(content)

            showToast(createToast(context), viewInfo, duration.value)
        }
    }

    /*** *** *** *** *** *** *** *** *** *** 私有函数 *** *** *** *** *** *** *** *** *** *** ***/

    private fun createViewInfo(context: Context, tag: String, hasIcon: Boolean): BaseViewInfo {
        val key = if (hasIcon) {
            "$KEY_ICON_TXT$tag"
        } else {
            "$KEY_TXT$tag"
        }
        var viewInfo: BaseViewInfo? = viewInfoCache.get(key)
        if (viewInfo == null) {
            viewInfo = if (hasIcon) {
                factory.getToastViewWithIcon(context, tag)
            } else {
                factory.getToastView(context, tag)
            }
            viewInfoCache.put(key, viewInfo)
        }
        return viewInfo
    }

    private fun createToast(context: Context): Toast {
        var toast = currentToast
        if (toast != null) {
            return toast
        }
        toast = Toast(context)
        toast.setMargin(config.horizontalMargin, config.verticalMargin)
        toast.setGravity(config.gravity.value, config.xOffsetPx, config.yOffsetPx)
        currentToast = toast
        return toast
    }

    private fun showToast(toast: Toast, viewInfo: BaseViewInfo, duration: Int) {
        if (toast.view != viewInfo.rootView) {
            toast.view = viewInfo.rootView
        }
        toast.duration = duration
        toast.show()
    }
}