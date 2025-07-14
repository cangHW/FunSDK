package com.proxy.service.core.framework.data.span.custom

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.ContextCompat
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.span.enums.ImageAlign
import com.proxy.service.core.framework.io.file.CsFileUtils
import java.io.InputStream

/**
 * 自定义 图片加载 span
 *
 * @author: cangHX
 * @data: 2025/7/14 14:42
 * @desc:
 */
open class CustomImageSpan : CustomDynamicDrawableSpan {

    companion object{
        private const val TAG = "${CoreConfig.TAG}Span_Image"
    }

    private var mDrawable: Drawable? = null
    private var mContentUri: Uri? = null
    private var mResourceId = -1

    constructor(bitmap: Bitmap, verticalAlignment: ImageAlign) : super(verticalAlignment) {
        mDrawable = BitmapDrawable(CsContextManager.getApplication().resources, bitmap)
        mDrawable?.setBounds(
            0, 0, mDrawable!!.intrinsicWidth, mDrawable!!.intrinsicHeight
        )
    }

    constructor(drawable: Drawable, verticalAlignment: ImageAlign) : super(verticalAlignment) {
        mDrawable = drawable
        mDrawable?.setBounds(
            0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight
        )
    }

    constructor(uri: Uri, verticalAlignment: ImageAlign) : super(verticalAlignment) {
        mContentUri = uri
    }

    constructor(resourceId: Int, verticalAlignment: ImageAlign) : super(verticalAlignment) {
        mResourceId = resourceId
    }

    override fun getDrawable(): Drawable? {
        if (mDrawable != null) {
            return mDrawable
        }

        val uri = mContentUri
        if (uri != null) {
            val bitmap: Bitmap
            var stream: InputStream? = null
            try {
                stream = CsContextManager.getApplication().contentResolver?.openInputStream(uri)

                bitmap = BitmapFactory.decodeStream(stream)
                val drawable = BitmapDrawable(
                    CsContextManager.getApplication().resources,
                    bitmap
                )
                drawable.setBounds(
                    0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight
                )
                return drawable
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable, "Unable to open uri: $uri")
            } finally {
                CsFileUtils.close(stream)
            }
        }

        if (mResourceId != -1) {
            try {
                val drawable = ContextCompat.getDrawable(
                    CsContextManager.getApplication(),
                    mResourceId
                )
                drawable!!.setBounds(
                    0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight
                )
                return drawable
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable, "Unable to find resource: $mResourceId")
            }
        }

        return null
    }

}