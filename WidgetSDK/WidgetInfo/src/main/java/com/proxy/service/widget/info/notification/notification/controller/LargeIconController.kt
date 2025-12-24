package com.proxy.service.widget.info.notification.notification.controller

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.bitmap.CsBitmapUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.imageloader.CsImageLoader
import com.proxy.service.imageloader.base.target.CsCustomTarget
import com.proxy.service.widget.info.notification.constants.NotificationConstants

/**
 * @author: cangHX
 * @data: 2025/12/16 16:10
 * @desc:
 */
class LargeIconController private constructor(
    private val bitmap: Bitmap?,
    private val url: String?,
    private val resId: Int?,
) : BaseController() {

    companion object {
        fun create(): LargeIconController {
            return LargeIconController(null, null, null)
        }

        fun createByBitmap(bitmap: Bitmap): LargeIconController {
            return LargeIconController(bitmap, null, null)
        }

        fun createByUrl(url: String): LargeIconController {
            return LargeIconController(null, url, null)
        }

        fun createByResId(resId: Int): LargeIconController {
            return LargeIconController(null, null, resId)
        }
    }

    override fun doRun(builder: NotificationCompat.Builder, callback: () -> Unit) {
        if (bitmap != null) {
            builder.setSmallIcon(IconCompat.createWithBitmap(bitmap))
            callback()
        } else if (!url.isNullOrEmpty() && url.isNotBlank()) {
            createIconByUrl(url) { icon ->
                icon?.let {
                    builder.setSmallIcon(it)
                }
                callback()
            }
        } else if (resId != null) {
            createIconByRes(resId) { icon ->
                icon?.let {
                    builder.setSmallIcon(it)
                }
                callback()
            }
        } else {
            callback()
        }
    }

    private fun createIconByUrl(url: String, callback: (icon: IconCompat?) -> Unit) {
        CsImageLoader.with(CsContextManager.getApplication())
            ?.asBitmapModel()
            ?.loadUrl(url)
            ?.into(object : CsCustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap?) {
                    if (resource != null) {
                        callback(IconCompat.createWithBitmap(resource))
                    } else {
                        CsLogger.tag(NotificationConstants.TAG)
                            .e("The notification large icon generated through the url failed. url = $url")
                        createIconByLocal(callback)
                    }
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    CsLogger.tag(NotificationConstants.TAG)
                        .e("The notification large icon generated through the url failed. url = $url")
                    createIconByLocal(callback)
                }
            })
    }

    private fun createIconByRes(resId: Int, callback: (icon: IconCompat?) -> Unit) {
        val drawable = ContextCompat.getDrawable(CsContextManager.getApplication(), resId)
        val bitmap = CsBitmapUtils.toBitmap(drawable)
        if (bitmap != null) {
            callback(IconCompat.createWithBitmap(bitmap))
            return
        }

        CsLogger.tag(NotificationConstants.TAG)
            .e("The notification large icon generated through resid failed. resId = $resId")
        createIconByLocal(callback)
    }

    private fun createIconByLocal(callback: (icon: IconCompat?) -> Unit) {
        val drawable = CsAppUtils.getIcon()
        val bitmap = CsBitmapUtils.toBitmap(drawable)
        if (bitmap != null) {
            callback(IconCompat.createWithBitmap(bitmap))
            return
        }

        CsLogger.tag(NotificationConstants.TAG)
            .e("The notification large icon failed to be generated through the current application icon. pkg = ${CsAppUtils.getPackageName()}")
        callback(null)
    }
}