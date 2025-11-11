package com.proxy.service.imageloader.info.pag.loader

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.proxy.service.imageloader.base.loader.pag.IPagLoader
import com.proxy.service.imageloader.base.loader.pag.PagController
import com.proxy.service.imageloader.base.option.pag.scene.PagSceneMode
import com.proxy.service.imageloader.info.pag.info.PagInfo
import com.proxy.service.imageloader.info.pag.loader.controller.PagControllerEmpty
import com.proxy.service.imageloader.info.pag.loader.controller.PagControllerImpl
import com.proxy.service.imageloader.info.pag.loader.view.IView
import com.proxy.service.imageloader.info.pag.loader.view.PagImageViewImpl
import com.proxy.service.imageloader.info.pag.loader.view.PagViewImpl
import com.proxy.service.imageloader.info.utils.ViewUtils

/**
 * @author: cangHX
 * @data: 2025/10/10 17:08
 * @desc:
 */
open class PagLoaderImpl(
    private val info: PagInfo
) : IPagLoader {

    override fun into(linearLayout: LinearLayout?): PagController {
        return into(linearLayout as? ViewGroup?)
    }

    override fun into(relativeLayout: RelativeLayout?): PagController {
        return into(relativeLayout as? ViewGroup?)
    }

    override fun into(frameLayout: FrameLayout?): PagController {
        return into(frameLayout as? ViewGroup?)
    }

    override fun into(viewGroup: ViewGroup?): PagController {
        if (viewGroup == null) {
            return PagControllerEmpty()
        }
        val view = getPagView(viewGroup)
        val controller = PagControllerImpl(view)
        info.loadConfig(view, controller)
        return controller
    }

    private fun getPagView(viewGroup: ViewGroup): IView {
        var index = 0
        var pagView: IView? = null
        while (index < viewGroup.childCount) {
            val child = viewGroup.getChildAt(index)
            if (child is PagViewImpl && info.sceneMode == PagSceneMode.QUALITY) {
                pagView = child
                break
            } else if (child is PagImageViewImpl && info.sceneMode == PagSceneMode.PERFORMANCE) {
                pagView = child
                break
            }
            index++
        }
        if (pagView == null) {
            pagView = if (info.sceneMode == PagSceneMode.QUALITY) {
                PagViewImpl(viewGroup.context)
            } else {
                PagImageViewImpl(viewGroup.context)
            }
            ViewUtils.addView(pagView as View, viewGroup, info.width, info.height)
        }
        return pagView
    }

}