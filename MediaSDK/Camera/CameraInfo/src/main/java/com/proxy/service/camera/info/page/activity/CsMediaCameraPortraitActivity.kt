package com.proxy.service.camera.info.page.activity

import com.proxy.service.camera.base.constants.CameraConstants
import com.proxy.service.camera.info.page.activity.base.AbstractCameraActionActivity

/**
 * @author: cangHX
 * @data: 2026/2/6 15:09
 * @desc:
 */
class CsMediaCameraPortraitActivity : AbstractCameraActionActivity() {

    companion object {
        private const val TAG = "${CameraConstants.TAG}PortraitActivity"
    }

    override fun getTag(): String {
        return TAG
    }

}