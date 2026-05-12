package com.proxy.service.core.framework.system.security.aes.impl.action.encrypt

import com.proxy.service.core.framework.data.string.CsStringUtils
import com.proxy.service.core.framework.system.security.aes.base.action.IInstantRun
import com.proxy.service.core.framework.system.security.aes.base.controller.IController
import java.nio.charset.StandardCharsets

/**
 * @author: cangHX
 * @date: 2024/12/4 10:09
 * @desc:
 */
class SecurityEncryptInstantRun(
    private val controller: IController
): IInstantRun {

    override fun updateSourceString(str: String): ByteArray? {
        val result = controller.updateWithOutCache(str.toByteArray(StandardCharsets.UTF_8))
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
