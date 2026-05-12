package com.proxy.service.core.framework.system.security.aes.impl.action.decrypt

import com.proxy.service.core.framework.data.string.CsStringUtils
import com.proxy.service.core.framework.system.security.aes.base.action.IInstantRun
import com.proxy.service.core.framework.system.security.aes.base.controller.IController

/**
 * @author: cangHX
 * @date: 2024/12/4 10:09
 * @desc:
 */
class SecurityDecryptInstantRun(
    private val controller: IController
) : IInstantRun {

    override fun updateSourceString(str: String): ByteArray? {
        val bytes = CsStringUtils.parseHexStr2Byte(str) ?: return null
        val result = controller.updateWithOutCache(bytes)
        if (controller.isError()) {
            return null
        }
        return result
    }

    override fun updateSourceBase64String(base64: String): ByteArray? {
        val bytes = CsStringUtils.parseBase64Str2Byte(base64) ?: return null
        val result = controller.updateWithOutCache(bytes)
        if (controller.isError()) {
            return null
        }
        return result
    }

    override fun updateSourceByteArray(byteArray: ByteArray): ByteArray? {
        val result = controller.updateWithOutCache(byteArray)
        if (controller.isError()) {
            return null
        }
        return result
    }

    override fun endInstantRun(): ByteArray? {
        val result = controller.finish()
        if (controller.isError()) {
            return null
        }
        return result
    }
}
