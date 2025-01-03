package com.proxy.service.core.framework.io.file.media.compat

import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.data.bitmap.CsBitmapUtils
import com.proxy.service.core.framework.io.file.base.IMediaStore
import com.proxy.service.core.framework.io.file.media.base.AbstractSource
import com.proxy.service.core.framework.io.file.media.config.MimeType
import com.proxy.service.core.framework.io.file.media.source.ByteSource
import java.io.File


/**
 * @author: cangHX
 * @data: 2024/12/31 17:14
 * @desc:
 */
class ImageStoreCompat : AbstractSource<IMediaStore.IImage>(), IMediaStore.IImage {

    init {
        store.setUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        store.setDisplayName("image_${System.currentTimeMillis()}")
        store.setMimeType(MimeType.IMAGE_JPEG)
        store.setDir(Environment.DIRECTORY_PICTURES + File.separator + CsAppUtils.getAppName())
    }

    override fun setSourceBitmap(bitmap: Bitmap): IMediaStore.IImage {
        CsBitmapUtils.toBytes(bitmap)?.let {
            setSourceByte(it)
        }
        return getT()
    }

    override fun setSourceByte(bytes: ByteArray): IMediaStore.IImage {
        store.setSource(ByteSource(bytes))
        return getT()
    }

    override fun getT(): IMediaStore.IImage {
        return this
    }
}