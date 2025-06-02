package com.proxy.service.document.image.loader

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.proxy.service.document.base.constants.Constants
import com.proxy.service.document.base.image.callback.loader.OnBoundChangedCallback
import com.proxy.service.document.base.image.callback.loader.OnDragCallback
import com.proxy.service.document.base.image.callback.loader.OnDrawCallback
import com.proxy.service.document.base.image.callback.loader.OnScaleCallback
import com.proxy.service.document.base.image.loader.IController
import com.proxy.service.document.base.image.loader.ILoader
import com.proxy.service.document.image.drawable.ConfigInfo
import com.proxy.service.document.image.drawable.ActionDrawable
import com.proxy.service.document.image.loader.factory.LayoutFactory
import com.proxy.service.imageloader.base.option.glide.IGlideOption
import com.proxy.service.imageloader.base.target.ITarget

/**
 * @author: cangHX
 * @data: 2025/5/30 10:49
 * @desc:
 */
open class LoaderImpl(
    private val glideOption: IGlideOption<Bitmap>?
) : ILoader {

    protected var minScale = Constants.DEFAULT_MIN_SCALE
    protected var maxScale = Constants.DEFAULT_MAX_SCALE

    protected var lockRect: Rect? = null

    protected var boundChangedCallback: OnBoundChangedCallback? = null
    protected var dragCallback: OnDragCallback? = null
    protected var scaleCallback: OnScaleCallback? = null
    protected var drawCallback: OnDrawCallback? = null

    override fun into(imageView: ImageView): IController {
        bindView(imageView)
        return ControllerImpl()
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
    private fun bindView(imageView: ImageView?) {
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
                    config.lockRect = lockRect
                    config.boundChangedCallback = boundChangedCallback
                    config.dragCallback = dragCallback
                    config.scaleCallback = scaleCallback
                    config.drawCallback = drawCallback

                    val drawable = ActionDrawable(imageView.context, it, config)
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