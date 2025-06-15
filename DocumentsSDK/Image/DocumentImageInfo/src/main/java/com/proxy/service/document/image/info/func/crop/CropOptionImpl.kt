package com.proxy.service.document.image.info.func.crop

import android.graphics.Color
import android.graphics.RectF
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.image.base.callback.crop.OnDrawCropCallback
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.document.image.base.loader.crop.ICropOption
import com.proxy.service.document.image.info.constants.ImageConstants

/**
 * @author: cangHX
 * @data: 2025/6/3 10:11
 * @desc:
 */
class CropOptionImpl(option: IOption) : CropLoaderImpl(option), ICropOption {

    override fun setCropSize(widthPx: Float, heightPx: Float): ICropOption {
        if (widthPx < 0) {
            CsLogger.tag(ImageConstants.LOG_TAG_CROP)
                .e("The widthPx in the crop size cannot be less than 0.")
            return this
        }
        if (heightPx < 0) {
            CsLogger.tag(ImageConstants.LOG_TAG_CROP)
                .e("The heightPx in the crop size cannot be less than 0.")
            return this
        }
        this.cropRect = null
        this.cropWidthPx = widthPx
        this.cropHeightPx = heightPx
        return this
    }

    override fun setCropRect(rect: RectF): ICropOption {
        this.cropRect = rect
        this.cropWidthPx = null
        this.cropHeightPx = null
        return this
    }

    override fun setMaskColor(color: Int): ICropOption {
        this.maskColor = color
        return this
    }

    override fun setMaskColorString(colorString: String): ICropOption {
        this.maskColor = Color.parseColor(colorString)
        return this
    }

    override fun setMaskColorRes(id: Int): ICropOption {
        this.maskColor = CsContextManager.getApplication().getColor(id)
        return this
    }

    override fun setCropLineColor(color: Int): ICropOption {
        this.cropLineColor = color
        return this
    }

    override fun setCropLineColorString(colorString: String): ICropOption {
        this.cropLineColor = Color.parseColor(colorString)
        return this
    }

    override fun setCropLineColorRes(id: Int): ICropOption {
        this.cropLineColor = CsContextManager.getApplication().getColor(id)
        return this
    }

    override fun setCropLineWidth(width: Float): ICropOption {
        this.cropLineWidth = width
        return this
    }

    override fun setDrawCropCallback(callback: OnDrawCropCallback): ICropOption {
        this.drawCropCallback = callback
        return this
    }
}