package com.proxy.service.document.image.info.func.preview

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.proxy.service.document.image.base.constants.ImageConstants
import com.proxy.service.document.image.base.callback.base.OnBoundChangedCallback
import com.proxy.service.document.image.base.callback.base.OnDoubleClickCallback
import com.proxy.service.document.image.base.callback.base.OnDragCallback
import com.proxy.service.document.image.base.callback.base.OnDrawCallback
import com.proxy.service.document.image.base.callback.base.OnLongPressCallback
import com.proxy.service.document.image.base.callback.base.OnScaleCallback
import com.proxy.service.document.image.base.callback.base.OnSingleClickCallback
import com.proxy.service.document.image.base.callback.base.OnTouchEventCallback
import com.proxy.service.document.image.base.loader.base.IController
import com.proxy.service.document.image.base.loader.base.ILoader
import com.proxy.service.document.image.info.drawable.ActionDrawable
import com.proxy.service.document.image.info.drawable.ConfigInfo
import com.proxy.service.document.image.info.func.preview.factory.LayoutFactory
import com.proxy.service.imageloader.base.option.glide.IGlideOption
import com.proxy.service.imageloader.base.target.ITarget

/**
 * @author: cangHX
 * @data: 2025/5/30 10:49
 * @desc:
 */
open class LoaderImpl(
    private val glideOption: IGlideOption<Bitmap>?
) : ILoader<IController> {

    protected var minScale = ImageConstants.DEFAULT_MIN_SCALE
    protected var maxScale = ImageConstants.DEFAULT_MAX_SCALE

    protected var lockSizeWidthPx: Float? = null
    protected var lockSizeHeightPx: Float? = null
    protected var lockRect: RectF? = null
    protected var lockView: Boolean = false
    protected var canMoveInLockRect: Boolean = true
    protected var overScrollBounceEnabled: Boolean = true

    protected var boundChangedCallback: OnBoundChangedCallback? = null
    protected var touchEventCallback: OnTouchEventCallback? = null
    protected var dragCallback: OnDragCallback? = null
    protected var scaleCallback: OnScaleCallback? = null
    protected var drawCallback: OnDrawCallback? = null
    protected var singleClickCallback: OnSingleClickCallback? = null
    protected var doubleClickCallback: OnDoubleClickCallback? = null
    protected var longPressCallback: OnLongPressCallback? = null

    override fun into(imageView: ImageView): IController {
        val controller = ControllerImpl()
        bindView(imageView, controller)
        return controller
    }

    override fun into(linearLayout: LinearLayout): IController {
        return into(LayoutFactory.loadImageView(linearLayout))
    }

    override fun into(relativeLayout: RelativeLayout): IController {
        return into(LayoutFactory.loadImageView(relativeLayout))
    }

    override fun into(frameLayout: FrameLayout): IController {
        return into(LayoutFactory.loadImageView(frameLayout))
    }

    override fun into(viewGroup: ViewGroup): IController {
        return into(LayoutFactory.loadImageView(viewGroup))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun bindView(imageView: ImageView?, controller: ControllerImpl) {
        if (imageView == null) {
            return
        }
        glideOption?.into(object : ITarget<Bitmap> {
            override fun onLoadCleared(placeholder: Drawable?) {
                imageView.setImageDrawable(null)
                imageView.setOnTouchListener(null)
            }

            override fun onResourceReady(resource: Bitmap?) {
                resource?.let {
                    val config = ConfigInfo()
                    config.minScale = minScale
                    config.maxScale = maxScale

                    config.lockSizeWidthPx = lockSizeWidthPx
                    config.lockSizeHeightPx = lockSizeHeightPx
                    config.lockRect = lockRect
                    config.lockView = lockView
                    config.canMoveInLockRect = canMoveInLockRect
                    config.overScrollBounceEnabled = overScrollBounceEnabled

                    config.boundChangedCallback = boundChangedCallback
                    config.touchEventCallback = touchEventCallback
                    config.dragCallback = dragCallback
                    config.scaleCallback = scaleCallback
                    config.drawCallback = drawCallback
                    config.singleClickCallback = singleClickCallback
                    config.doubleClickCallback = doubleClickCallback
                    config.longPressCallback = longPressCallback

                    val drawable = ActionDrawable(imageView.context, it, config)
                    controller.setDrawable(drawable)
                    imageView.setImageDrawable(drawable)
                    imageView.setOnTouchListener { _, event ->
                        drawable.onTouchEvent(event)
                        true
                    }
                }
            }
        })
    }
}