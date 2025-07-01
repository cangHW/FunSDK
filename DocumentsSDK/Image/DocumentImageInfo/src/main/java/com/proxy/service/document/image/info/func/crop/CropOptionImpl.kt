package com.proxy.service.document.image.info.func.crop

import android.graphics.Color
import android.graphics.RectF
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.image.base.callback.crop.OnDrawCropCallback
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.document.image.base.loader.crop.ICropOption
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.mode.CropMode

/**
 * @author: cangHX
 * @data: 2025/6/3 10:11
 * @desc:
 */
class CropOptionImpl(option: IOption) : CropLoaderImpl(option), ICropOption {

    companion object {
        private const val TAG = "${ImageConstants.LOG_TAG_IMAGE_START}CropOption"
    }

    override fun setCropFrameRectToFitBitmap(): ICropOption {
        this.cropFrameFitBitmap = true
        this.cropFrameRect = null
        this.cropFrameWidthPx = null
        this.cropFrameHeightPx = null
        return this
    }

    override fun setCropFrameSize(widthPx: Float, heightPx: Float): ICropOption {
        if (widthPx < 0) {
            CsLogger.tag(TAG)
                .e("The widthPx in the crop size cannot be less than 0. widthPx = $widthPx")
            return this
        }
        if (heightPx < 0) {
            CsLogger.tag(TAG)
                .e("The heightPx in the crop size cannot be less than 0. heightPx = $heightPx")
            return this
        }
        this.cropFrameFitBitmap = false
        this.cropFrameRect = null
        this.cropFrameWidthPx = widthPx
        this.cropFrameHeightPx = heightPx
        return this
    }

    override fun setCropFrameRect(rect: RectF): ICropOption {
        if (rect.right <= rect.left || rect.bottom <= rect.top) {
            CsLogger.tag(TAG).e("RectF is illegal. rect = $rect")
            return this
        }
        this.cropFrameFitBitmap = false
        this.cropFrameRect = rect
        this.cropFrameWidthPx = null
        this.cropFrameHeightPx = null
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

    override fun setCropFrameLineColor(color: Int): ICropOption {
        this.cropFrameLineColor = color
        return this
    }

    override fun setCropFrameLineColorString(colorString: String): ICropOption {
        this.cropFrameLineColor = Color.parseColor(colorString)
        return this
    }

    override fun setCropFrameLineColorRes(id: Int): ICropOption {
        this.cropFrameLineColor = CsContextManager.getApplication().getColor(id)
        return this
    }

    override fun setCropFrameLineWidth(width: Float): ICropOption {
        this.cropFrameLineWidth = width
        return this
    }

    override fun setCropMode(mode: CropMode): ICropOption {
        this.mode = mode
        return this
    }

    override fun setDrawCropCallback(callback: OnDrawCropCallback): ICropOption {
        this.drawCropCallback = callback
        return this
    }
}