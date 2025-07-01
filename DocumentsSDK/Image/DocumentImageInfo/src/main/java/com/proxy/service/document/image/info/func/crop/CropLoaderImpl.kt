package com.proxy.service.document.image.info.func.crop

import android.graphics.Color
import android.graphics.RectF
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.callback.crop.OnDrawCropCallback
import com.proxy.service.document.image.base.loader.base.ILoader
import com.proxy.service.document.image.base.loader.base.IOption
import com.proxy.service.document.image.base.loader.crop.ICropController
import com.proxy.service.document.image.base.mode.CropMode
import com.proxy.service.document.image.info.func.crop.controller.BaseController
import com.proxy.service.document.image.info.func.crop.controller.BaseImageMoveAndScaleControllerImpl
import com.proxy.service.document.image.info.func.crop.controller.CropFrameMoveAndScaleControllerImpl
import com.proxy.service.document.image.info.func.crop.controller.CropFrameMoveControllerImpl

/**
 * @author: cangHX
 * @data: 2025/6/3 10:12
 * @desc:
 */
open class CropLoaderImpl(
    private val option: IOption
) : ILoader<ICropController> {

    companion object {
        private const val TAG = "${ImageConstants.LOG_TAG_IMAGE_START}CropLoader"
    }

    protected var mode: CropMode = CropMode.builderBaseImageMoveAndScaleMode().build()

    protected var cropFrameFitBitmap: Boolean = false
    protected var cropFrameRect: RectF? = null
    protected var cropFrameWidthPx: Float? = null
    protected var cropFrameHeightPx: Float? = null
    protected var cropFrameLineColor: Int = Color.parseColor(ImageConstants.DEFAULT_CROP_FRAME_LINE_COLOR)
    protected var cropFrameLineWidth: Float = ImageConstants.DEFAULT_CROP_FRAME_LINE_WIDTH

    protected var maskColor: Int = Color.parseColor(ImageConstants.DEFAULT_CROP_MASK_COLOR)

    protected var drawCropCallback: OnDrawCropCallback? = null

    override fun into(imageView: ImageView): ICropController {
        val crop = createCropController(option, mode)
        crop.setController(option.into(imageView))
        return crop
    }

    override fun into(linearLayout: LinearLayout): ICropController {
        val crop = createCropController(option, mode)
        crop.setController(option.into(linearLayout))
        return crop
    }

    override fun into(relativeLayout: RelativeLayout): ICropController {
        val crop = createCropController(option, mode)
        crop.setController(option.into(relativeLayout))
        return crop
    }

    override fun into(frameLayout: FrameLayout): ICropController {
        val crop = createCropController(option, mode)
        crop.setController(option.into(frameLayout))
        return crop
    }

    override fun into(viewGroup: ViewGroup): ICropController {
        val crop = createCropController(option, mode)
        crop.setController(option.into(viewGroup))
        return crop
    }

    private fun createCropController(option: IOption, mode: CropMode): BaseController {
        val info = CropInfo()
        info.cropFrameFitBitmap = cropFrameFitBitmap
        info.cropFrameRect = cropFrameRect
        info.cropFrameWidthPx = cropFrameWidthPx
        info.cropFrameHeightPx = cropFrameHeightPx
        info.cropFrameLineColor = cropFrameLineColor
        info.cropFrameLineWidth = cropFrameLineWidth

        info.maskColor = maskColor

        info.drawCropCallback = drawCropCallback

        val controller = when (mode) {
            is CropMode.BaseImageMoveAndScaleMode -> {
                BaseImageMoveAndScaleControllerImpl(option, info, mode)
            }

            is CropMode.CropFrameMoveAndScaleMode -> {
                CropFrameMoveAndScaleControllerImpl(option, info, mode)
            }

            is CropMode.CropFrameMoveMode -> {
                CropFrameMoveControllerImpl(option, info, mode)
            }

            else -> {
                CsLogger.tag(TAG).e("Unsupported mode. ${mode.javaClass.name}")
                val currentMode = CropMode.BaseImageMoveAndScaleMode()
                BaseImageMoveAndScaleControllerImpl(option, info, currentMode)
            }
        }

        controller.init()
        return controller
    }
}