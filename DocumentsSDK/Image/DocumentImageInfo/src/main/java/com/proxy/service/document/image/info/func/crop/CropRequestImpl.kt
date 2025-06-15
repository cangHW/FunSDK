package com.proxy.service.document.image.info.func.crop

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.document.image.base.loader.base.IRequest
import com.proxy.service.document.image.base.loader.crop.ICropOption
import java.io.File

/**
 * @author: cangHX
 * @data: 2025/6/3 10:08
 * @desc:
 */
class CropRequestImpl(
    private val request: IRequest<IOption>
) : IRequest<ICropOption> {

    override fun loadBitmap(bitmap: Bitmap): ICropOption {
        return CropOptionImpl(request.loadBitmap(bitmap))
    }

    override fun loadDrawable(drawable: Drawable): ICropOption {
        return CropOptionImpl(request.loadDrawable(drawable))
    }

    override fun loadUrl(url: String): ICropOption {
        return CropOptionImpl(request.loadUrl(url))
    }

    override fun loadUri(uri: Uri): ICropOption {
        return CropOptionImpl(request.loadUri(uri))
    }

    override fun loadFile(file: File): ICropOption {
        return CropOptionImpl(request.loadFile(file))
    }

    override fun loadRes(resourceId: Int): ICropOption {
        return CropOptionImpl(request.loadRes(resourceId))
    }

    override fun loadByteArray(bytes: ByteArray): ICropOption {
        return CropOptionImpl(request.loadByteArray(bytes))
    }

    override fun loadAsset(assetPath: String): ICropOption {
        return CropOptionImpl(request.loadAsset(assetPath))
    }
}