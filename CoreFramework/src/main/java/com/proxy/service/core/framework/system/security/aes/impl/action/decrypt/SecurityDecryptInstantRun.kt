package com.proxy.service.core.framework.system.security.aes.impl.action.decrypt

import com.proxy.service.core.framework.data.string.CsStringUtils
import com.proxy.service.core.framework.system.security.aes.base.action.IInstantRun
import com.proxy.service.core.framework.system.security.aes.base.controller.IController

/**
 * @author: cangHX
 * @data: 2024/12/4 10:09
 * @desc:
 */
class SecurityDecryptInstantRun(
    private val controller: IController
) : IInstantRun {
    override fun updateSourceString(str: String): ByteArray {
        return CsStringUtils.parseHexStr2Byte(str)?.let {
            controller.updateWithOutCache(it)
        } ?: ByteArray(0)
    }

    override fun updateSourceBase64String(base64: String): ByteArray {
        return CsStringUtils.parseBase64Str2Byte(base64)?.let {
            controller.updateWithOutCache(it)
        } ?: ByteArray(0)
    }

    override fun updateSourceByteArray(byteArray: ByteArray): ByteArray {
        return controller.updateWithOutCache(byteArray)
    }

    override fun endInstantRun(): ByteArray {
        return controller.finish()
    }
}