package com.proxy.service.camera.info.page

import com.proxy.service.camera.base.callback.PageTakePictureCallback
import java.io.Serializable

/**
 * @author: cangHX
 * @data: 2026/2/8 18:00
 * @desc:
 */
class MediaCameraParams : Serializable {

    var takePictureCallback: PageTakePictureCallback? = null

}