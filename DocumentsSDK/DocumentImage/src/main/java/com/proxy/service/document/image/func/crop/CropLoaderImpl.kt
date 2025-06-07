package com.proxy.service.document.image.func.crop

import android.graphics.Color
import android.graphics.RectF
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.proxy.service.document.base.constants.Constants
import com.proxy.service.document.base.image.callback.crop.OnDrawCropCallback
import com.proxy.service.document.base.image.loader.base.ILoader
import com.proxy.service.document.base.image.loader.base.IOption
import com.proxy.service.document.base.image.loader.crop.ICropController
import com.proxy.service.document.image.func.crop.controller.CropControllerImpl

/**
 * @author: cangHX
 * @data: 2025/6/3 10:12
 * @desc:
 */
open class CropLoaderImpl(
    private val option: IOption
) : ILoader<ICropController> {

    protected var cropRect: RectF? = null
    protected var cropWidthPx: Int = Constants.DEFAULT_CROP_SIZE
    protected var cropHeightPx: Int = Constants.DEFAULT_CROP_SIZE

    protected var maskColor: Int = Color.parseColor(Constants.DEFAULT_CROP_MASK_COLOR)

    protected var cropLineColor: Int = Color.parseColor(Constants.DEFAULT_CROP_LINE_COLOR)

    protected var cropLineWidth: Float = Constants.DEFAULT_CROP_LINE_WIDTH

    protected var drawCropCallback: OnDrawCropCallback? = null

    override fun into(imageView: ImageView): ICropController {
        val crop = createCropController(option)
        crop.setController(option.into(imageView))
        return crop
    }

    override fun into(linearLayout: LinearLayout): ICropController {
        val crop = createCropController(option)
        crop.setController(option.into(linearLayout))
        return crop
    }

    override fun into(relativeLayout: RelativeLayout): ICropController {
        val crop = createCropController(option)
        crop.setController(option.into(relativeLayout))
        return crop
    }

    override fun into(frameLayout: FrameLayout): ICropController {
        val crop = createCropController(option)
        crop.setController(option.into(frameLayout))
        return crop
    }

    override fun into(viewGroup: ViewGroup): ICropController {
        val crop = createCropController(option)
        crop.setController(option.into(viewGroup))
        return crop
    }

    private fun createCropController(option: IOption): CropControllerImpl {
        val info = CropInfo()
        info.cropRect = cropRect
        info.cropWidthPx = cropWidthPx
        info.cropHeightPx = cropHeightPx

        info.maskColor = maskColor

        info.cropLineColor = cropLineColor
        info.cropLineWidth = cropLineWidth

        info.drawCropCallback = drawCropCallback

        val controller = CropControllerImpl(option, info)
        controller.init()
        return controller
    }
}