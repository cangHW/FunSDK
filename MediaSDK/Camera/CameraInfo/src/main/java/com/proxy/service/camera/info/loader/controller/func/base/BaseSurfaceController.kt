package com.proxy.service.camera.info.loader.controller.func.base

import android.view.Surface
import com.proxy.service.camera.base.loader.controller.ICameraController
import com.proxy.service.camera.info.loader.controller.IFunController
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @data: 2026/4/20 11:19
 * @desc:
 */
abstract class BaseSurfaceController : IFunController, ICameraController {

    protected var width: Int = 1080
    protected var height: Int = 720

    protected var funSurfaceChangedCallback: IFunController.SurfaceChangedCallback? = null
    protected var funParamsController: IFunController.IParamsController? = null

    override fun getSurface(): Surface? {
        try {
            return createSurface()
        } catch (throwable: Throwable) {
            CsLogger.tag(getTag()).e(throwable)
        }
        return null
    }

    override fun setSurfaceChangedCallback(callback: IFunController.SurfaceChangedCallback) {
        this.funSurfaceChangedCallback = callback
    }

    override fun setParamsController(controller: IFunController.IParamsController) {
        this.funParamsController = controller
    }

    override fun setSurfaceSize(width: Int, height: Int) {
        CsLogger.tag(getTag()).i("setSurfaceSize. width=$width, height=$height")
        if (width <= 0 || height <= 0) {
            CsLogger.tag(getTag()).w("Invalid size, ignore.")
            return
        }
        if (this.width != width || this.height != height) {
            this.width = width
            this.height = height
            resetSurface()
            createSurface()
        }
    }

    override fun destroy() {
        funSurfaceChangedCallback = null
        funParamsController = null
    }

    /**
     * 获取日志 tag
     * */
    protected abstract fun getTag(): String

    /**
     * 重置 surface
     * */
    protected abstract fun resetSurface()

    /**
     * 生成 surface
     * */
    protected abstract fun createSurface(): Surface?
}